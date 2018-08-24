package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.AgendaPortao;
import br.com.vitral.entidade.AgendaPortaoId;
import br.com.vitral.entidade.DiaAgendaPortao;
import br.com.vitral.entidade.Funcionario;
import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.DiaAgendaPortaoModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.util.Uteis;

public class AgendaPortaoDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	AgendaPortao agendaPortao;

	@Inject
	FuncionarioDao fuDao;

	public void salvar(AgendaPortaoModel agendaPortaoModel) {
		em = Uteis.getEntityManager();
		if (consultar(agendaPortaoModel) == null) {
			agendaPortao = new AgendaPortao();
			agendaPortao.setId(new AgendaPortaoId(agendaPortaoModel.getMes(), agendaPortaoModel.getAno()));
			List<DiaAgendaPortao> dias = new ArrayList<>();
			for (DiaAgendaPortaoModel dModel : agendaPortaoModel.getDias()) {
				DiaAgendaPortao d = new DiaAgendaPortao();
				d.setData(dModel.getData());
				Funcionario a = fuDao.consultar(dModel.getAbertura().getId());
				d.setAbertura(a);
				Funcionario f = fuDao.consultar(dModel.getFechamento().getId());
				d.setFechamento(f);
				d.setFeriado(dModel.isFeriado());
				dias.add(d);
			}
			agendaPortao.setDias(dias);
			em.persist(agendaPortao);
		} else {
			agendaPortao = em.find(AgendaPortao.class,
					new AgendaPortaoId(agendaPortaoModel.getMes(), agendaPortaoModel.getAno()));
			List<DiaAgendaPortao> dias = new ArrayList<>();
			for (DiaAgendaPortaoModel dModel : agendaPortaoModel.getDias()) {
				DiaAgendaPortao d = new DiaAgendaPortao();
				d.setData(dModel.getData());
				Funcionario a = fuDao.consultar(dModel.getAbertura().getId());
				d.setAbertura(a);
				Funcionario f = fuDao.consultar(dModel.getFechamento().getId());
				d.setFechamento(f);
				d.setFeriado(dModel.isFeriado());
				dias.add(d);
			}
			agendaPortao.setDias(dias);
			em.merge(agendaPortao);
		}
	}

	public List<AgendaPortaoModel> listar() {
		Query query = Uteis.getEntityManager().createNamedQuery("AgendaPortao.findAll");
		return converterLista(query.getResultList());
	}

	private List<AgendaPortaoModel> converterLista(Collection<AgendaPortao> lista) {
		List<AgendaPortaoModel> agendasPortaoModel = new ArrayList<>();
		AgendaPortaoModel agendaPortaoModel = null;
		if (lista != null) {
			for (AgendaPortao a : lista) {
				agendaPortaoModel = new AgendaPortaoModel();
				agendaPortaoModel.setMes(a.getId().getMes());
				agendaPortaoModel.setAno(a.getId().getAno());
				List<DiaAgendaPortaoModel> diasModel = new ArrayList<>();
				DiaAgendaPortaoModel dModel = null;
				for (DiaAgendaPortao d : a.getDias()) {
					dModel = new DiaAgendaPortaoModel();
					dModel.setId(d.getId());
					dModel.setData(d.getData());
					FuncionarioModel fA = new FuncionarioModel();
					fA.setId(d.getAbertura().getId());
					fA.setNome(d.getAbertura().getNome());
					dModel.setAbertura(fA);
					FuncionarioModel fF = new FuncionarioModel();
					fF.setId(d.getFechamento().getId());
					fF.setNome(d.getFechamento().getNome());
					dModel.setAbertura(fF);
					dModel.setFeriado(d.isFeriado());
					diasModel.add(dModel);
				}
				agendaPortaoModel.setDias(diasModel);
				agendasPortaoModel.add(agendaPortaoModel);
			}
		}
		return agendasPortaoModel;
	}

	public void remover(int mes, int ano) {
		em = Uteis.getEntityManager();
		em.remove(em.find(AgendaPortao.class, new AgendaPortaoId(mes, ano)));
	}

	private AgendaPortao consultar(AgendaPortaoModel agendaPortaoModel) {
		return consultar(agendaPortaoModel.getMes(), agendaPortaoModel.getAno());
	}

	public AgendaPortao consultar(int mes, int ano) {
		return Uteis.getEntityManager().find(AgendaPortao.class, new AgendaPortaoId(mes, ano));
	}

	public List<Integer> listarAnosDistintos() {
		return (List<Integer>) Uteis.getEntityManager().createNamedQuery("AgendaPortao.findAnosDistintos")
				.getResultList();
	}

}