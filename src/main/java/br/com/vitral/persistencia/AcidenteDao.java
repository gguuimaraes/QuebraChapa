package br.com.vitral.persistencia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.vitral.entidade.Acidente;
import br.com.vitral.modelo.AcidenteModel;
import br.com.vitral.modelo.FuncionarioModel;
import br.com.vitral.util.Uteis;

public class AcidenteDao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	Acidente acidente;

	@Inject
	FuncionarioDao funcionarioDao;

	transient EntityManager em;

	public void salvar(AcidenteModel acidenteModel) {
		em = Uteis.getEntityManager();
		if (acidenteModel.getId() == null) {
			acidente = new Acidente();
			acidente.setFuncionario(funcionarioDao.consultar(acidenteModel.getFuncionario().getId()));
			acidente.setObs(acidenteModel.getObs());
			acidente.setData(acidenteModel.getData());
			em.persist(acidente);
		} else {
			acidente = em.find(Acidente.class, acidenteModel.getId());
			acidente.setFuncionario(funcionarioDao.consultar(acidenteModel.getFuncionario().getId()));
			acidente.setObs(acidenteModel.getObs());
			acidente.setData(acidenteModel.getData());
			em.merge(acidente);
		}
	}

	public List<AcidenteModel> listar() {
		List<AcidenteModel> acidentesModel = new ArrayList<>();
		em = Uteis.getEntityManager();
		Query query = em.createNamedQuery("Acidente.findAll");
		@SuppressWarnings("unchecked")
		Collection<Acidente> acidentes = (Collection<Acidente>) query.getResultList();
		AcidenteModel acidenteModel = null;
		for (Acidente a : acidentes) {
			acidenteModel = new AcidenteModel();
			acidenteModel.setId(a.getId());
			FuncionarioModel funcionarioModel = new FuncionarioModel();
			funcionarioModel.setId(a.getFuncionario().getId());
			funcionarioModel.setNome(a.getFuncionario().getNome());
			acidenteModel.setFuncionario(funcionarioModel);
			acidenteModel.setObs(a.getObs());
			acidenteModel.setData(a.getData());
			acidentesModel.add(acidenteModel);
		}
		return acidentesModel;
	}

	private Date getDataMaisRecente() {
		return (Date) Uteis.getEntityManager().createNamedQuery("Acidente.findDataMaisRecente").getSingleResult();
	}

	@SuppressWarnings("unchecked")
	private List<Date> listarDatasOrdenadas() {
		return Uteis.getEntityManager().createNamedQuery("Acidente.findDatasOrdenadas").getResultList();
	}

	public void remover(int id) {
		Uteis.getEntityManager().remove(Uteis.getEntityManager().find(Acidente.class, id));
	}

	public long getDiasSemAcidente() {
		return TimeUnit.DAYS.convert(new Date().getTime() - getDataMaisRecente().getTime(), TimeUnit.MILLISECONDS);
	}

	public long getRecorde() {
		List<Date> datas = listarDatasOrdenadas();
		if (datas.isEmpty())
			return 0;

		long maiorDiferenca = TimeUnit.DAYS.convert(new Date().getTime() - datas.get(datas.size() - 1).getTime(),
				TimeUnit.MILLISECONDS);

		for (int i = 0; i < datas.size() - 1; i++) {
			long diferenca = TimeUnit.DAYS.convert(datas.get(i + 1).getTime() - datas.get(i).getTime(),
					TimeUnit.MILLISECONDS);

			if (diferenca > maiorDiferenca) {
				maiorDiferenca = diferenca;
			}
		}

		return maiorDiferenca;
	}
}