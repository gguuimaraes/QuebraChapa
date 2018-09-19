package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Funcionario;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.util.Uteis;

public class FuncionarioDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	Funcionario funcionario;

	public void salvar(FuncionarioModel funcionarioModel) {
		em = Uteis.getEntityManager();
		if (funcionarioModel.getId() == 0) {
			funcionario = new Funcionario();
			funcionario.setNome(funcionarioModel.getNome());
			em.persist(funcionario);
		} else {
			funcionario = em.find(Funcionario.class, funcionarioModel.getId());
			funcionario.setNome(funcionarioModel.getNome());
			em.merge(funcionario);
		}
	}

	public List<FuncionarioModel> listar() {
		List<FuncionarioModel> funcionariosModel = new ArrayList<>();
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Funcionario.findAll");
		@SuppressWarnings("unchecked")
		Collection<Funcionario> funcionarios = (Collection<Funcionario>) query.getResultList();
		FuncionarioModel funcionarioModel = null;
		for (Funcionario f : funcionarios) {
			funcionarioModel = new FuncionarioModel();
			funcionarioModel.setId(f.getId());
			funcionarioModel.setNome(f.getNome());
			funcionariosModel.add(funcionarioModel);
		}
		return funcionariosModel;
	}

	public void remover(int id) {
		em = Uteis.getEntityManager();
		em.remove(em.find(Funcionario.class, id));
	}

	public Funcionario consultar(int id) {
		return Uteis.getEntityManager().find(Funcionario.class, id);
	}

	public FuncionarioModel consultarModel(int id) {
		return converterUnidade(consultar(id));
	}

	private FuncionarioModel converterUnidade(Funcionario funcionario) {
		if (funcionario == null)
			return null;
		FuncionarioModel funcionarioModel = new FuncionarioModel();
		funcionarioModel.setId(funcionario.getId());
		funcionarioModel.setNome(funcionario.getNome());
		return funcionarioModel;
	}
}