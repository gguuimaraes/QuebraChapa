package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.vitral.entidade.Setor;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.util.Uteis;

public class SetorDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	Setor setor;

	public void salvar(SetorModel setorModel) {
		em = Uteis.getEntityManager();
		if (setorModel.getId() == null) {
			setor = new Setor();
			setor.setNome(setorModel.getNome());
			setor.setCor(setorModel.getCor());
			em.persist(setor);
		} else {
			setor = em.find(Setor.class, setorModel.getId());
			setor.setNome(setorModel.getNome());
			setor.setCor(setorModel.getCor());
			em.merge(setor);
		}
	}

	public List<SetorModel> listar() {
		List<SetorModel> setoresModel = new ArrayList<>();
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Setor.findAll");
		@SuppressWarnings("unchecked")
		Collection<Setor> setores = (Collection<Setor>) query.getResultList();
		SetorModel setorModel = null;
		for (Setor s : setores) {
			setorModel = new SetorModel();
			setorModel.setId(s.getId());
			setorModel.setNome(s.getNome());
			setorModel.setCor(s.getCor());
			setoresModel.add(setorModel);
		}
		return setoresModel;
	}

	public void remover(int id) {
		em = Uteis.getEntityManager();
		em.remove(consultar(id));
	}

	public Setor consultar(int id) {
		return Uteis.getEntityManager().find(Setor.class, id);
	}

	public Setor consultarPeloNome(String nome) {
		try {
			return (Setor) Uteis.getEntityManager().createNamedQuery("Setor.findPeloNome").setParameter("nome", nome)
			.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}