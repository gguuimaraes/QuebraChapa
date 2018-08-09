package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
	EntityManager entityManager;

	@Inject
	SetorDao setorDao;
	@Inject
	FuncionarioDao funcionarioDao;

	public void salvar(AreaCortadaModel areaCortadaModel) {
		entityManager = Uteis.JpaEntityManager();
		if (areaCortadaModel.getId() == null) {
			areaCortada = new AreaCortada();
			areaCortada.setData(areaCortadaModel.getData());
			areaCortada.setFuncionario(funcionarioDao.consultar(areaCortadaModel.getFuncionario().getId()));
			areaCortada.setSetor(setorDao.consultar(areaCortadaModel.getSetor().getId()));
			areaCortada.setArea(areaCortadaModel.getArea());
			entityManager.persist(areaCortada);
		} else {
			areaCortada = entityManager.find(AreaCortada.class, areaCortadaModel.getId());
			areaCortada.setData(areaCortadaModel.getData());
			areaCortada.setFuncionario(funcionarioDao.consultar(areaCortadaModel.getFuncionario().getId()));
			areaCortada.setSetor(setorDao.consultar(areaCortadaModel.getSetor().getId()));
			areaCortada.setArea(areaCortadaModel.getArea());
			entityManager.merge(areaCortada);
		}
	}

	public List<AreaCortadaModel> listar() {
		List<AreaCortadaModel> areasCortadasModel = new ArrayList<AreaCortadaModel>();
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("AreaCortada.findAll");
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
		entityManager = Uteis.JpaEntityManager();
		entityManager.remove(entityManager.find(AreaCortada.class, id));
	}

	public AreaCortada consultar(int id) {
		return Uteis.JpaEntityManager().find(AreaCortada.class, id);
	}

}