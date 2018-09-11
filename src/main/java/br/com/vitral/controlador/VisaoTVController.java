package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.vitral.modelo.AgendaPortaoModel;
import br.com.vitral.modelo.SemanaAgendaPortaoModel;
import br.com.vitral.modelo.SetorModel;
import br.com.vitral.persistencia.AgendaPortaoDao;
import br.com.vitral.persistencia.AreaFaturadaDao;
import br.com.vitral.persistencia.FuncionarioDao;
import br.com.vitral.persistencia.QuebraDao;
import br.com.vitral.util.Uteis;

@Named(value = "visaoTVController")
@SessionScoped
public class VisaoTVController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private QuebraDao quebraDao;

	@Inject
	private AreaFaturadaDao areaFaturadaDao;

	private PieChartModel modelQuebraSetor;

	private LineChartModel modelAreaQuebraMensal;

	private LineChartModel modelTaxaQuebraMensal;

	@PostConstruct
	public void init() {
		iniciaQuebraSetor();
		iniciaAreaQuebraMensal();
		iniciaTaxaQuebraMensal();
		desenhaQuebraSetor();
		desenhaAreaQuebraMensal();
		desenhaTaxaQuebraMensal();
	}
	
	private void iniciaQuebraSetor() {
		modelQuebraSetor = new PieChartModel();
		modelQuebraSetor.setTitle("% Quebra por Setor");
		modelQuebraSetor.setLegendPosition("e");
		modelQuebraSetor.setShowDataLabels(true);
		modelQuebraSetor.setDatatipFormat("%s");
		modelQuebraSetor.setShadow(false);
	}

	private void iniciaAreaQuebraMensal() {
		modelAreaQuebraMensal = new LineChartModel();
		modelAreaQuebraMensal.setTitle("Metragem de Quebra Mensal");
		modelAreaQuebraMensal.setShowPointLabels(true);
		modelAreaQuebraMensal.setSeriesColors("ff8000");
		modelAreaQuebraMensal.setExtender("ext");
		modelAreaQuebraMensal.setShadow(false);
		DateAxis x = new DateAxis();
		x.setTickFormat("%m/%Y");
		x.setTickAngle(-15);
		x.setTickInterval("2592000000");
		modelAreaQuebraMensal.getAxes().put(AxisType.X, x);
		Axis y = modelAreaQuebraMensal.getAxis(AxisType.Y);
		y.setTickFormat("%.2f");
		y.setMin(0);
		y.setLabel("Quebra (m²)");
	}

	private void iniciaTaxaQuebraMensal() {
		modelTaxaQuebraMensal = new LineChartModel();
		modelTaxaQuebraMensal.setTitle("Percentual de Quebra Mensal");
		modelTaxaQuebraMensal.setShowPointLabels(true);
		modelTaxaQuebraMensal.setExtender("ext");
		modelTaxaQuebraMensal.setShadow(false);
		DateAxis x = new DateAxis();
		x.setTickFormat("%m/%Y");
		x.setTickAngle(-15);
		x.setTickInterval("2592000000");
		modelTaxaQuebraMensal.getAxes().put(AxisType.X, x);
		Axis y = modelTaxaQuebraMensal.getAxis(AxisType.Y);
		y.setTickFormat("%.2f%");
		y.setMin(0);
		y.setLabel("Percentual");
	}

	private void desenhaQuebraSetor() {
		modelQuebraSetor.clear();
		StringBuilder cores = new StringBuilder();
		Collection<Object[]> lista = quebraDao.quebraPorSetor(Uteis.getDataInicio(), Uteis.getDataFim());
		for (Object[] l : lista) {
			if (cores.toString().length() != 0)
				cores.append(',');
			modelQuebraSetor.set(String.format("%s: %.4f m²", ((SetorModel) l[0]).getNome(), l[1]), (Double) l[1]);
			cores.append(((SetorModel) l[0]).getCor());
		}
		modelQuebraSetor.setSeriesColors(cores.toString());
	}

	private void desenhaAreaQuebraMensal() {
		modelAreaQuebraMensal.clear();
		LineChartSeries series = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double areaQuebra = quebraDao.areaPorPeriodo(Uteis.getDataInicio(c.getTime()),
					Uteis.getDataFim(c.getTime()));
			c.set(Calendar.DATE, 1);
			if (areaQuebra == null && i != 0)
				break;
			series.set(c.getTimeInMillis(), areaQuebra != null ? areaQuebra : 0);
		}
		modelAreaQuebraMensal.addSeries(series);
	}

	private void desenhaTaxaQuebraMensal() {
		modelTaxaQuebraMensal.clear();
		LineChartSeries series = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double areaQuebra = quebraDao.areaPorPeriodo(Uteis.getDataInicio(c.getTime()),
					Uteis.getDataFim(c.getTime()));
			Float areaFaturada = areaFaturadaDao.maiorAreaPorPeriodo(Uteis.getDataInicio(c.getTime()),
					Uteis.getDataFim(c.getTime()));
			c.set(Calendar.DATE, 1);
			if (areaQuebra == null && areaFaturada == null && i != 0)
				break;
			series.set(c.getTimeInMillis(),
					areaQuebra != null && areaFaturada != null ? areaQuebra / areaFaturada * 100 : 0);
		}
		modelTaxaQuebraMensal.addSeries(series);
	}

	public PieChartModel getModelQuebraSetor() {
		desenhaQuebraSetor();
		return modelQuebraSetor;
	}

	public LineChartModel getModelAreaQuebraMensal() {
		desenhaAreaQuebraMensal();
		return modelAreaQuebraMensal;
	}

	public LineChartModel getModelTaxaQuebraMensal() {
		desenhaTaxaQuebraMensal();
		return modelTaxaQuebraMensal;
	}

	public Float getAreaFaturada() {
		return areaFaturadaDao.maiorAreaPorPeriodo(Uteis.getDataInicio(), Uteis.getDataFim());
	}

	public Double getAreaQuebra() {
		return quebraDao.areaPorPeriodo(Uteis.getDataInicio(), Uteis.getDataFim());
	}

}