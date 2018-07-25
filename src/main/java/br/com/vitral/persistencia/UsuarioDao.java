package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Usuario;
import br.com.vitral.modelo.UsuarioModel;
import br.com.vitral.util.Uteis;

public class UsuarioDao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	Usuario usuario;
	EntityManager entityManager;

	public Usuario validaUsuario(UsuarioModel usuarioModel) {
		try {
			Query query = Uteis.JpaEntityManager().createNamedQuery("Usuario.findUser");
			query.setParameter("nome", usuarioModel.getNome());
			query.setParameter("senha", usuarioModel.getSenha());
			return (Usuario) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public void salvar(UsuarioModel usuarioModel) {
		entityManager = Uteis.JpaEntityManager();
		if (usuarioModel.getId() == null) {
			usuario = new Usuario();
			usuario.setNome(usuarioModel.getNome());
			usuario.setSenha(usuarioModel.getSenha());
			usuario.setTipo(usuarioModel.getTipo());
			entityManager.persist(usuario);
		} else {
			usuario = entityManager.find(Usuario.class, usuarioModel.getId());
			usuario.setNome(usuarioModel.getNome());
			usuario.setSenha(usuarioModel.getSenha());
			usuario.setTipo(usuarioModel.getTipo());
			entityManager.merge(usuario);
		}
	}

	public List<UsuarioModel> listar() {
		List<UsuarioModel> usuariosModel = new ArrayList<UsuarioModel>();
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("Usuario.findAll");
		@SuppressWarnings("unchecked")
		Collection<Usuario> usuarios = (Collection<Usuario>) query.getResultList();
		UsuarioModel usuarioModel = null;
		for (Usuario u : usuarios) {
			usuarioModel = new UsuarioModel();
			usuarioModel.setId(u.getId());
			usuarioModel.setNome(u.getNome());
			usuarioModel.setSenha(u.getSenha());
			usuarioModel.setTipo(u.getTipo());
			usuariosModel.add(usuarioModel);
		}
		return usuariosModel;
	}
	
	public void remover(int id) {
		entityManager = Uteis.JpaEntityManager();
		entityManager.remove(entityManager.find(Usuario.class, id));
	}
}