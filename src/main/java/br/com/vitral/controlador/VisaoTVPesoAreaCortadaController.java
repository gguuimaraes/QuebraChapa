package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.vitral.modelo.AreaCortadaModel;
import br.com.vitral.modelo.PesoModel;
import br.com.vitral.persistencia.AreaCortadaDao;
import br.com.vitral.persistencia.PesoDao;

@Named(value = "visaoTVPesoAreaCortadaController")
@SessionScoped
public class VisaoTVPesoAreaCortadaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PesoDao pesoDao;

	@Inject
	private AreaCortadaDao areaCortadaDao;

	public List<PesoModel> getPesosExpedicao() {
		List<PesoModel> pesosExpedicao = new ArrayList<>();
		for (PesoModel p : pesoDao.listarPesosDoDia(new Date())) {
			if (p.getSetor().getNome().equals("EXPEDICAO")) {
				pesosExpedicao.add(p);
			}
		}
		return pesosExpedicao;
	}

	public List<PesoModel> getPesosEntrega() {
		List<PesoModel> pesosEntrega = new ArrayList<>();
		for (PesoModel p : pesoDao.listarPesosDoDia(new Date())) {
			if (p.getSetor().getNome().equals("ENTREGA")) {
				pesosEntrega.add(p);
			}
		}
		return pesosEntrega;
	}

	public List<AreaCortadaModel> getAreasCortadasMesaPequena() {
		List<AreaCortadaModel> areasCortadas = new ArrayList<>();
		for (AreaCortadaModel a : areaCortadaDao.listarAreasDoDia(new Date())) {
			if (a.getSetor().getNome().equals("MESA PEQUENA")) {
				areasCortadas.add(a);
			}
		}
		return areasCortadas;
	}

	public List<AreaCortadaModel> getAreasCortadasMesaGrande() {
		List<AreaCortadaModel> areasCortadas = new ArrayList<>();
		for (AreaCortadaModel a : areaCortadaDao.listarAreasDoDia(new Date())) {
			if (a.getSetor().getNome().equals("MESA GRANDE")) {
				areasCortadas.add(a);
			}
		}
		return areasCortadas;
	}

}