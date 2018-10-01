package br.com.vitral.persistencia;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Usuario;
import br.com.vitral.modelo.UsuarioModel;
import br.com.vitral.util.PasswordEncryptionService;
import br.com.vitral.util.Uteis;

public class UsuarioDao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5531429780015441983L;
	@Inject
	Usuario usuario;
	transient EntityManager em;

	@Inject
	PasswordEncryptionService pes;

	public Usuario validaUsuario(UsuarioModel usuarioModel) {
		try {
			Query query = Uteis.getEntityManager().createNamedQuery("Usuario.findPeloNome");
			query.setParameter("nome", usuarioModel.getNome());
			Usuario usuario = (Usuario) query.getSingleResult();
			return pes.authenticate(usuarioModel.getSenha(), usuario.getSenha(), usuarioModel.getNome().getBytes())
					? usuario
					: null;
		} catch (Exception e) {
			return null;
		}
	}

	public void salvar(UsuarioModel usuarioModel) {
		em = Uteis.getEntityManager();
		try {
			if (usuarioModel.getId() == null) {
				usuario = new Usuario();
				usuario.setNome(usuarioModel.getNome());
				usuario.setSenha(pes.getEncryptedPassword(usuarioModel.getSenha(), usuarioModel.getNome().getBytes()));
				usuario.setTipo(usuarioModel.getTipo());
				em.persist(usuario);
			} else {
				usuario = em.find(Usuario.class, usuarioModel.getId());
				usuario.setNome(usuarioModel.getNome());
				if (usuarioModel.getSenha() != null)
					usuario.setSenha(
							pes.getEncryptedPassword(usuarioModel.getSenha(), usuarioModel.getNome().getBytes()));
				usuario.setTipo(usuarioModel.getTipo());
				em.merge(usuario);
			}
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

		}
	}

	public List<UsuarioModel> listar() {
		Query query = Uteis.getEntityManager().createNamedQuery("Usuario.findAll");
		return converterLista(query.getResultList());
	}

	public void remover(int id) {
		em = Uteis.getEntityManager();
		em.remove(em.find(Usuario.class, id));
	}

	public Usuario consultar(int id) {
		return Uteis.getEntityManager().find(Usuario.class, id);
	}

	public UsuarioModel consultarModel(int id) {
		return converterUnidade(consultar(id));
	}

	private UsuarioModel converterUnidade(Usuario usuario) {
		if (usuario == null)
			return null;
		UsuarioModel usuarioModel = new UsuarioModel();
		usuarioModel.setId(usuario.getId());
		usuarioModel.setNome(usuario.getNome());
		// usuarioModel.setSenha(usuario.getSenha());
		usuarioModel.setTipo(usuario.getTipo());
		return usuarioModel;
	}

	private List<UsuarioModel> converterLista(Collection<Usuario> lista) {
		List<UsuarioModel> usuariosModel = new ArrayList<>();
		UsuarioModel usuarioModel = null;
		for (Usuario u : lista) {
			usuarioModel = new UsuarioModel();
			usuarioModel.setId(u.getId());
			usuarioModel.setNome(u.getNome());
			// usuarioModel.setSenha(u.getSenha());
			usuarioModel.setTipo(u.getTipo());
			usuariosModel.add(usuarioModel);
		}
		return usuariosModel;
	}
}