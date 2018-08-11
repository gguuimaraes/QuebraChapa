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
	@Inject
	Funcionario funcionario;
	EntityManager entityManager;

	public void salvar(FuncionarioModel funcionarioModel) {
		entityManager = Uteis.JpaEntityManager();
		if (funcionarioModel.getId() == 0) {
			funcionario = new Funcionario();
			funcionario.setNome(funcionarioModel.getNome());
			entityManager.persist(funcionario);
		} else {
			funcionario = entityManager.find(Funcionario.class, funcionarioModel.getId());
			funcionario.setNome(funcionarioModel.getNome());
			entityManager.merge(funcionario);
		}
	}

	public List<FuncionarioModel> listar() {
		List<FuncionarioModel> funcionariosModel = new ArrayList<FuncionarioModel>();
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("Funcionario.findAll");
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
		entityManager = Uteis.JpaEntityManager();
		entityManager.remove(entityManager.find(Funcionario.class, id));
	}
	
	public Funcionario consultar(int id) {
		return Uteis.JpaEntityManager().find(Funcionario.class, id);
	}
}