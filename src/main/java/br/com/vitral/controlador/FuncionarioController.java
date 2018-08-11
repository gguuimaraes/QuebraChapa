package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.RowEditEvent;

import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.persistencia.FuncionarioDao;
import br.com.vitral.util.Uteis;

@Named(value = "funcionarioController")
@SessionScoped
public class FuncionarioController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FuncionarioModel funcionarioModel;

	@Inject
	private FuncionarioDao funcionarioDao;

	@Produces
	private List<FuncionarioModel> funcionarios;

	
	@PostConstruct
	public void init() {
		funcionarios = funcionarioDao.listar();
	}

	public void cadastrar() {
		funcionarioModel = new FuncionarioModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').show();");
	}

	public void salvar() {
		funcionarioDao.salvar(funcionarioModel);
		init();
		this.funcionarioModel = new FuncionarioModel();
		PrimeFaces.current().executeScript("PF('dialogCadastro').hide();");
		Uteis.MensagemInfo("Funcionário cadastrado com sucesso");
	}

	public void excluir(FuncionarioModel funcionarioModel) {
		funcionarioDao.remover(funcionarioModel.getId());
		funcionarios.remove(funcionarioModel);
	}

	public FuncionarioModel getFuncionarioModel() {
		return this.funcionarioModel;
	}

	public void setFuncionarioModel(FuncionarioModel funcionarioModel) {
		this.funcionarioModel = funcionarioModel;
	}

	public List<FuncionarioModel> getFuncionarios() {
		return funcionarios;
	}

	public void onRowEdit(RowEditEvent event) {
		funcionarioDao.salvar((FuncionarioModel) event.getObject());
		Uteis.MensagemInfo("Funcionário alterado com sucesso");
	}

	public void onRowCancel() {
		Uteis.MensagemInfo("Operação cancelada");
	}
}