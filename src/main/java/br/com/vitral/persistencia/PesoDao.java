package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Peso;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.PesoModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.util.Uteis;

public class PesoDao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	Peso peso;
	EntityManager em;

	@Inject
	SetorDao setorDao;
	@Inject
	FuncionarioDao funcionarioDao;

	public void salvar(PesoModel pesoModel) {
		em = Uteis.JpaEntityManager();
		if (pesoModel.getId() == 0) {
			peso = new Peso();
			peso.setData(pesoModel.getData());
			peso.setFuncionario(funcionarioDao.consultar(pesoModel.getFuncionario().getId()));
			peso.setSetor(setorDao.consultar(pesoModel.getSetor().getId()));
			peso.setPeso(pesoModel.getPeso());
			em.persist(peso);
		} else {
			peso = em.find(Peso.class, pesoModel.getId());
			peso.setData(pesoModel.getData());
			peso.setFuncionario(funcionarioDao.consultar(pesoModel.getFuncionario().getId()));
			peso.setSetor(setorDao.consultar(pesoModel.getSetor().getId()));
			peso.setPeso(pesoModel.getPeso());
			em.merge(peso);
		}
	}

	public List<PesoModel> listar() {
		List<PesoModel> pesosModel = new ArrayList<PesoModel>();
		em = Uteis.JpaEntityManager();
		Query query = em.createNamedQuery("Peso.findAll");
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
		em = Uteis.JpaEntityManager();
		em.remove(em.find(Peso.class, id));
	}

	public Peso consultar(PesoModel pesoModel) {
		return Uteis.JpaEntityManager().find(Peso.class, pesoModel.getId());
	}

	public List<PesoModel> listarPesosDoDia(Date dia) {
		List<PesoModel> pesosModel = new ArrayList<PesoModel>();
		em = Uteis.JpaEntityManager();
		Query query = em.createNamedQuery("Peso.findPesosDoDia");
		query.setParameter("data", dia);
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

}