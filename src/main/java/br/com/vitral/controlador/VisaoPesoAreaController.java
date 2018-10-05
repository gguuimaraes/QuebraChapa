package br.com.vitral.controlador;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import br.com.vitral.modelo.AreaCortadaModel;
import br.com.vitral.modelo.PesoModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.persistencia.AreaCortadaDao;
import br.com.vitral.persistencia.PesoDao;
import br.com.vitral.persistencia.SetorDao;
import br.com.vitral.util.Uteis;

@Named(value = "visaoPesoAreaController")
@SessionScoped
public class VisaoPesoAreaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PesoDao pesoDao;

	@Inject
	private SetorDao setorDao;

	@Inject
	private AreaCortadaDao areaCortadaDao;

	private SetorModel setorExpedicao;
	private SetorModel setorExpedicaoVPE;
	private SetorModel setorEntrega;
	private SetorModel setorMesaGrande;
	private SetorModel setorMesaPequena;
	private SetorModel setorPonteRolante;

	private static final String STR_EXPEDICAO = "EXPEDICAO";
	private static final String STR_EXPEDICAO_VPE = "EXPEDICAO VPE";
	private static final String STR_ENTREGA = "ENTREGA";
	private static final String STR_PONTE_ROLANTE = "PONTE ROLANTE";
	private static final String STR_MESA_GRANDE = "MESA GRANDE";
	private static final String STR_MESA_PEQUENA = "MESA PEQUENA";

	private SimpleDateFormat sdf = new SimpleDateFormat("MMM/yyyy");

	@PostConstruct
	public void init() {
		setorExpedicao = setorDao.consultarModelPeloNome(STR_EXPEDICAO);
		setorExpedicaoVPE = setorDao.consultarModelPeloNome(STR_EXPEDICAO_VPE);
		setorEntrega = setorDao.consultarModelPeloNome(STR_ENTREGA);
		setorPonteRolante = setorDao.consultarModelPeloNome(STR_PONTE_ROLANTE);
		setorMesaGrande = setorDao.consultarModelPeloNome(STR_MESA_GRANDE);
		setorMesaPequena = setorDao.consultarModelPeloNome(STR_MESA_PEQUENA);

		iniciaPesoMensal();
		iniciaAreaCortadaMensal();
	}

	public List<PesoModel> getPesos(String nomeSetor) {
		return getPesos(nomeSetor, new Date());
	}

	private List<PesoModel> getPesos(String nomeSetor, Date dia) {
		switch (nomeSetor) {
		case STR_EXPEDICAO:
			return pesoDao.listar(setorExpedicao, dia);
		case STR_EXPEDICAO_VPE:
			return pesoDao.listar(setorExpedicaoVPE, dia);
		case STR_ENTREGA:
			return pesoDao.listar(setorEntrega, dia);
		case STR_PONTE_ROLANTE:
			return pesoDao.listar(setorPonteRolante, dia);
		default:
			return new ArrayList<>();
		}
	}

	public List<AreaCortadaModel> getAreasCortadas(String nomeSetor) {
		return getAreasCortadas(nomeSetor, new Date());
	}

	private List<AreaCortadaModel> getAreasCortadas(String nomeSetor, Date dia) {
		switch (nomeSetor) {
		case STR_MESA_GRANDE:
			return areaCortadaDao.listar(setorMesaGrande, dia);
		case STR_MESA_PEQUENA:
			return areaCortadaDao.listar(setorMesaPequena, dia);
		default:
			return new ArrayList<>();
		}
	}

	public Double getPesoTotal(String nomeSetor) {
		return getPesoTotal(nomeSetor, new Date());
	}

	private Double getPesoTotal(String nomeSetor, Date dia) {
		switch (nomeSetor) {
		case STR_EXPEDICAO:
			return pesoDao.consultarPesoTotal(setorExpedicao, dia);
		case STR_EXPEDICAO_VPE:
			return pesoDao.consultarPesoTotal(setorExpedicaoVPE, dia);
		case STR_ENTREGA:
			return pesoDao.consultarPesoTotal(setorEntrega, dia);
		case STR_PONTE_ROLANTE:
			return pesoDao.consultarPesoTotal(setorPonteRolante, dia);
		default:
			return null;
		}
	}

	public Double getAreaTotal(String nomeSetor) {
		return getAreaTotal(nomeSetor, new Date());
	}

	private Double getAreaTotal(String nomeSetor, Date dia) {
		switch (nomeSetor) {
		case STR_MESA_GRANDE:
			return areaCortadaDao.consultarAreaTotal(setorMesaGrande, dia);
		case STR_MESA_PEQUENA:
			return areaCortadaDao.consultarAreaTotal(setorMesaPequena, dia);
		default:
			return null;
		}
	}

	public Double getPesoTotal() {
		return pesoDao.consultarPesoTotal(new Date());
	}

	public LineChartModel getModelPesoMensal() {
		desenhaPesoMensal();
		return modelPesoMensal;
	}

	public LineChartModel getModelAreaCortadaMensal() {
		desenhaAreaCortadaMensal();
		return modelAreaCortadaMensal;
	}

	public SetorModel getSetorExpedicao() {
		return setorExpedicao;
	}

	public SetorModel getSetorExpedicaoVPE() {
		return setorExpedicaoVPE;
	}

	public SetorModel getSetorEntrega() {
		return setorEntrega;
	}

	public SetorModel getSetorMesaGrande() {
		return setorMesaGrande;
	}

	public SetorModel getSetorMesaPequena() {
		return setorMesaPequena;
	}

	public SetorModel getSetorPonteRolante() {
		return setorPonteRolante;
	}

	private LineChartModel modelPesoMensal;
	private LineChartModel modelAreaCortadaMensal;

	private void iniciaPesoMensal() {
		modelPesoMensal = new LineChartModel();
		modelPesoMensal.setTitle("Peso Mensal");
		modelPesoMensal.setShowPointLabels(true);
		modelPesoMensal.setSeriesColors("800080");
		modelPesoMensal.setExtender("ext");
		modelPesoMensal.setShadow(false);
		CategoryAxis x = new CategoryAxis();
		x.setTickAngle(-15);
		modelPesoMensal.getAxes().put(AxisType.X, x);
		Axis y = modelPesoMensal.getAxis(AxisType.Y);
		y.setTickFormat("%'.2f");
		y.setMin(0);
		y.setLabel("Peso (kg)");
	}

	private void iniciaAreaCortadaMensal() {
		modelAreaCortadaMensal = new LineChartModel();
		modelAreaCortadaMensal.setTitle("Área Cortada Mensal");
		modelAreaCortadaMensal.setShowPointLabels(true);
		modelAreaCortadaMensal.setSeriesColors("809300");
		modelAreaCortadaMensal.setExtender("ext");
		modelAreaCortadaMensal.setShadow(false);
		CategoryAxis x = new CategoryAxis();
		x.setTickAngle(-15);
		modelAreaCortadaMensal.getAxes().put(AxisType.X, x);
		Axis y = modelAreaCortadaMensal.getAxis(AxisType.Y);
		y.setTickFormat("%'.2f");
		y.setMin(0);
		y.setLabel("Área (m²)");

	}

	private void desenhaPesoMensal() {
		modelPesoMensal.clear();
		LineChartSeries series = new LineChartSeries();
		for (int i = 11; i >= 0; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double peso = pesoDao.consultarPesoTotal(Uteis.getDataInicioMes(c.getTime()),
					Uteis.getDataFimMes(c.getTime()));
			c.set(Calendar.DATE, 1);
			if (peso != null)
				series.set(sdf.format(c.getTime()), peso);
		}
		modelPesoMensal.addSeries(series);
	}

	private void desenhaAreaCortadaMensal() {
		modelAreaCortadaMensal.clear();
		LineChartSeries series = new LineChartSeries();
		for (int i = 11; i >= 0; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double area = areaCortadaDao.consultarAreaTotal(Uteis.getDataInicioMes(c.getTime()),
					Uteis.getDataFimMes(c.getTime()));
			c.set(Calendar.DATE, 1);
			if (area != null)
				series.set(sdf.format(c.getTime()), area);
		}
		modelAreaCortadaMensal.addSeries(series);
	}
}