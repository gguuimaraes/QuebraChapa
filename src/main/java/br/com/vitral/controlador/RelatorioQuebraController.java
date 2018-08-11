package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.primefaces.PrimeFaces;

import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.QuebraModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.modelo.TipoVidroModel;
import br.com.vitral.persistencia.QuebraDao;
import br.com.vitral.util.Uteis;

@Named(value = "relatorioQuebraController")
@SessionScoped
public class RelatorioQuebraController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private QuebraDao quebraDao;

	@Produces
	private List<QuebraModel> quebras;

	private Date dataInicio = Uteis.getDataInicio();

	private Date dataFim = Uteis.getDataFim();

	private FuncionarioModel funcionario;

	private TipoVidroModel tipoVidro;

	private SetorModel setor;

	public List<QuebraModel> getQuebras() {
		return quebras;
	}

	public void setQuebras(List<QuebraModel> quebras) {
		this.quebras = quebras;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public FuncionarioModel getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioModel funcionario) {
		this.funcionario = funcionario;
	}

	public TipoVidroModel getTipoVidro() {
		return tipoVidro;
	}

	public void setTipoVidro(TipoVidroModel tipoVidro) {
		this.tipoVidro = tipoVidro;
	}

	public SetorModel getSetor() {
		return setor;
	}

	public void setSetor(SetorModel setor) {
		this.setor = setor;
	}

	public double getSomaAreaTotal() {
		double soma = 0;
		if (quebras == null)
			return soma;
		for (QuebraModel q : quebras) {
			soma += q.getAreaTotal();
		}
		return soma;
	}

	public double getSomaAreaAproveitada() {
		double soma = 0;
		if (quebras == null)
			return soma;
		for (QuebraModel q : quebras) {
			soma += q.getAreaAproveitada();
		}
		return soma;
	}

	public void executar() {
		List<Object> parametros = new ArrayList<>();
		if (setor != null)
			parametros.add(setor);
		if (tipoVidro != null)
			parametros.add(tipoVidro);
		if (funcionario != null)
			parametros.add(funcionario);
		quebras = quebraDao.listarPorPeriodo(dataInicio, dataFim, parametros.toArray());
	}

	public void limpar() {
		if (quebras != null)
			quebras.clear();
		PrimeFaces.current().resetInputs("form");
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
		for (int j = 0; j < header.getPhysicalNumberOfCells(); j++)
			header.getCell(j).setCellStyle(headerStyle);

		HSSFCellStyle parStyle = wb.createCellStyle();
		parStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		parStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		HSSFCellStyle imparStyle = wb.createCellStyle();
		imparStyle.setFillForegroundColor(HSSFColor.WHITE.index);
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
		footerStyle.setFillForegroundColor(HSSFColor.BLACK.index);
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
	
	private static FuncionarioModel funcionarioNenhum = new FuncionarioModel();
	
	public FuncionarioModel getFuncionarioNenhum() {
		funcionarioNenhum.setId(0);
		return funcionarioNenhum;
	}

}