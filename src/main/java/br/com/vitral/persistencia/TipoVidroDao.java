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

	transient EntityManager em;

	@Inject
	TipoVidro tipoVidro;

	public void salvar(TipoVidroModel tipoVidroModel) {
		em = Uteis.getEntityManager();
		if (tipoVidroModel.getId() == null) {
			tipoVidro = new TipoVidro();
			tipoVidro.setNome(tipoVidroModel.getNome());
			em.persist(tipoVidro);
		} else {
			tipoVidro = em.find(TipoVidro.class, tipoVidroModel.getId());
			tipoVidro.setNome(tipoVidroModel.getNome());
			em.merge(tipoVidro);
		}
	}

	public List<TipoVidroModel> listar() {
		List<TipoVidroModel> tiposVidroModel = new ArrayList<>();
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("TipoVidro.findAll");
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
		em = Uteis.getEntityManager();
		em.remove(em.find(TipoVidro.class, id));
	}

	public TipoVidro consultar(int id) {
		return Uteis.getEntityManager().find(TipoVidro.class, id);
	}
}