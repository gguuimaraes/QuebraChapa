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

	transient EntityManager em;

	@Inject
	AreaFaturada areaFaturada;

	public void salvar(AreaFaturadaModel areaFaturadaModel) {
		em = Uteis.getEntityManager();
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
		em = Uteis.getEntityManager();
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
		em = Uteis.getEntityManager();
		em.remove(em.find(AreaFaturada.class, id));
	}

	public AreaFaturada consultar(int id) {
		return Uteis.getEntityManager().find(AreaFaturada.class, id);
	}

	public Float maiorAreaPorPeriodo(Date dataInicio, Date dataFim) {
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("AreaFaturada.findMaiorAreaPorPeriodo");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		return (Float) query.getSingleResult();
	}

	public AreaFaturadaModel consultarModel(int id) {
		return converterUnidade(consultar(id));
	}

	public AreaFaturadaModel converterUnidade(AreaFaturada areaFaturada) {
		if (areaFaturada == null)
			return null;
		AreaFaturadaModel areaFaturadaModel = new AreaFaturadaModel();
		areaFaturadaModel.setId(areaFaturada.getId());
		areaFaturadaModel.setData(areaFaturada.getData());
		areaFaturadaModel.setArea(areaFaturada.getArea());
		return areaFaturadaModel;
	}

}