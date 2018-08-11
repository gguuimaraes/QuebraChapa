package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.AreaFaturada;
import br.com.vitral.modelo.AreaFaturadaModel;
import br.com.vitral.util.Uteis;

public class AreaFaturadaDao implements Serializable {

	private static final long serialVersionUID = 1L;

	EntityManager em;

	@Inject
	AreaFaturada areaFaturada;

	public void salvar(AreaFaturadaModel areaFaturadaModel) {
		em = Uteis.JpaEntityManager();
		if (areaFaturadaModel.getId() == 0) {
			areaFaturada = new AreaFaturada();
			areaFaturada.setData(areaFaturadaModel.getData());
			areaFaturada.setArea(areaFaturadaModel.getArea());
			em.persist(areaFaturada);
		} else {
			areaFaturada = em.find(AreaFaturada.class, areaFaturadaModel.getId());
			areaFaturada.setData(areaFaturadaModel.getData());
			areaFaturada.setArea(areaFaturadaModel.getArea());
			em.merge(areaFaturada);
		}
	}

	public List<AreaFaturadaModel> listar() {
		List<AreaFaturadaModel> areasFaturadasModel = new ArrayList<>();
		em = Uteis.JpaEntityManager();
		Query query = em.createNamedQuery("AreaFaturada.findAll");
		@SuppressWarnings("unchecked")
		Collection<AreaFaturada> areasFaturadas = (Collection<AreaFaturada>) query.getResultList();
		AreaFaturadaModel areaFaturadaModel = null;
		for (AreaFaturada a : areasFaturadas) {
			areaFaturadaModel = new AreaFaturadaModel();
			areaFaturadaModel.setId(a.getId());
			areaFaturadaModel.setData(a.getData());
			areaFaturadaModel.setArea(a.getArea());
			areasFaturadasModel.add(areaFaturadaModel);
		}
		return areasFaturadasModel;
	}

	public void remover(int id) {
		em = Uteis.JpaEntityManager();
		em.remove(em.find(AreaFaturada.class, id));
	}

	public AreaFaturada consultar(int id) {
		return Uteis.JpaEntityManager().find(AreaFaturada.class, id);
	}

	public Float maiorAreaPorPeriodo(Date dataInicio, Date dataFim) {
		em = Uteis.JpaEntityManager();
		Query query = em.createNamedQuery("AreaFaturada.findMaiorAreaPorPeriodo");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		return (Float) query.getSingleResult();
	}

}