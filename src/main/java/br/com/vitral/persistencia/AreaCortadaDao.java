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
import br.com.vitral.modelo.AreaCortadaModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.util.Uteis;

public class AreaCortadaDao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	AreaCortada areaCortada;
	EntityManager em;

	@Inject
	SetorDao setorDao;
	@Inject
	FuncionarioDao funcionarioDao;

	public void salvar(AreaCortadaModel areaCortadaModel) {
		em = Uteis.JpaEntityManager();
		if (areaCortadaModel.getId() == null) {
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
		List<AreaCortadaModel> areasCortadasModel = new ArrayList<AreaCortadaModel>();
		em = Uteis.JpaEntityManager();
		Query query = em.createNamedQuery("AreaCortada.findAll");
		@SuppressWarnings("unchecked")
		Collection<AreaCortada> areasCortadas = (Collection<AreaCortada>) query.getResultList();
		AreaCortadaModel areaCortadaModel = null;
		for (AreaCortada a : areasCortadas) {
			areaCortadaModel = new AreaCortadaModel();
			areaCortadaModel.setId(a.getId());
			areaCortadaModel.setData(a.getData());
			FuncionarioModel funcionarioModel = new FuncionarioModel();
			funcionarioModel.setId(a.getFuncionario().getId());
			funcionarioModel.setNome(a.getFuncionario().getNome());
			areaCortadaModel.setFuncionario(funcionarioModel);
			SetorModel setorModel = new SetorModel();
			setorModel.setId(a.getSetor().getId());
			setorModel.setNome(a.getSetor().getNome());
			areaCortadaModel.setSetor(setorModel);
			areaCortadaModel.setArea(a.getArea());
			areasCortadasModel.add(areaCortadaModel);
		}
		return areasCortadasModel;
	}

	public void remover(int id) {
		em = Uteis.JpaEntityManager();
		em.remove(em.find(AreaCortada.class, id));
	}

	public AreaCortada consultar(int id) {
		return Uteis.JpaEntityManager().find(AreaCortada.class, id);
	}
	
	public List<AreaCortadaModel> listarAreasDoDia(Date dia) {
		List<AreaCortadaModel> areasModel = new ArrayList<AreaCortadaModel>();
		em = Uteis.JpaEntityManager();
		Query query = em.createNamedQuery("AreaCortada.findAreasCortadasDoDia");
		query.setParameter("data", dia);
		@SuppressWarnings("unchecked")
		Collection<AreaCortadaModel> areas = (Collection<AreaCortadaModel>) query.getResultList();
		AreaCortadaModel areaModel = null;
		for (AreaCortadaModel a : areas) {
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
		return areasModel;
	}

}