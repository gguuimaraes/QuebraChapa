package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.Calendar;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.DiaAgendaPortaoModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.persistencia.AgendaPortaoDao;
import br.com.vitral.persistencia.FuncionarioDao;

@Named(value = "agendaPortaoController")
@SessionScoped
public class AgendaPortaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	AgendaPortaoDao dao;

	@Inject
	FuncionarioDao fDao;

	public void incluirAgendaPortao() {
		AgendaPortaoModel agenda = new AgendaPortaoModel();
		agenda.setAno(2018);
		agenda.setMes(Calendar.AUGUST);
		DiaAgendaPortaoModel dia = null;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		FuncionarioModel fu1 = fDao.listar().get(0);
		FuncionarioModel fu2 = fDao.listar().get(1);
		for (int i = 1; i <= 20; i++) {
			c.add(Calendar.DATE, i);
			dia = new DiaAgendaPortaoModel();
			dia.setData(c.getTime());
			dia.setAbertura(fu1);
			dia.setFechamento(fu2);
			dia.setFeriado(false);
			agenda.getDias().add(dia);
		}
		dao.salvar(agenda);
	}

}