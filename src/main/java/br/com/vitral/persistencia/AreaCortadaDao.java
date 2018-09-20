package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.AreaCortada;
import br.com.vitral.entidade.Setor;
import br.com.vitral.modelo.AreaCortadaModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.util.Uteis;

public class AreaCortadaDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	AreaCortada areaCortada;

	@Inject
	SetorDao setorDao;

	@Inject
	FuncionarioDao funcionarioDao;

	public void salvar(AreaCortadaModel areaCortadaModel) {
		em = Uteis.getEntityManager();
		if (areaCortadaModel.getId() == 0) {
			areaCortada = new AreaCortada();
			areaCortada.setData(areaCortadaModel.getData());
			areaCortada.setFuncionario(funcionarioDao.consultar(areaCortadaModel.getFuncionario().getId()));
			areaCortada.setSetor(setorDao.consultar(areaCortadaModel.getSetor().getId()));
			areaCortada.setArea(areaCortadaModel.getArea());
			em.persist(areaCortada);
		} else {
			areaCortada = em.find(AreaCortada.class, areaCortadaModel.getId());
			areaCortada.setData(areaCortadaModel.getData());
			areaCortada.setFuncionario(funcionarioDao.consultar(areaCortadaModel.getFuncionario().getId()));
			areaCortada.setSetor(setorDao.consultar(areaCortadaModel.getSetor().getId()));
			areaCortada.setArea(areaCortadaModel.getArea());
			em.merge(areaCortada);
		}
	}

	public List<AreaCortadaModel> listar() {
		Query query = Uteis.getEntityManager().createNamedQuery("AreaCortada.findAll");
		return converterLista(query.getResultList());
	}

	public List<AreaCortadaModel> listar(Date dia) {
		Query query = Uteis.getEntityManager().createNamedQuery("AreaCortada.findAreasCortadasDoDia");
		query.setParameter("data", dia);
		return converterLista(query.getResultList());
	}

	public List<AreaCortadaModel> listar(SetorModel setor, Date dia) {
		Query query = Uteis.getEntityManager().createNamedQuery("AreaCortada.findAreasPorSetorDia");
		query.setParameter("data", dia);
		query.setParameter("setorId", setor.getId());
		return converterLista(query.getResultList());
	}

	public void remover(int id) {
		em = Uteis.getEntityManager();
		em.remove(em.find(AreaCortada.class, id));
	}

	public AreaCortada consultar(int id) {
		return Uteis.getEntityManager().find(AreaCortada.class, id);
	}

	private List<AreaCortadaModel> converterLista(Collection<AreaCortada> lista) {
		List<AreaCortadaModel> areasModel = new ArrayList<>();
		AreaCortadaModel areaModel = null;
		if (lista != null) {
			for (AreaCortada a : lista) {
				areaModel = new AreaCortadaModel();
				areaModel.setId(a.getId());
				areaModel.setData(a.getData());
				FuncionarioModel funcionarioModel = new FuncionarioModel();
				funcionarioModel.setId(a.getFuncionario().getId());
				funcionarioModel.setNome(a.getFuncionario().getNome());
				areaModel.setFuncionario(funcionarioModel);
				SetorModel setorModel = new SetorModel();
				setorModel.setId(a.getSetor().getId());
				setorModel.setNome(a.getSetor().getNome());
				areaModel.setSetor(setorModel);
				areaModel.setArea(a.getArea());
				areasModel.add(areaModel);
			}
		}

		return areasModel;
	}

	public Double consultarAreaTotal(SetorModel setor, Date dia) {
		Double areaTotal = null;
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("AreaCortada.findAreaTotalSetorDia");
		query.setParameter("setorId", setor.getId());
		query.setParameter("data", dia);
		areaTotal = (Double) query.getSingleResult();
		return areaTotal;
	}

	public AreaCortadaModel consultarModel(int id) {
		return converterUnidade(consultar(id));
	}

	private AreaCortadaModel converterUnidade(AreaCortada areaCortada) {
		if (areaCortada == null)
			return null;
		AreaCortadaModel areaCortadaModel = new AreaCortadaModel();
		areaCortadaModel.setId(areaCortada.getId());
		areaCortadaModel.setData(areaCortada.getData());
		FuncionarioModel funcionarioModel = new FuncionarioModel();
		funcionarioModel.setId(areaCortada.getFuncionario().getId());
		funcionarioModel.setNome(areaCortada.getFuncionario().getNome());
		areaCortadaModel.setFuncionario(funcionarioModel);
		SetorModel setorModel = new SetorModel();
		setorModel.setId(areaCortada.getSetor().getId());
		setorModel.setNome(areaCortada.getSetor().getNome());
		areaCortadaModel.setSetor(setorModel);
		areaCortadaModel.setArea(areaCortada.getArea());
		return areaCortadaModel;
	}

}