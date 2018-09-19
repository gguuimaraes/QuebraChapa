package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

import br.com.vitral.modelo.QuebraModel;
import br.com.vitral.modelo.QuebraModel;
import br.com.vitral.persistencia.QuebraDao;
import br.com.vitral.util.Uteis;

@Named(value = "quebraController")
@SessionScoped
public class QuebraController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private QuebraModel quebraModel;

	@Inject
	private QuebraDao quebraDao;

	@Produces
	private List<QuebraModel> quebras;

	@Produces
	private List<QuebraModel> quebrasFiltradas;

	@PostConstruct
	public void init() {
		quebras = quebraDao.listar();
	}

	public void cadastrar() {
		quebraModel = new QuebraModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		quebraDao.salvar(quebraModel);
		init();
		this.quebraModel = new QuebraModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		PrimeFaces.current().executeScript("PF('quebras').clearFilters();");
		Uteis.messageInformation("Quebra cadastrada com sucesso");
	}

	public void excluir(QuebraModel quebraModel) {
		quebraDao.remover(quebraModel.getId());
		quebras.remove(quebraModel);
	}

	public QuebraModel getQuebraModel() {
		return this.quebraModel;
	}

	public void setQuebraModel(QuebraModel quebraModel) {
		this.quebraModel = quebraModel;
	}

	public List<QuebraModel> getQuebras() {
		return quebras;
	}

	public List<QuebraModel> getQuebrasFiltradas() {
		return quebrasFiltradas;
	}

	public void setQuebrasFiltradas(List<QuebraModel> quebrasFiltradas) {
		this.quebrasFiltradas = quebrasFiltradas;
	}

	public boolean filtrarFuncionario(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals(""))
			return true;

		if (value == null)
			value = "Nenhum";

		return value.toString().compareTo(filterText) == 0;
	}

	public void postProcessXLS(Object document) {
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);

		HSSFRow header = sheet.getRow(0);
		Font headerFont = wb.createFont();
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		headerFont.setBold(true);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headerStyle.setFont(headerFont);
		headerStyle.setAlignment(HorizontalAlignment.CENTER);
		for (int j = 0; j < header.getPhysicalNumberOfCells(); j++) {
			HSSFCell cell = header.getCell(j);
			cell.setCellStyle(headerStyle);
		}

		HSSFCellStyle parStyle = wb.createCellStyle();
		parStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		parStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		HSSFCellStyle imparStyle = wb.createCellStyle();
		imparStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		imparStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
			HSSFRow linha = sheet.getRow(i);

			if (i % 2 == 0)
				for (int j = 0; j < linha.getPhysicalNumberOfCells(); j++) {
					linha.getCell(j).setCellStyle(parStyle);
				}
			else
				for (int j = 0; j < linha.getPhysicalNumberOfCells(); j++) {
					linha.getCell(j).setCellStyle(imparStyle);
				}

			String stringValue = linha.getCell(2).getStringCellValue().substring(0, 6).replace(",", ".");
			double doubleValue = Double.parseDouble(stringValue);
			linha.getCell(2).setCellValue(doubleValue);

			stringValue = linha.getCell(3).getStringCellValue().substring(0, 6).replace(",", ".");
			doubleValue = Double.parseDouble(stringValue);
			linha.getCell(3).setCellValue(doubleValue);
		}

		Font footerFont = wb.createFont();
		footerFont.setColor(IndexedColors.WHITE.getIndex());
		footerFont.setBold(true);
		footerFont.setItalic(true);
		HSSFCellStyle footerStyle = wb.createCellStyle();
		footerStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
		footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		footerStyle.setFont(footerFont);
		footerStyle.setAlignment(HorizontalAlignment.RIGHT);
		HSSFRow footer = sheet.createRow(sheet.getPhysicalNumberOfRows());
		for (int j = 0; j < header.getPhysicalNumberOfCells(); j++)
			footer.createCell(j);
		footer.getCell(1).setCellValue("TOTAL");
		int linhas = sheet.getPhysicalNumberOfRows() - 1;
		footer.getCell(2).setCellFormula("SUM(C2:C" + linhas + ")");
		footer.getCell(3).setCellFormula("SUM(D2:D" + linhas + ")");
		for (int j = 0; j < footer.getPhysicalNumberOfCells(); j++)
			footer.getCell(j).setCellStyle(footerStyle);
	}

	public void onRowEdit(RowEditEvent event) {
		quebraDao.salvar((QuebraModel) event.getObject());
		Uteis.messageInformation("Quebra alterada com sucesso");
	}

	public void onRowCancel(RowEditEvent event) {
		QuebraModel quebraModel = (QuebraModel) event.getObject();
		quebras.set(quebras.indexOf(quebraModel), quebraDao.consultarModel(quebraModel.getId()));
		Uteis.messageInformation("Operação cancelada");
	}

}