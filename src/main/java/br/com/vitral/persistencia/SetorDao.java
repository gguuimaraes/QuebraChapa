package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Setor;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.util.Uteis;

public class SetorDao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	Setor setor;
	EntityManager entityManager;

	public void salvar(SetorModel setorModel) {
		entityManager = Uteis.JpaEntityManager();
		if (setorModel.getId() == null) {
			setor = new Setor();
			setor.setNome(setorModel.getNome());
			entityManager.persist(setor);
		} else {
			setor = entityManager.find(Setor.class, setorModel.getId());
			setor.setNome(setorModel.getNome());
			entityManager.merge(setor);
		}
	}

	public List<SetorModel> listar() {
		List<SetorModel> setoresModel = new ArrayList<SetorModel>();
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("Setor.findAll");
		@SuppressWarnings("unchecked")
		Collection<Setor> setores = (Collection<Setor>) query.getResultList();
		SetorModel setorModel = null;
		for (Setor s : setores) {
			setorModel = new SetorModel();
			setorModel.setId(s.getId());
			setorModel.setNome(s.getNome());
			setoresModel.add(setorModel);
		}
		return setoresModel;
	}
	
	public void remover(int id) {
		entityManager = Uteis.JpaEntityManager();
		entityManager.remove(entityManager.find(Setor.class, id));
	}
	
	public Setor consultar(int id) {
		return Uteis.JpaEntityManager().find(Setor.class, id);
	}
}