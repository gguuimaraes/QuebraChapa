package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.AgendaCalcada;
import br.com.vitral.entidade.Funcionario;
import br.com.vitral.entidade.DiaAgendaCalcada;
import br.com.vitral.modelo.AgendaCalcadaModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.DiaAgendaCalcadaModel;
import br.com.vitral.util.Uteis;

public class AgendaCalcadaDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	AgendaCalcada agendaCalcada;

	@Inject
	FuncionarioDao fuDao;

	public void salvar(AgendaCalcadaModel agendaCalcadaModel) {
		em = Uteis.getEntityManager();
		if (consultar(agendaCalcadaModel) == null) {
			agendaCalcada = new AgendaCalcada();
			agendaCalcada.setAno(agendaCalcadaModel.getAno());
			List<DiaAgendaCalcada> dias = new ArrayList<>();
			for (DiaAgendaCalcadaModel diaModel : agendaCalcadaModel.getDias()) {
				DiaAgendaCalcada dia = new DiaAgendaCalcada();
				dia.setData(diaModel.getData());
				if (diaModel.getFuncionario() != null) {
					Funcionario f = fuDao.consultar(diaModel.getFuncionario().getId());
					dia.setFuncionario(f);
				}
				dia.setFeriado(diaModel.isFeriado());
				dias.add(dia);
			}
			agendaCalcada.setDias(dias);
			em.persist(agendaCalcada);
		} else {
			agendaCalcada = consultar(agendaCalcadaModel.getAno());
			for (int i = 0; i < agendaCalcadaModel.getDias().size(); i++) {
				DiaAgendaCalcadaModel diaModel = agendaCalcadaModel.getDias().get(i);
				if (diaModel.getFuncionario() != null) {
					Funcionario f = fuDao.consultar(diaModel.getFuncionario().getId());
					agendaCalcada.getDias().get(i).setFuncionario(f);
				}
				agendaCalcada.getDias().get(i).setFeriado(diaModel.isFeriado());
			}
			em.merge(agendaCalcada);
		}
	}

	public List<AgendaCalcadaModel> listar() {
		Query query = Uteis.getEntityManager().createNamedQuery("AgendaCalcada.findAll");
		return converterLista(query.getResultList());
	}

	private List<AgendaCalcadaModel> converterLista(Collection<AgendaCalcada> lista) {
		List<AgendaCalcadaModel> agendasCalcadaModel = new ArrayList<>();
		AgendaCalcadaModel agendaCalcadaModel = null;
		if (lista != null) {
			for (AgendaCalcada agenda : lista) {
				agendaCalcadaModel = new AgendaCalcadaModel();
				agendaCalcadaModel.setAno(agenda.getAno());
				List<DiaAgendaCalcadaModel> diasModel = new ArrayList<>();
				DiaAgendaCalcadaModel diaModel = null;
				for (DiaAgendaCalcada dia : agenda.getDias()) {
					diaModel = new DiaAgendaCalcadaModel();
					diaModel.setId(dia.getId());
					diaModel.setData(dia.getData());
					if (dia.getFuncionario() != null) {
						FuncionarioModel f = new FuncionarioModel();
						f.setId(dia.getFuncionario().getId());
						f.setNome(dia.getFuncionario().getNome());
						diaModel.setFuncionario(f);
					}
					diaModel.setFeriado(dia.isFeriado());
					diasModel.add(diaModel);
				}
				agendaCalcadaModel.setDias(diasModel);
				agendasCalcadaModel.add(agendaCalcadaModel);
			}
		}
		return agendasCalcadaModel;

	}

	public void remover(int ano) {
		em = Uteis.getEntityManager();
		em.remove(em.find(AgendaCalcada.class, ano));
	}

	private AgendaCalcada consultar(AgendaCalcadaModel agendaCalcadaModel) {
		return consultar(agendaCalcadaModel.getAno());
	}

	public AgendaCalcada consultar(int ano) {
		return Uteis.getEntityManager().find(AgendaCalcada.class, ano);
	}

	public AgendaCalcadaModel consultarModel(int ano) {
		AgendaCalcada a = consultar(ano);
		AgendaCalcadaModel model = null;
		if (a != null) {
			model = new AgendaCalcadaModel();
			model.setAno(a.getAno());
			List<DiaAgendaCalcadaModel> diasModel = new ArrayList<>();
			DiaAgendaCalcadaModel diaModel = null;
			for (DiaAgendaCalcada dia : a.getDias()) {
				diaModel = new DiaAgendaCalcadaModel();
				diaModel.setId(dia.getId());
				diaModel.setData(dia.getData());
				if (dia.getFuncionario() != null) {
					FuncionarioModel f = new FuncionarioModel();
					f.setId(dia.getFuncionario().getId());
					f.setNome(dia.getFuncionario().getNome());
					diaModel.setFuncionario(f);
				}
				diaModel.setFeriado(dia.isFeriado());
				diasModel.add(diaModel);
			}
			model.setDias(diasModel);
		}
		return model;
	}

	public List<Integer> listarAnosDistintos() {
		return (List<Integer>) Uteis.getEntityManager().createNamedQuery("AgendaCalcada.findAnosDistintos")
				.getResultList();
	}

}