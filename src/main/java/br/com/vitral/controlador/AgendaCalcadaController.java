package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vitral.modelo.AgendaCalcadaModel;
import br.com.vitral.modelo.DiaAgendaCalcadaModel;
import br.com.vitral.persistencia.AgendaCalcadaDao;
import br.com.vitral.persistencia.FuncionarioDao;
import br.com.vitral.util.Uteis;

@Named(value = "agendaCalcadaController")
@SessionScoped
public class AgendaCalcadaController implements Serializable {

	private static final long serialVersionUID = 1L;

	AgendaCalcadaModel agenda = null;

	@Inject
	AgendaCalcadaDao agendaDao;

	@Inject
	FuncionarioDao fDao;

	@Produces
	private Set<Integer> anos;

	private int ano;

	private boolean edicao = false;

	private int mes;

	private List<DiaAgendaCalcadaModel> dias;

	@PostConstruct
	public void init() {
		anos = new HashSet<>();
		anos.addAll(agendaDao.listarAnosDistintos());
		ano = GregorianCalendar.getInstance().get(Calendar.YEAR);
		anos.add(ano);
		anos.add(ano + 1);
		selecionarAno();
		atualizarDias();
	}

	public void selecionarAno() {
		agenda = agendaDao.consultarModel(ano);
		Calendar c = Calendar.getInstance();
		mes = ano == c.get(Calendar.YEAR) ? c.get(Calendar.MONTH) : 0;
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

	public AgendaCalcadaModel getAgenda() {
		return agenda;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
		atualizarDias();
	}

	private void atualizarDias() {
		if (agenda != null) {
			dias = new ArrayList<>();
			GregorianCalendar c = new GregorianCalendar();
			int mesFor = 0;
			for (int i = mes == 0 ? mes : mes * 20; i < agenda.getDias().size() && mesFor <= mes; i++) {
				c.setTime(agenda.getDias().get(i).getData());
				mesFor = c.get(Calendar.MONTH);
				if (mesFor == mes)
					dias.add(agenda.getDias().get(i));
			}
		}
	}

	public List<DiaAgendaCalcadaModel> getDias() {
		return dias;
	}

	public void criar() {
		agenda = new AgendaCalcadaModel();
		agenda.setAno(ano);
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.YEAR, ano);
		DiaAgendaCalcadaModel dia = null;
		for (int numeroDia = 1; numeroDia <= c.getActualMaximum(Calendar.DAY_OF_YEAR); numeroDia++) {
			c.set(Calendar.DAY_OF_YEAR, numeroDia);
			if (c.get(Calendar.DAY_OF_WEEK) >= Calendar.MONDAY && c.get(Calendar.DAY_OF_WEEK) <= Calendar.FRIDAY) {
				dia = new DiaAgendaCalcadaModel();
				dia.setData(c.getTime());
				agenda.getDias().add(dia);
			} else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				numeroDia++;
			}
		}
		c = Calendar.getInstance();
		mes = ano == c.get(Calendar.YEAR) ? c.get(Calendar.MONTH) : 0;
		atualizarDias();
		Uteis.messageInformation("Preencha os campos");
		edicao = true;
	}

	public void editar() {
		edicao = true;
	}

	public void cancelar() {
		selecionarAno();
		atualizarDias();
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
		List<DiaAgendaCalcadaModel> preenchidos = new ArrayList<>();
		int i = 0;
		for (; i < getDias().size(); i++) {
			if (getDias().get(i).getFuncionario() != null && !getDias().get(i).isFeriado()) {
				preenchidos.add(getDias().get(i));
			}
		}
		int j = 0;
		for (i = preenchidos.size(); i < getDias().size(); i++) {
			if (!getDias().get(i).isFeriado() && getDias().get(i).getFuncionario() == null) {
				getDias().get(i).setFuncionario(preenchidos.get(j).getFuncionario());
				j = j < preenchidos.size() - 1 ? j + 1 : 0;
			}
		}
	}

}