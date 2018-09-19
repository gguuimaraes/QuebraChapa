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
import br.com.vitral.entidade.Setor;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.PesoModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.util.Uteis;

public class PesoDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	Peso peso;

	@Inject
	SetorDao setorDao;

	@Inject
	FuncionarioDao funcionarioDao;

	public void salvar(PesoModel pesoModel) {
		em = Uteis.getEntityManager();
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
		Query query = Uteis.getEntityManager().createNamedQuery("Peso.findAll");
		return converterLista(query.getResultList());
	}

	public List<PesoModel> listar(Date dia) {
		Query query = Uteis.getEntityManager().createNamedQuery("Peso.findPesosDoDia");
		query.setParameter("data", dia);
		return converterLista(query.getResultList());
	}

	public List<PesoModel> listar(Setor setor, Date dia) {
		Query query = Uteis.getEntityManager().createNamedQuery("Peso.findPesosPorSetorDia");
		query.setParameter("data", dia);
		query.setParameter("setorId", setor.getId());
		return converterLista(query.getResultList());
	}

	private List<PesoModel> converterLista(Collection<Peso> lista) {
		List<PesoModel> pesosModel = new ArrayList<>();
		PesoModel pesoModel = null;
		if (lista != null) {
			for (Peso p : lista) {
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
		}
		return pesosModel;
	}

	public void remover(int id) {
		em = Uteis.getEntityManager();
		em.remove(em.find(Peso.class, id));
	}

	public Peso consultar(int id) {
		return Uteis.getEntityManager().find(Peso.class, id);
	}

	public Peso consultar(PesoModel pesoModel) {
		return consultar(pesoModel.getId());
	}

	public Double consultarPesoTotal(Setor setor, Date dia) {
		Double pesoTotal = null;
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Peso.findPesoTotalSetorDia");
		query.setParameter("setorId", setor.getId());
		query.setParameter("data", dia);
		pesoTotal = (Double) query.getSingleResult();
		return pesoTotal;
	}

	public PesoModel consultarModel(int id) {
		return converterUnidade(consultar(id));
	}

	private PesoModel converterUnidade(Peso peso) {
		if (peso == null)
			return null;
		PesoModel pesoModel = new PesoModel();
		pesoModel.setId(peso.getId());
		pesoModel.setData(peso.getData());
		FuncionarioModel funcionarioModel = new FuncionarioModel();
		funcionarioModel.setId(peso.getFuncionario().getId());
		funcionarioModel.setNome(peso.getFuncionario().getNome());
		pesoModel.setFuncionario(funcionarioModel);
		SetorModel setorModel = new SetorModel();
		setorModel.setId(peso.getSetor().getId());
		setorModel.setNome(peso.getSetor().getNome());
		pesoModel.setSetor(setorModel);
		pesoModel.setPeso(peso.getPeso());
		return pesoModel;
	}
	
	public Double consultarPesoTotal(Date dia) {
		Double pesoTotal = null;
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Peso.findPesoTotalDia");
		query.setParameter("data", dia);
		pesoTotal = (Double) query.getSingleResult();
		return pesoTotal;
	}

}