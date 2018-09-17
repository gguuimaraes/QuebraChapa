package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vitral.modelo.AgendaCalcadaModel;
import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.DiaAgendaCalcadaModel;
import br.com.vitral.modelo.SemanaAgendaPortaoModel;
import br.com.vitral.persistencia.AgendaCalcadaDao;
import br.com.vitral.persistencia.AgendaPortaoDao;

@Named(value = "relatorioAgendaPortaoController")
@SessionScoped
public class RelatorioAgendaPortaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	AgendaPortaoDao agendaPortaoDao;

	@Inject
	AgendaCalcadaDao agendaCalcadaDao;

	public List<SemanaAgendaPortaoModel> getSemanas() {
		List<SemanaAgendaPortaoModel> semanas = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		AgendaPortaoModel agenda = agendaPortaoDao.consultarModel(c.get(Calendar.YEAR));
		if (agenda != null) {
			SemanaAgendaPortaoModel semana = agenda.getSemanas().get(c.get(Calendar.WEEK_OF_YEAR) - 1);
			while (semana != null && semanas.size() <= 4) {
				semanas.add(semana);
				semana = obterProximaSemana(agenda, semana);
			}
		}
		return semanas;
	}

	private SemanaAgendaPortaoModel obterProximaSemana(AgendaPortaoModel agenda, SemanaAgendaPortaoModel semana) {
		SemanaAgendaPortaoModel proximaSemana = null;
		int indexSemanaAtual = agenda.getSemanas().indexOf(semana);
		if (indexSemanaAtual + 1 < agenda.getSemanas().size()) {
			proximaSemana = agenda.getSemanas().get(indexSemanaAtual + 1);
		} else {
			agenda = agendaPortaoDao.consultarModel(agenda.getAno() + 1);
			if (agenda != null) {
				proximaSemana = agenda.getSemanas().get(0);
			}
		}
		return proximaSemana;
	}

	public List<DiaAgendaCalcadaModel> getDias() {
		List<DiaAgendaCalcadaModel> dias = new ArrayList<>();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ? 2
				: c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 1 : 0);
		DiaAgendaCalcadaModel dia = agendaCalcadaDao.consultarDiaPelaData(c.getTime());
		while (dia != null && dias.size() <= 4) {
			if (!dia.isFeriado())
				dias.add(dia);
			c.add(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY ? 3 : 1);
			dia = agendaCalcadaDao.consultarDiaPelaData(c.getTime());
		}
		return dias;
	}

}