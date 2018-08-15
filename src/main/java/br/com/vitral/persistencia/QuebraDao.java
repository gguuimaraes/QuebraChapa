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
import br.com.vitral.entidade.Setor;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.modelo.QuebraModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.modelo.TipoVidroModel;
import br.com.vitral.util.Uteis;

public class QuebraDao implements Serializable {

	private static final long serialVersionUID = 1L;

	transient EntityManager em;

	@Inject
	Quebra quebra;
	
	@Inject
	TipoVidroDao tipoVidroDao;
	
	@Inject
	SetorDao setorDao;
	
	@Inject
	FuncionarioDao funcionarioDao;

	private static final String STR_DATA_INICIO = "dataInicio";
	private static final String STR_DATA_FIM = "dataFim";

	public void salvar(QuebraModel quebraModel) {
		em = Uteis.getEntityManager();
		if (quebraModel.getId() == 0) {
			quebra = new Quebra();
			quebra.setData(quebraModel.getData());
			quebra.setTipoVidro(tipoVidroDao.consultar(quebraModel.getTipoVidro().getId()));
			quebra.setAreaTotal(quebraModel.getAreaTotal());
			quebra.setAreaAproveitada(quebraModel.getAreaAproveitada());
			quebra.setSetor(setorDao.consultar(quebraModel.getSetor().getId()));
			quebra.setFuncionario(quebraModel.getFuncionario() == null ? null
					: funcionarioDao.consultar(quebraModel.getFuncionario().getId()));
			quebra.setMotivo(quebraModel.getMotivo());
			em.persist(quebra);
		} else {
			quebra = em.find(Quebra.class, quebraModel.getId());
			quebra.setData(quebraModel.getData());
			quebra.setTipoVidro(tipoVidroDao.consultar(quebraModel.getTipoVidro().getId()));
			quebra.setAreaTotal(quebraModel.getAreaTotal());
			quebra.setAreaAproveitada(quebraModel.getAreaAproveitada());
			quebra.setSetor(setorDao.consultar(quebraModel.getSetor().getId()));
			quebra.setFuncionario(quebraModel.getFuncionario() == null ? null
					: funcionarioDao.consultar(quebraModel.getFuncionario().getId()));
			quebra.setMotivo(quebraModel.getMotivo());
			em.merge(quebra);
		}
	}

	public List<QuebraModel> listar() {
		List<QuebraModel> quebrasModel = new ArrayList<>();
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Quebra.findAll");
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
		List<QuebraModel> quebrasModel = new ArrayList<>();
		em = Uteis.getEntityManager();
		StringBuilder sb = new StringBuilder(
				"SELECT q FROM Quebra q WHERE q.data >= :dataInicio AND q.data <= :dataFim ");
		for (Object p : parametros) {
			sb.append("AND ");
			if (p instanceof SetorModel) {
				sb.append("q.setor.id = :setorId ");
			} else if (p instanceof TipoVidroModel) {
				sb.append("q.tipoVidro.id = :tipoVidroId ");
			} else if (p instanceof FuncionarioModel && ((FuncionarioModel) p).getId() != 0) {
				sb.append("q.funcionario.id = :funcionarioId ");
			} else sb.append("q.funcionario.id IS NULL ");

		}
		sb.append("ORDER BY q.data DESC");
		Query query = em.createQuery(sb.toString());
		query.setParameter(STR_DATA_INICIO, dataInicio);
		query.setParameter(STR_DATA_FIM, dataFim);
		for (Object p : parametros) {
			if (p instanceof SetorModel) {
				query.setParameter("setorId", ((SetorModel) p).getId());
			} else if (p instanceof TipoVidroModel) {
				query.setParameter("tipoVidroId", ((TipoVidroModel) p).getId());
			} else if (p instanceof FuncionarioModel && ((FuncionarioModel) p).getId() != 0) {
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
		em = Uteis.getEntityManager();
		em.remove(em.find(Quebra.class, id));
	}

	public Double areaPorPeriodo(Date dataInicio, Date dataFim) {
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Quebra.findAreaPorPeriodo");
		query.setParameter(STR_DATA_INICIO, dataInicio);
		query.setParameter(STR_DATA_FIM, dataFim);
		return (Double) query.getSingleResult();
	}

	public Collection<Object[]> quebraPorSetor(Date dataInicio, Date dataFim) {
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Quebra.findQuebraPorSetorEPeriodo");
		query.setParameter(STR_DATA_INICIO, dataInicio);
		query.setParameter(STR_DATA_FIM, dataFim);
		@SuppressWarnings("unchecked")
		Collection<Object[]> lista = (Collection<Object[]>) query.getResultList();
		for (Object[] l : lista) {
			Setor s = setorDao.consultar((int) l[0]);
			SetorModel sModel = new SetorModel();
			sModel.setId(s.getId());
			sModel.setNome(s.getNome());
			sModel.setCor(s.getCor());
			l[0] = sModel;
		}
		return lista;
	}
}