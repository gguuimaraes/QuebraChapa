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

import br.com.vitral.modelo.AreaFaturadaModel;
import br.com.vitral.modelo.AreaFaturadaModel;
import br.com.vitral.persistencia.AreaFaturadaDao;
import br.com.vitral.util.Uteis;

@Named(value = "areaFaturadaController")
@SessionScoped
public class AreaFaturadaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AreaFaturadaModel areaFaturadaModel;

	@Inject
	private AreaFaturadaDao areaFaturadaDao;

	@Produces
	private List<AreaFaturadaModel> areasFaturadas;

	@PostConstruct
	public void init() {
		areasFaturadas = areaFaturadaDao.listar();
	}

	public void cadastrar() {
		areaFaturadaModel = new AreaFaturadaModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		areaFaturadaDao.salvar(areaFaturadaModel);
		init();
		this.areaFaturadaModel = new AreaFaturadaModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		Uteis.messageInformation("Área Faturada cadastrada com sucesso");
	}

	public void excluir(AreaFaturadaModel areaFaturadaModel) {
		areaFaturadaDao.remover(areaFaturadaModel.getId());
		areasFaturadas.remove(areaFaturadaModel);
	}

	public AreaFaturadaModel getAreaFaturadaModel() {
		return this.areaFaturadaModel;
	}

	public void setAreaFaturadaModel(AreaFaturadaModel areaFaturadaModel) {
		this.areaFaturadaModel = areaFaturadaModel;
	}

	public List<AreaFaturadaModel> getAreasFaturadas() {
		return areasFaturadas;
	}

	public void onRowEdit(RowEditEvent event) {
		areaFaturadaDao.salvar((AreaFaturadaModel) event.getObject());
		Uteis.messageInformation("Área Faturada alterada com sucesso");
	}

	public void onRowCancel(RowEditEvent event) {
		AreaFaturadaModel areaCortadaModel = (AreaFaturadaModel) event.getObject();
		areasFaturadas.set(areasFaturadas.indexOf(areaCortadaModel), areaFaturadaDao.consultarModel(areaCortadaModel.getId()));
		Uteis.messageInformation("Operação cancelada");
	}
}