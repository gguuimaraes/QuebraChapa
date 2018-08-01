package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.data.PageEvent;

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

	int rowsPerPage = 10;

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

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
		Uteis.MensagemInfo("Quebra cadastrada com sucesso");
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

	public void onRowEdit(RowEditEvent event) {
		quebraDao.salvar((QuebraModel) event.getObject());
		Uteis.MensagemInfo("Quebra alterada com sucesso");
	}

	public void onRowCancel(RowEditEvent event) {
		Uteis.MensagemInfo("Operação cancelada");
	}

	public Float getSomaAreaTotal() {
		System.out.println("Linhas por página: " + rowsPerPage);
		float soma = 0;
		for (int l = 0; l < (rowsPerPage <= quebras.size() ? rowsPerPage : quebras.size()); l++) {
			soma += quebras.get(l).getAreaTotal();
		}
		System.out.println("getSomaAreaTotal: " + soma);
		return soma;
	}

	public void onPaginate(PageEvent event) {
		DataTable tabela = ((org.primefaces.component.datatable.DataTable) event.getSource());
		System.out.println("Linha: " + tabela.getRows());
	}
}