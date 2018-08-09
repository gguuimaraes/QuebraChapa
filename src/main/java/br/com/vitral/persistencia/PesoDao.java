package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Peso;
import br.com.vitral.modelo.PesoModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.util.Uteis;

public class PesoDao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	Peso peso;
	EntityManager entityManager;

	@Inject
	SetorDao setorDao;
	@Inject
	FuncionarioDao funcionarioDao;

	public void salvar(PesoModel pesoModel) {
		entityManager = Uteis.JpaEntityManager();
		if (pesoModel.getId() == null) {
			peso = new Peso();
			peso.setData(pesoModel.getData());
			peso.setFuncionario(funcionarioDao.consultar(pesoModel.getFuncionario().getId()));
			peso.setSetor(setorDao.consultar(pesoModel.getSetor().getId()));
			peso.setPeso(pesoModel.getPeso());
			entityManager.persist(peso);
		} else {
			peso = entityManager.find(Peso.class, pesoModel.getId());
			peso.setData(pesoModel.getData());
			peso.setFuncionario(funcionarioDao.consultar(pesoModel.getFuncionario().getId()));
			peso.setSetor(setorDao.consultar(pesoModel.getSetor().getId()));
			peso.setPeso(pesoModel.getPeso());
			entityManager.merge(peso);
		}
	}

	public List<PesoModel> listar() {
		List<PesoModel> pesosModel = new ArrayList<PesoModel>();
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("Peso.findAll");
		@SuppressWarnings("unchecked")
		Collection<Peso> pesos = (Collection<Peso>) query.getResultList();
		PesoModel pesoModel = null;
		for (Peso p : pesos) {
			pesoModel = new PesoModel();
			pesoModel.setId(p.getId());
			pesoModel.setData(p.getData());
			FuncionarioModel funcionarioModel = new FuncionarioModel();
			funcionarioModel.setId(p.getFuncionario().getId());
			funcionarioModel.setNome(p.getFuncionario().getNome());
			pesoModel.setFuncionario(funcionarioModel);
			SetorModel setorModel = new SetorModel();
			setorModel.setId(p.getSetor().getId());
			setorModel.setNome(p.getSetor().getNome());
			pesoModel.setSetor(setorModel);
			pesoModel.setPeso(p.getPeso());
			pesosModel.add(pesoModel);
		}
		return pesosModel;
	}

	public void remover(int id) {
		entityManager = Uteis.JpaEntityManager();
		entityManager.remove(entityManager.find(Peso.class, id));
	}

	public Peso consultar(int id) {
		return Uteis.JpaEntityManager().find(Peso.class, id);
	}

}