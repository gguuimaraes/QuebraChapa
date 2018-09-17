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
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LegendPlacement;

import br.com.vitral.entidade.Setor;
import br.com.vitral.modelo.AreaCortadaModel;
import br.com.vitral.modelo.PesoModel;
import br.com.vitral.persistencia.AreaCortadaDao;
import br.com.vitral.persistencia.PesoDao;
import br.com.vitral.persistencia.SetorDao;

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

	private Setor setorExpedicao;
	private Setor setorExpedicaoVPE;
	private Setor setorEntrega;
	private Setor setorMesaGrande;
	private Setor setorMesaPequena;
	private Setor setorPonteRolante;

	private static final String STR_EXPEDICAO = "EXPEDICAO";
	private static final String STR_EXPEDICAO_VPE = "EXPEDICAO VPE";
	private static final String STR_ENTREGA = "ENTREGA";
	private static final String STR_PONTE_ROLANTE = "PONTE ROLANTE";
	private static final String STR_MESA_GRANDE = "MESA GRANDE";
	private static final String STR_MESA_PEQUENA = "MESA PEQUENA";

	private SimpleDateFormat df = new SimpleDateFormat("dd/MM");

	@PostConstruct
	public void init() {
		setorExpedicao = setorDao.consultarPeloNome(STR_EXPEDICAO);
		setorExpedicaoVPE = setorDao.consultarPeloNome(STR_EXPEDICAO_VPE);
		setorEntrega = setorDao.consultarPeloNome(STR_ENTREGA);
		setorPonteRolante = setorDao.consultarPeloNome(STR_PONTE_ROLANTE);
		setorMesaGrande = setorDao.consultarPeloNome(STR_MESA_GRANDE);
		setorMesaPequena = setorDao.consultarPeloNome(STR_MESA_PEQUENA);

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

	public BarChartModel getModelPesoMensal() {
		desenhaPesoMensal();
		return modelPesoMensal;
	}

	public BarChartModel getModelAreaMensal() {
		desenhaAreaCortadaMensal();
		return modelAreaCortadaMensal;
	}

	public Setor getSetorExpedicao() {
		return setorExpedicao;
	}

	public Setor getSetorExpedicaoVPE() {
		return setorExpedicaoVPE;
	}

	public Setor getSetorEntrega() {
		return setorEntrega;
	}

	public Setor getSetorMesaGrande() {
		return setorMesaGrande;
	}

	public Setor getSetorMesaPequena() {
		return setorMesaPequena;
	}

	public Setor getSetorPonteRolante() {
		return setorPonteRolante;
	}

	private BarChartModel modelPesoMensal;
	private BarChartModel modelAreaCortadaMensal;

	private void iniciaPesoMensal() {
		modelPesoMensal = new BarChartModel();
		modelPesoMensal.setTitle("Peso Mensal");
		modelPesoMensal.setLegendPosition("s");
		modelPesoMensal.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		modelPesoMensal.setLegendCols(4);
		modelPesoMensal.setDatatipFormat("%s - %s");		
		modelPesoMensal.setExtender("ext");
		Axis yAxis = modelPesoMensal.getAxis(AxisType.Y);
		yAxis.setLabel("Peso (kg)");
		yAxis.setMin(0);
		StringBuilder cores = new StringBuilder();
		cores.append(setorExpedicao.getCor());
		cores.append(',');
		cores.append(setorExpedicaoVPE.getCor());
		cores.append(',');
		cores.append(setorEntrega.getCor());
		cores.append(',');
		cores.append(setorPonteRolante.getCor());
		modelPesoMensal.setSeriesColors(cores.toString());
	}

	private void iniciaAreaCortadaMensal() {
		modelAreaCortadaMensal = new BarChartModel();
		modelAreaCortadaMensal.setTitle("�rea Cortada Mensal");
		modelAreaCortadaMensal.setLegendPosition("s");
		modelAreaCortadaMensal.setLegendPlacement(LegendPlacement.OUTSIDEGRID);
		modelAreaCortadaMensal.setLegendCols(2);
		modelAreaCortadaMensal.setDatatipFormat("%s - %s");
		modelAreaCortadaMensal.setExtender("ext");
		Axis yAxis = modelAreaCortadaMensal.getAxis(AxisType.Y);
		yAxis.setLabel("�rea (m�)");
		yAxis.setMin(0);
		StringBuilder cores = new StringBuilder();
		cores.append(setorMesaPequena.getCor());
		cores.append(',');
		cores.append(setorMesaGrande.getCor());
		modelAreaCortadaMensal.setSeriesColors(cores.toString());
	}

	private void desenhaPesoMensal() {
		ChartSeries expedicao = new ChartSeries();
		ChartSeries expedicaoVPE = new ChartSeries();
		ChartSeries entrega = new ChartSeries();
		ChartSeries ponteRolante = new ChartSeries();
		expedicao.setLabel(STR_EXPEDICAO);
		expedicaoVPE.setLabel(STR_EXPEDICAO_VPE);
		entrega.setLabel(STR_ENTREGA);
		ponteRolante.setLabel(STR_PONTE_ROLANTE);
		for (int i = 15; i >= 0; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i * -1);
			Double pEx = pesoDao.consultarPesoTotal(setorExpedicao, c.getTime());
			Double pExVPE = pesoDao.consultarPesoTotal(setorExpedicaoVPE, c.getTime());
			Double pEn = pesoDao.consultarPesoTotal(setorEntrega, c.getTime());
			Double pPR = pesoDao.consultarPesoTotal(setorPonteRolante, c.getTime());
			if (pEx != null || pExVPE != null || pEn != null || pPR != null) {
				expedicao.set(df.format(c.getTime()), pEx == null ? 0f : pEx);
				expedicaoVPE.set(df.format(c.getTime()), pExVPE == null ? 0f : pExVPE);
				entrega.set(df.format(c.getTime()), pEn == null ? 0f : pEn);
				ponteRolante.set(df.format(c.getTime()), pPR == null ? 0f : pPR);
			}
		}
		modelPesoMensal.clear();
		modelPesoMensal.addSeries(expedicao);
		modelPesoMensal.addSeries(expedicaoVPE);
		modelPesoMensal.addSeries(entrega);
		modelPesoMensal.addSeries(ponteRolante);
	}

	private void desenhaAreaCortadaMensal() {
		ChartSeries mesaPequena = new ChartSeries();
		ChartSeries mesaGrande = new ChartSeries();
		mesaPequena.setLabel(STR_MESA_PEQUENA);
		mesaGrande.setLabel(STR_MESA_GRANDE);
		for (int i = 15; i >= 0; i--) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i * -1);
			Double aMP = areaCortadaDao.consultarAreaTotal(setorMesaPequena, c.getTime());
			Double aMG = areaCortadaDao.consultarAreaTotal(setorMesaGrande, c.getTime());
			if (aMP != null || aMG != null) {
				mesaPequena.set(df.format(c.getTime()), aMP == null ? 0f : aMP);
				mesaGrande.set(df.format(c.getTime()), aMG == null ? 0f : aMG);
			}
		}
		modelAreaCortadaMensal.clear();
		modelAreaCortadaMensal.addSeries(mesaPequena);
		modelAreaCortadaMensal.addSeries(mesaGrande);
	}
}