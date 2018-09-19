package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

import br.com.vitral.modelo.SetorModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.persistencia.SetorDao;
import br.com.vitral.util.Uteis;

@Named(value = "setorController")
@SessionScoped
public class SetorController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private SetorModel setorModel;

	@Inject
	private SetorDao setorDao;

	@Produces
	private List<SetorModel> setores;

	@PostConstruct
	public void init() {
		setores = setorDao.listar();
	}

	public void cadastrar() {
		setorModel = new SetorModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		setorDao.salvar(setorModel);
		init();
		this.setorModel = new SetorModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		Uteis.messageInformation("Setor cadastrado com sucesso");
	}

	public void excluir(SetorModel setorModel) {
		setorDao.remover(setorModel.getId());
		setores.remove(setorModel);
	}

	public SetorModel getSetorModel() {
		return this.setorModel;
	}

	public void setSetorModel(SetorModel setorModel) {
		this.setorModel = setorModel;
	}

	public List<SetorModel> getSetores() {
		return setores;
	}

	public void onRowEdit(RowEditEvent event) {
		setorDao.salvar((SetorModel) event.getObject());
		Uteis.messageInformation("Setor alterado com sucesso");
	}

	public void onRowCancel(RowEditEvent event) {
		SetorModel setorModel = (SetorModel) event.getObject();
		setores.set(setores.indexOf(setorModel), setorDao.consultarModel(setorModel.getId()));
		Uteis.messageInformation("Operação cancelada");
	}
}