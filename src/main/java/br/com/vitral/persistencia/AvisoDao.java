package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Aviso;
import br.com.vitral.modelo.AvisoModel;
import br.com.vitral.util.Uteis;

public class AvisoDao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	Aviso aviso;

	@Inject
	FuncionarioDao funcionarioDao;

	transient EntityManager em;

	public void salvar(AvisoModel avisoModel) {
		em = Uteis.getEntityManager();
		if (avisoModel.getId() == null) {
			aviso = new Aviso();
			aviso.setDescricao(avisoModel.getDescricao());
			aviso.setCorpo(avisoModel.getCorpo());
			em.persist(aviso);
		} else {
			aviso = em.find(Aviso.class, avisoModel.getId());
			aviso.setDescricao(avisoModel.getDescricao());
			aviso.setCorpo(avisoModel.getCorpo());
			em.merge(aviso);
		}
	}

	public List<AvisoModel> listar() {
		List<AvisoModel> avisosModel = new ArrayList<>();
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Aviso.findAll");
		@SuppressWarnings("unchecked")
		Collection<Aviso> avisos = (Collection<Aviso>) query.getResultList();
		for (Aviso a : avisos) {
			avisosModel.add(converterUnidade(a));
		}
		return avisosModel;
	}


	public void remover(int id) {
		Uteis.getEntityManager().remove(Uteis.getEntityManager().find(Aviso.class, id));
	}

	public Aviso consultar(int id) {
		return Uteis.getEntityManager().find(Aviso.class, id);
	}

	public AvisoModel consultarModel(int id) {
		return converterUnidade(consultar(id));
	}

	private AvisoModel converterUnidade(Aviso aviso) {
		if (aviso == null)
			return null;
		AvisoModel avisoModel = new AvisoModel();
		avisoModel.setId(aviso.getId());
		avisoModel.setDescricao(aviso.getDescricao());
		avisoModel.setCorpo(aviso.getCorpo());
		return avisoModel;
	}
}