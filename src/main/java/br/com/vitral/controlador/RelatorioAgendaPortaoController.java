package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.SemanaAgendaPortaoModel;
import br.com.vitral.persistencia.AgendaPortaoDao;

@Named(value = "relatorioAgendaPortaoController")
@SessionScoped
public class RelatorioAgendaPortaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	AgendaPortaoDao agendaDao;

	public List<SemanaAgendaPortaoModel> getSemanas() {
		List<SemanaAgendaPortaoModel> semanas = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		AgendaPortaoModel agenda = agendaDao.consultarModel(c.get(Calendar.YEAR));
		SemanaAgendaPortaoModel semana = agenda.getSemanas().get(c.get(Calendar.WEEK_OF_YEAR) - 1);
		while (semana != null && semanas.size() <= 4) {
			semanas.add(semana);
			semana = obterProximaSemana(agenda, semana);
		}
		return semanas;
	}

	private SemanaAgendaPortaoModel obterProximaSemana(AgendaPortaoModel agenda, SemanaAgendaPortaoModel semana) {
		SemanaAgendaPortaoModel proximaSemana = null;
		int indexSemanaAtual = agenda.getSemanas().indexOf(semana);
		if (indexSemanaAtual + 1 < agenda.getSemanas().size()) {
			proximaSemana = agenda.getSemanas().get(indexSemanaAtual + 1);
		} else {
			agenda = agendaDao.consultarModel(agenda.getAno() + 1);
			if (agenda != null) {
				proximaSemana = agenda.getSemanas().get(0);
			}
		}
		return proximaSemana;
	}

}