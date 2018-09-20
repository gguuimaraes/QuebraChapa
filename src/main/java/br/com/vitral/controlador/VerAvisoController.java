package br.com.vitral.controlador;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vitral.modelo.AvisoModel;
import br.com.vitral.persistencia.AvisoDao;

@Named(value = "verAvisoController")
@SessionScoped
public class VerAvisoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	AvisoDao avisoDao;

	AvisoModel avisoModel;

	public void onPageLoad() {
		try {
			String strId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("aviso");
			Integer avisoId = Integer.parseInt(strId);
			avisoModel = avisoDao.consultarModel(avisoId);
		} catch (Exception e) {

		}
	}

	public AvisoModel getAvisoModel() {
		return avisoModel;
	}

	public void setAvisoModel(AvisoModel avisoModel) {
		this.avisoModel = avisoModel;
	}

}