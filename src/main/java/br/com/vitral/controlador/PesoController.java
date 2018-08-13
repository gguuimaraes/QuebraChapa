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

import br.com.vitral.modelo.PesoModel;
import br.com.vitral.persistencia.PesoDao;
import br.com.vitral.util.Uteis;

@Named(value = "pesoController")
@SessionScoped
public class PesoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PesoModel pesoModel;

	@Inject
	private PesoDao pesoDao;

	@Produces
	private List<PesoModel> pesos;

	@PostConstruct
	public void init() {
		pesos = pesoDao.listar();
	}

	public void cadastrar() {
		pesoModel = new PesoModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		pesoDao.salvar(pesoModel);
		init();
		this.pesoModel = new PesoModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		Uteis.messageInformation("Peso cadastrado com sucesso");
	}

	public void excluir(PesoModel pesoModel) {
		System.out.println(pesoModel);
		pesoDao.remover(pesoModel.getId());
		pesos.remove(pesoModel);
	}

	public PesoModel getPesoModel() {
		return this.pesoModel;
	}

	public void setPesoModel(PesoModel pesoModel) {
		this.pesoModel = pesoModel;
	}

	public List<PesoModel> getPesos() {
		return pesos;
	}

	public void onRowEdit(RowEditEvent event) {
		pesoDao.salvar((PesoModel) event.getObject());
		Uteis.messageInformation("Peso alterado com sucesso");
	}

	public void onRowCancel() {
		Uteis.messageInformation("Operação cancelada");
	}
}