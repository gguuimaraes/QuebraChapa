package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.util.collections.ArraySet;

import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.SemanaAgendaPortaoModel;
import br.com.vitral.persistencia.AgendaPortaoDao;
import br.com.vitral.persistencia.FuncionarioDao;
import br.com.vitral.util.Uteis;

@Named(value = "agendaPortaoController")
@SessionScoped
public class AgendaPortaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	AgendaPortaoModel agenda = null;

	@Inject
	AgendaPortaoDao agendaDao;

	@Inject
	FuncionarioDao fDao;

	@Produces
	private Set<Integer> anos;

	private int ano;

	private boolean edicao = false;

	@PostConstruct
	public void init() {
		anos = new ArraySet<>();
		anos.addAll(agendaDao.listarAnosDistintos());
		ano = Calendar.getInstance().get(Calendar.YEAR);
		anos.add(ano);
		anos.add(ano + 1);
		selecionarAgenda();
	}

	public void selecionarAgenda() {
		agenda = agendaDao.consultarModel(ano);
		edicao = false;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public Set<Integer> getAnos() {
		return anos;
	}

	public AgendaPortaoModel getAgenda() {
		return agenda;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void criar() {
		agenda = new AgendaPortaoModel();
		agenda.setAno(ano);
		GregorianCalendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.YEAR, ano);
		SemanaAgendaPortaoModel semana = null;
		for (int numeroSemana = 1; numeroSemana <= c.getActualMaximum(Calendar.WEEK_OF_YEAR); numeroSemana++) {
			c.set(Calendar.WEEK_OF_YEAR, numeroSemana);
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			semana = new SemanaAgendaPortaoModel();
			semana.setPrimeiroDia(c.getTime());
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
			semana.setUltimoDia(c.getTime());
			agenda.getSemanas().add(semana);
		}

		Uteis.messageInformation("Preencha os campos");
		edicao = true;
	}

	public void editar() {
		edicao = true;
	}

	public void cancelar() {
		selecionarAgenda();
		edicao = false;
		Uteis.messageInformation("Operação cancelada");
	}

	public void salvar() {
		agendaDao.salvar(agenda);
		edicao = false;
		Uteis.messageInformation("Agenda salva com sucesso");
	}

	public void excluir() {
		agendaDao.remover(ano);
		agenda = null;
		Uteis.messageInformation("Agenda excluída com sucesso");
	}

	public void espelhar() {
		List<SemanaAgendaPortaoModel> preenchidas = new ArrayList<>();
		int i = 0;
		for (; i < agenda.getSemanas().size(); i++) {
			if (agenda.getSemanas().get(i).getAbertura() != null
					&& agenda.getSemanas().get(i).getFechamento() != null) {
				preenchidas.add(agenda.getSemanas().get(i));
			} else {
				break;
			}
		}
		int j = 0;
		for (; i < agenda.getSemanas().size(); i++) {
			agenda.getSemanas().get(i).setAbertura(preenchidas.get(j).getAbertura());
			agenda.getSemanas().get(i).setFechamento(preenchidas.get(j).getFechamento());
			j = j < preenchidas.size() - 1 ? j + 1 : 0;
		}
	}

}