package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.AgendaPortao;
import br.com.vitral.entidade.Funcionario;
import br.com.vitral.entidade.SemanaAgendaPortao;
import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.SemanaAgendaPortaoModel;
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
			agendaPortao.setAno(agendaPortaoModel.getAno());
			List<SemanaAgendaPortao> semanas = new ArrayList<>();
			for (SemanaAgendaPortaoModel semanaModel : agendaPortaoModel.getSemanas()) {
				SemanaAgendaPortao semana = new SemanaAgendaPortao();
				semana.setPrimeiroDia(semanaModel.getPrimeiroDia());
				semana.setUltimoDia(semanaModel.getUltimoDia());
				if (semanaModel.getAbertura() != null) {
					Funcionario a = fuDao.consultar(semanaModel.getAbertura().getId());
					semana.setAbertura(a);
				}
				if (semanaModel.getFechamento() != null) {
					Funcionario f = fuDao.consultar(semanaModel.getFechamento().getId());
					semana.setFechamento(f);
				}
				semanas.add(semana);
			}
			agendaPortao.setSemanas(semanas);
			em.persist(agendaPortao);
		} else {
			agendaPortao = consultar(agendaPortaoModel.getAno());
			for (int i = 0; i < agendaPortaoModel.getSemanas().size(); i++) {
				SemanaAgendaPortaoModel semanaModel = agendaPortaoModel.getSemanas().get(i);
				if (semanaModel.getAbertura() != null) {
					Funcionario a = fuDao.consultar(semanaModel.getAbertura().getId());
					agendaPortao.getSemanas().get(i).setAbertura(a);
				}
				if (semanaModel.getFechamento() != null) {
					Funcionario f = fuDao.consultar(semanaModel.getFechamento().getId());
					agendaPortao.getSemanas().get(i).setFechamento(f);
				}
			}
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
			for (AgendaPortao agenda : lista) {
				agendaPortaoModel = new AgendaPortaoModel();
				agendaPortaoModel.setAno(agenda.getAno());
				List<SemanaAgendaPortaoModel> semanasModel = new ArrayList<>();
				SemanaAgendaPortaoModel semanaModel = null;
				for (SemanaAgendaPortao semana : agenda.getSemanas()) {
					semanaModel = new SemanaAgendaPortaoModel();
					semanaModel.setId(semana.getId());
					semanaModel.setPrimeiroDia(semana.getPrimeiroDia());
					semanaModel.setUltimoDia(semana.getUltimoDia());
					if (semana.getAbertura() != null) {
						FuncionarioModel fA = new FuncionarioModel();
						fA.setId(semana.getAbertura().getId());
						fA.setNome(semana.getAbertura().getNome());
						semanaModel.setAbertura(fA);
					}
					if (semana.getFechamento() != null) {
						FuncionarioModel fF = new FuncionarioModel();
						fF.setId(semana.getFechamento().getId());
						fF.setNome(semana.getFechamento().getNome());
						semanaModel.setFechamento(fF);
					}
					semanasModel.add(semanaModel);
				}
				agendaPortaoModel.setSemanas(semanasModel);
				agendasPortaoModel.add(agendaPortaoModel);
			}
		}
		return agendasPortaoModel;

	}

	public void remover(int ano) {
		em = Uteis.getEntityManager();
		em.remove(em.find(AgendaPortao.class, ano));
	}

	private AgendaPortao consultar(AgendaPortaoModel agendaPortaoModel) {
		return consultar(agendaPortaoModel.getAno());
	}

	public AgendaPortao consultar(int ano) {
		return Uteis.getEntityManager().find(AgendaPortao.class, ano);
	}

	public AgendaPortaoModel consultarModel(int ano) {
		AgendaPortao a = consultar(ano);
		AgendaPortaoModel model = null;
		if (a != null) {
			model = new AgendaPortaoModel();
			model.setAno(a.getAno());
			List<SemanaAgendaPortaoModel> semanasModel = new ArrayList<>();
			SemanaAgendaPortaoModel semanaModel = null;
			for (SemanaAgendaPortao semana : a.getSemanas()) {
				semanaModel = new SemanaAgendaPortaoModel();
				semanaModel.setId(semana.getId());
				semanaModel.setPrimeiroDia(semana.getPrimeiroDia());
				semanaModel.setUltimoDia(semana.getUltimoDia());
				if (semana.getAbertura() != null) {
					FuncionarioModel fA = new FuncionarioModel();
					fA.setId(semana.getAbertura().getId());
					fA.setNome(semana.getAbertura().getNome());
					semanaModel.setAbertura(fA);
				}
				if (semana.getFechamento() != null) {
					FuncionarioModel fF = new FuncionarioModel();
					fF.setId(semana.getFechamento().getId());
					fF.setNome(semana.getFechamento().getNome());
					semanaModel.setFechamento(fF);
				}
				semanasModel.add(semanaModel);
			}
			model.setSemanas(semanasModel);
		}
		return model;
	}

	public List<Integer> listarAnosDistintos() {
		return (List<Integer>) Uteis.getEntityManager().createNamedQuery("AgendaPortao.findAnosDistintos")
				.getResultList();
	}

}