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

import br.com.vitral.modelo.AvisoModel;
import br.com.vitral.persistencia.AvisoDao;
import br.com.vitral.util.Uteis;

@Named(value = "avisoController")
@SessionScoped
public class AvisoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AvisoModel avisoModel;

	@Inject
	private AvisoDao avisoDao;

	@Produces
	private List<AvisoModel> avisos;

	@PostConstruct
	public void init() {
		avisos = avisoDao.listar();
	}

	public void cadastrar() {
		avisoModel = new AvisoModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}
	
	public void modificar(AvisoModel avisoModel) {
		this.avisoModel = avisoModel;
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		avisoDao.salvar(avisoModel);
		init();
		this.avisoModel = new AvisoModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		Uteis.messageInformation("Aviso cadastrado com sucesso");
	}

	public void excluir(AvisoModel avisoModel) {
		avisoDao.remover(avisoModel.getId());
		avisos.remove(avisoModel);
	}

	public AvisoModel getAvisoModel() {
		return this.avisoModel;
	}

	public void setAvisoModel(AvisoModel avisoModel) {
		this.avisoModel = avisoModel;
	}

	public List<AvisoModel> getAvisos() {
		return avisos;
	}

	public void setAvisos(List<AvisoModel> avisos) {
		this.avisos = avisos;
	}

	public void onRowEdit(RowEditEvent event) {
		avisoDao.salvar((AvisoModel) event.getObject());
		Uteis.messageInformation("Aviso alterado com sucesso");
	}

	public void onRowCancel(RowEditEvent event) {
		AvisoModel avisoModel = (AvisoModel) event.getObject();
		avisos.set(avisos.indexOf(avisoModel), avisoDao.consultarModel(avisoModel.getId()));
		Uteis.messageInformation("Operação cancelada");
	}
}