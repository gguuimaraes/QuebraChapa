package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.AgendaPortao;
import br.com.vitral.entidade.DiaAgendaPortao;
import br.com.vitral.entidade.Funcionario;
import br.com.vitral.modelo.DiaAgendaPortaoModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.util.Uteis;

public class DiaAgendaPortaoDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	DiaAgendaPortao diaAgendaPortao;

	@Inject
	FuncionarioDao fuDao;
	
	public void salvar(DiaAgendaPortaoModel diaAgendaPortaoModel) {
		em = Uteis.getEntityManager();
		if (diaAgendaPortaoModel.getId() == 0) {
			diaAgendaPortao = new DiaAgendaPortao();
			diaAgendaPortao.setData(diaAgendaPortaoModel.getData());
			Funcionario a = fuDao.consultar(diaAgendaPortaoModel.getAbertura().getId());		
			diaAgendaPortao.setAbertura(a);
			Funcionario f = fuDao.consultar(diaAgendaPortaoModel.getFechamento().getId());
			diaAgendaPortao.setFechamento(f);
			diaAgendaPortao.setFeriado(diaAgendaPortaoModel.isFeriado());
			em.persist(diaAgendaPortao);
		} else {
			diaAgendaPortao = em.find(DiaAgendaPortao.class, diaAgendaPortaoModel.getId());
			diaAgendaPortao.setData(diaAgendaPortaoModel.getData());
			Funcionario a = fuDao.consultar(diaAgendaPortaoModel.getAbertura().getId());		
			diaAgendaPortao.setAbertura(a);
			Funcionario f = fuDao.consultar(diaAgendaPortaoModel.getFechamento().getId());
			diaAgendaPortao.setFechamento(f);
			diaAgendaPortao.setFeriado(diaAgendaPortaoModel.isFeriado());
			em.merge(diaAgendaPortao);
		}
	}

	public List<DiaAgendaPortaoModel> listar(AgendaPortao agendaPortao) {
		Query query = Uteis.getEntityManager().createNamedQuery("DiaAgendaPortao.findPorMesAno");
		query.setParameter("mes", agendaPortao.getMes());
		query.setParameter("ano", agendaPortao.getAno());
		return converterLista(query.getResultList());
	}

	private List<DiaAgendaPortaoModel> converterLista(Collection<DiaAgendaPortao> lista) {
		List<DiaAgendaPortaoModel> diasAgendaPortaoModel = new ArrayList<>();
		DiaAgendaPortaoModel diaAgendaPortaoModel = null;
		if (lista != null) {
			for (DiaAgendaPortao d : lista) {
				diaAgendaPortaoModel = new DiaAgendaPortaoModel();
				diaAgendaPortaoModel.setId(d.getId());
				diaAgendaPortaoModel.setData(d.getData());
				FuncionarioModel a = new FuncionarioModel();
				a.setId(d.getAbertura().getId());
				a.setNome(d.getAbertura().getNome());
				diaAgendaPortaoModel.setAbertura(a);
				FuncionarioModel f = new FuncionarioModel();
				f.setId(d.getFechamento().getId());
				f.setNome(d.getFechamento().getNome());
				diaAgendaPortaoModel.setAbertura(f);
				diaAgendaPortaoModel.setFeriado(d.isFeriado());
				diasAgendaPortaoModel.add(diaAgendaPortaoModel);
			}
		}
		return diasAgendaPortaoModel;
	}

	public void remover(int id) {
		em = Uteis.getEntityManager();
		em.remove(em.find(DiaAgendaPortao.class, id));
	}

	public DiaAgendaPortao consultar(DiaAgendaPortaoModel diaAgendaPortaoModel) {
		return Uteis.getEntityManager().find(DiaAgendaPortao.class, diaAgendaPortaoModel.getId());
	}

}