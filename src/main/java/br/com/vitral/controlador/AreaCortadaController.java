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

import br.com.vitral.modelo.AreaCortadaModel;
import br.com.vitral.persistencia.AreaCortadaDao;
import br.com.vitral.util.Uteis;

@Named(value = "areaCortadaController")
@SessionScoped
public class AreaCortadaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AreaCortadaModel areaCortadaModel;

	@Inject
	private AreaCortadaDao areaCortadaDao;

	@Produces
	private List<AreaCortadaModel> areasCortadas;

	@PostConstruct
	public void init() {
		areasCortadas = areaCortadaDao.listar();
	}

	public void cadastrar() {
		areaCortadaModel = new AreaCortadaModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		areaCortadaDao.salvar(areaCortadaModel);
		init();
		this.areaCortadaModel = new AreaCortadaModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		Uteis.MensagemInfo("Área Cortada cadastrada com sucesso");
	}

	public void excluir(AreaCortadaModel areaCortadaModel) {
		areaCortadaDao.remover(areaCortadaModel.getId());
		areasCortadas.remove(areaCortadaModel);
	}

	public AreaCortadaModel getAreaCortadaModel() {
		return this.areaCortadaModel;
	}

	public void setAreaCortadaModel(AreaCortadaModel areaCortadaModel) {
		this.areaCortadaModel = areaCortadaModel;
	}

	public List<AreaCortadaModel> getAreasCortadas() {
		return areasCortadas;
	}

	public void onRowEdit(RowEditEvent event) {
		areaCortadaDao.salvar((AreaCortadaModel) event.getObject());
		Uteis.MensagemInfo("Área Cortada alterada com sucesso");
	}

	public void onRowCancel(RowEditEvent event) {
		Uteis.MensagemInfo("Operação cancelada");
	}
}