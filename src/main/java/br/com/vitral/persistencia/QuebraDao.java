package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Quebra;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.QuebraModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.modelo.TipoVidroModel;
import br.com.vitral.util.Uteis;

public class QuebraDao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	Quebra quebra;
	EntityManager entityManager;

	@Inject
	TipoVidroDao tipoVidroDao;
	@Inject
	SetorDao setorDao;
	@Inject
	FuncionarioDao funcionarioDao;

	public void salvar(QuebraModel quebraModel) {
		entityManager = Uteis.JpaEntityManager();
		if (quebraModel.getId() == null) {
			quebra = new Quebra();
			quebra.setData(quebraModel.getData());
			quebra.setTipoVidro(tipoVidroDao.consultar(quebraModel.getTipoVidro().getId()));
			quebra.setAreaTotal(quebraModel.getAreaTotal());
			quebra.setAreaAproveitada(quebraModel.getAreaAproveitada());
			quebra.setSetor(setorDao.consultar(quebraModel.getSetor().getId()));
			quebra.setFuncionario(quebraModel.getFuncionario() == null ? null
					: funcionarioDao.consultar(quebraModel.getFuncionario().getId()));
			quebra.setMotivo(quebraModel.getMotivo());
			entityManager.persist(quebra);
		} else {
			quebra = entityManager.find(Quebra.class, quebraModel.getId());
			quebra.setData(quebraModel.getData());
			quebra.setTipoVidro(tipoVidroDao.consultar(quebraModel.getTipoVidro().getId()));
			quebra.setAreaTotal(quebraModel.getAreaTotal());
			quebra.setAreaAproveitada(quebraModel.getAreaAproveitada());
			quebra.setSetor(setorDao.consultar(quebraModel.getSetor().getId()));
			quebra.setFuncionario(quebraModel.getFuncionario() == null ? null
					: funcionarioDao.consultar(quebraModel.getFuncionario().getId()));
			quebra.setMotivo(quebraModel.getMotivo());
			entityManager.merge(quebra);
		}
	}

	public List<QuebraModel> listar() {
		List<QuebraModel> quebrasModel = new ArrayList<QuebraModel>();
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("Quebra.findAll");
		@SuppressWarnings("unchecked")
		Collection<Quebra> quebras = (Collection<Quebra>) query.getResultList();
		QuebraModel quebraModel = null;
		for (Quebra q : quebras) {
			quebraModel = new QuebraModel();
			quebraModel.setId(q.getId());
			quebraModel.setData(q.getData());
			TipoVidroModel tipoVidroModel = new TipoVidroModel();
			tipoVidroModel.setId(q.getTipoVidro().getId());
			tipoVidroModel.setNome(q.getTipoVidro().getNome());
			quebraModel.setTipoVidro(tipoVidroModel);
			quebraModel.setAreaTotal(q.getAreaTotal());
			quebraModel.setAreaAproveitada(q.getAreaAproveitada());
			SetorModel setorModel = new SetorModel();
			setorModel.setId(q.getSetor().getId());
			setorModel.setNome(q.getSetor().getNome());
			quebraModel.setSetor(setorModel);
			if (q.getFuncionario() != null) {
				FuncionarioModel funcionarioModel = new FuncionarioModel();
				funcionarioModel.setId(q.getFuncionario().getId());
				funcionarioModel.setNome(q.getFuncionario().getNome());
				quebraModel.setFuncionario(funcionarioModel);
			}
			quebraModel.setMotivo(q.getMotivo());
			quebrasModel.add(quebraModel);
		}
		return quebrasModel;
	}

	public List<QuebraModel> listarPorPeriodo(Date dataInicio, Date dataFim, Object... parametros) {
		List<QuebraModel> quebrasModel = new ArrayList<QuebraModel>();
		entityManager = Uteis.JpaEntityManager();
		String queryString = "SELECT q FROM Quebra q ";
		queryString += "WHERE ";
		queryString += "q.data >= :dataInicio ";
		queryString += "AND ";
		queryString += "q.data <= :dataFim ";

		for (Object p : parametros) {
			queryString += "AND ";
			if (p instanceof SetorModel) {
				queryString += "q.setor.id = :setorId ";
			} else if (p instanceof TipoVidroModel) {
				queryString += "q.tipoVidro.id = :tipoVidroId ";
			} else if (p instanceof FuncionarioModel) {
				if (((FuncionarioModel) p).getId() != 0)
					queryString += "q.funcionario.id = :funcionarioId ";
				else
					queryString += "q.funcionario.id IS NULL ";
			}
		}
		queryString += "ORDER BY q.data DESC";
		Query query = entityManager.createQuery(queryString);
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		for (Object p : parametros) {
			if (p instanceof SetorModel) {
				query.setParameter("setorId", ((SetorModel) p).getId());
			} else if (p instanceof TipoVidroModel) {
				query.setParameter("tipoVidroId", ((TipoVidroModel) p).getId());
			} else if (p instanceof FuncionarioModel) {
				if (((FuncionarioModel) p).getId() != 0)
					query.setParameter("funcionarioId", ((FuncionarioModel) p).getId());
			}
		}
		@SuppressWarnings("unchecked")
		Collection<Quebra> quebras = (Collection<Quebra>) query.getResultList();
		QuebraModel quebraModel = null;
		for (Quebra q : quebras) {
			quebraModel = new QuebraModel();
			quebraModel.setId(q.getId());
			quebraModel.setData(q.getData());
			TipoVidroModel tipoVidroModel = new TipoVidroModel();
			tipoVidroModel.setId(q.getTipoVidro().getId());
			tipoVidroModel.setNome(q.getTipoVidro().getNome());
			quebraModel.setTipoVidro(tipoVidroModel);
			quebraModel.setAreaTotal(q.getAreaTotal());
			quebraModel.setAreaAproveitada(q.getAreaAproveitada());
			SetorModel setorModel = new SetorModel();
			setorModel.setId(q.getSetor().getId());
			setorModel.setNome(q.getSetor().getNome());
			quebraModel.setSetor(setorModel);
			if (q.getFuncionario() != null) {
				FuncionarioModel funcionarioModel = new FuncionarioModel();
				funcionarioModel.setId(q.getFuncionario().getId());
				funcionarioModel.setNome(q.getFuncionario().getNome());
				quebraModel.setFuncionario(funcionarioModel);
			}
			quebraModel.setMotivo(q.getMotivo());
			quebrasModel.add(quebraModel);
		}
		return quebrasModel;
	}

	public void remover(int id) {
		entityManager = Uteis.JpaEntityManager();
		entityManager.remove(entityManager.find(Quebra.class, id));
	}

	public Double areaPorPeriodo(Date dataInicio, Date dataFim) {
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("Quebra.findAreaPorPeriodo");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		return (Double) query.getSingleResult();
	}

	public Collection<Object[]> quebraPorSetor(Date dataInicio, Date dataFim) {
		entityManager = Uteis.JpaEntityManager();
		Query query = entityManager.createNamedQuery("Quebra.findQuebraPorSetorEPeriodo");
		query.setParameter("dataInicio", dataInicio);
		query.setParameter("dataFim", dataFim);
		@SuppressWarnings("unchecked")
		Collection<Object[]> lista = (Collection<Object[]>) query.getResultList();
		SetorDao setorDao = new SetorDao();
		for (Object[] l : lista) {
			l[0] = setorDao.consultar((int) l[0]).getNome();
		}
		return lista;
	}
}