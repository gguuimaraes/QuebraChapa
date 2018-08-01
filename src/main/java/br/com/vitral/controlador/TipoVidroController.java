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

import br.com.vitral.modelo.TipoVidroModel;
import br.com.vitral.persistencia.TipoVidroDao;
import br.com.vitral.util.Uteis;

@Named(value = "tipoVidroController")
@SessionScoped
public class TipoVidroController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private TipoVidroModel tipoVidroModel;

	@Inject
	private TipoVidroDao tipoVidroDao;

	@Produces
	private List<TipoVidroModel> tiposVidro;

	@PostConstruct
	public void init() {
		tiposVidro = tipoVidroDao.listar();
	}

	public void cadastrar() {
		tipoVidroModel = new TipoVidroModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		tipoVidroDao.salvar(tipoVidroModel);
		init();
		this.tipoVidroModel = new TipoVidroModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		Uteis.MensagemInfo("Tipo Vidro cadastrado com sucesso");
	}

	public void excluir(TipoVidroModel tipoVidroModel) {
		tipoVidroDao.remover(tipoVidroModel.getId());
		tiposVidro.remove(tipoVidroModel);
	}

	public TipoVidroModel getTipoVidroModel() {
		return this.tipoVidroModel;
	}

	public void setTipoVidroModel(TipoVidroModel tipoVidroModel) {
		this.tipoVidroModel = tipoVidroModel;
	}

	public List<TipoVidroModel> getTiposVidro() {
		return tiposVidro;
	}

	public void onRowEdit(RowEditEvent event) {
		tipoVidroDao.salvar((TipoVidroModel) event.getObject());
		Uteis.MensagemInfo("Tipo Vidro alterado com sucesso");
	}

	public void onRowCancel(RowEditEvent event) {
		Uteis.MensagemInfo("Operação cancelada");
	}
}