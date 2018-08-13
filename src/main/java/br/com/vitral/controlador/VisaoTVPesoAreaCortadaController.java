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

import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;

import br.com.vitral.entidade.Setor;
import br.com.vitral.modelo.AreaCortadaModel;
import br.com.vitral.modelo.PesoModel;
import br.com.vitral.persistencia.AreaCortadaDao;
import br.com.vitral.persistencia.PesoDao;
import br.com.vitral.persistencia.SetorDao;

@Named(value = "visaoTVPesoAreaCortadaController")
@SessionScoped
public class VisaoTVPesoAreaCortadaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private PesoDao pesoDao;

	@Inject
	private SetorDao setorDao;

	@Inject
	private AreaCortadaDao areaCortadaDao;

	private Setor setorExpedicao;
	private Setor setorEntrega;
	private Setor setorMesaGrande;
	private Setor setorMesaPequena;

	private static final String STR_EXPEDICAO = "EXPEDICAO";
	private static final String STR_ENTREGA = "ENTREGA";
	private static final String STR_MESA_GRANDE = "MESA GRANDE";
	private static final String STR_MESA_PEQUENA = "MESA PEQUENA";

	@PostConstruct
	public void init() {
		setorExpedicao = setorDao.consultarPeloNome(STR_EXPEDICAO);
		setorEntrega = setorDao.consultarPeloNome(STR_ENTREGA);
		setorMesaGrande = setorDao.consultarPeloNome(STR_MESA_GRANDE);
		setorMesaPequena = setorDao.consultarPeloNome(STR_MESA_PEQUENA);
	}

	public List<PesoModel> getPesos(String nomeSetor) {
		return getPesos(nomeSetor, new Date());
	}

	private List<PesoModel> getPesos(String nomeSetor, Date dia) {
		switch (nomeSetor) {
		case STR_EXPEDICAO:
			return pesoDao.listar(setorExpedicao, dia);
		case STR_ENTREGA:
			return pesoDao.listar(setorEntrega, dia);
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
		case STR_ENTREGA:
			return pesoDao.consultarPesoTotal(setorEntrega, dia);
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

	public BarChartModel getPesoMensal() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		BarChartModel model = new BarChartModel();

		ChartSeries expedicao = new ChartSeries();
		expedicao.setLabel(STR_EXPEDICAO);
		for (int i = 0; i < 30; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i * -1);
			Double peso = pesoDao.consultarPesoTotal(setorExpedicao, c.getTime());
			if (peso != null) {
				expedicao.set(df.format(c.getTime()), peso);
			}
		}

		ChartSeries entrega = new ChartSeries();
		entrega.setLabel(STR_ENTREGA);
		for (int i = 0; i < 30; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i * -1);
			Double peso = pesoDao.consultarPesoTotal(setorEntrega, c.getTime());
			if (peso != null) {
				entrega.set(df.format(c.getTime()), peso);
			}
		}

		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%#d/%m/%Y");
		eixoX.setTickInterval("86400000");
		model.getAxes().put(AxisType.X, eixoX);

		model.addSeries(expedicao);
		model.addSeries(entrega);

		model.setSeriesColors("ff0000,0000ff");
		model.setBarWidth(30);
		return model;
	}
	
	public BarChartModel getAreaMensal() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		BarChartModel model = new BarChartModel();

		ChartSeries mesaPequena = new ChartSeries();
		mesaPequena.setLabel(STR_MESA_PEQUENA);
		for (int i = 0; i < 30; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i * -1);
			Double area = areaCortadaDao.consultarAreaTotal(setorMesaPequena, c.getTime());
			if (area != null) {
				mesaPequena.set(df.format(c.getTime()), area);
			}
		}

		ChartSeries mesaGrande = new ChartSeries();
		mesaGrande.setLabel(STR_MESA_GRANDE);
		for (int i = 0; i < 30; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, i * -1);
			Double area = areaCortadaDao.consultarAreaTotal(setorMesaGrande, c.getTime());
			if (area != null) {
				mesaGrande.set(df.format(c.getTime()), area);
			}
		}

		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%#d/%m/%Y");
		eixoX.setTickInterval("86400000");
		model.getAxes().put(AxisType.X, eixoX);

		model.addSeries(mesaPequena);
		model.addSeries(mesaGrande);

		model.setSeriesColors("ffa500,008000");
		model.setBarWidth(30);
		return model;
	}

}