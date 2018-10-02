package br.com.vitral.controlador;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;

import br.com.vitral.entidade.Usuario;
import br.com.vitral.modelo.UsuarioModel;
import br.com.vitral.persistencia.UsuarioDao;
import br.com.vitral.util.Uteis;

@Named(value = "alterarSenhaController")
@SessionScoped
public class AlterarSenhaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -522096478205423143L;

	private String senhaAntiga;
	private String nova;

	@Inject
	private UsuarioDao usuarioDao;

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getNova() {
		return nova;
	}

	public void setNova(String nova) {
		this.nova = nova;
	}

	public void alterar() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UsuarioModel usuarioModel = (UsuarioModel) facesContext.getExternalContext().getSessionMap()
				.get("usuarioAutenticado");
		usuarioModel.setSenha(senhaAntiga);
		Usuario usuario = usuarioDao.validaUsuario(usuarioModel);
		if (usuario != null) {
			usuarioModel.setSenha(nova);
			usuarioDao.salvar(usuarioModel);
			usuarioModel.setSenha(null);
			Uteis.message("Senha alterada com sucesso!");
			PrimeFaces.current().executeScript("PF('poll').start()");	
		} else {
			Uteis.message("Senha antiga inválida");
		}
	}

	public void redirecionar() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
	}
}