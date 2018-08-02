package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.data.FilterEvent;
import org.primefaces.event.data.PageEvent;
import org.primefaces.model.FilterMeta;

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

}