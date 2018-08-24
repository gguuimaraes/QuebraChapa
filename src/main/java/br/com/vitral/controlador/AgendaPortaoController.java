package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.util.collections.ArraySet;

import br.com.vitral.entidade.AgendaPortao;
import br.com.vitral.entidade.DiaAgendaPortao;
import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.DiaAgendaPortaoModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.persistencia.AgendaPortaoDao;
import br.com.vitral.persistencia.FuncionarioDao;

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

	int ano;
	int mes;

	@PostConstruct
	public void init() {
		anos = new ArraySet<>();
		anos.addAll(agendaDao.listarAnosDistintos());
		ano = Calendar.getInstance().get(Calendar.YEAR);
		anos.add(ano);
		anos.add(ano + 1);
		mes = Calendar.getInstance().get(Calendar.MONTH);
		selecionarAgenda();
	}

	private void selecionarAgenda() {
		AgendaPortao agendaPortao = agendaDao.consultar(mes, ano);
		if (agendaPortao == null) {
			agenda = null;
			return;
		}
		agenda = new AgendaPortaoModel();
		agenda.setMes(agendaPortao.getId().getMes());
		agenda.setAno(agendaPortao.getId().getAno());
		List<DiaAgendaPortaoModel> diasModel = new ArrayList<>();
		DiaAgendaPortaoModel dModel = null;
		for (DiaAgendaPortao d : agendaPortao.getDias()) {
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
		agenda.setDias(diasModel);
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
		selecionarAgenda();
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
		selecionarAgenda();
	}

	public Set<Integer> getAnos() {
		return anos;
	}

	public AgendaPortaoModel getAgenda() {
		return agenda;
	}
}