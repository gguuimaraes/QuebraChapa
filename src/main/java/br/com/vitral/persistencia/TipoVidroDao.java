package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.TipoVidro;
import br.com.vitral.modelo.TipoVidroModel;
import br.com.vitral.util.Uteis;

public class TipoVidroDao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	TipoVidro tipoVidro;
	EntityManager entityManager;

	public void salvar(TipoVidroModel tipoVidroModel) {
		entityManager = Uteis.JpaEntityManager();
		if (tipoVidroModel.getId() == null) {
			tipoVidro = new TipoVidro();
			tipoVidro.setNome(tipoVidroModel.getNome());
			entityManager.persist(tipoVidro);
		} else {
			tipoVidro = entityManager.find(TipoVidro.class, tipoVidroModel.getId());
			tipoVidro.setNome(tipoVidroModel.getNome());
			entityManager.merge(tipoVidro);
		}
	}

	public List<TipoVidroModel> listar() {
		List<TipoVidroModel> tiposVidroModel = new ArrayList<TipoVidroModel>();
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("TipoVidro.findAll");
		@SuppressWarnings("unchecked")
		Collection<TipoVidro> tiposVidro = (Collection<TipoVidro>) query.getResultList();
		TipoVidroModel tipoVidroModel = null;
		for (TipoVidro t : tiposVidro) {
			tipoVidroModel = new TipoVidroModel();
			tipoVidroModel.setId(t.getId());
			tipoVidroModel.setNome(t.getNome());
			tiposVidroModel.add(tipoVidroModel);
		}
		return tiposVidroModel;
	}

	public void remover(int id) {
		entityManager = Uteis.JpaEntityManager();
		entityManager.remove(entityManager.find(TipoVidro.class, id));
	}

	public TipoVidro consultar(int id) {
		return Uteis.JpaEntityManager().find(TipoVidro.class, id);
	}
}