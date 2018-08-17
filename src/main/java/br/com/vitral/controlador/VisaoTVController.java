package br.com.vitral.controlador;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.vitral.modelo.SetorModel;
import br.com.vitral.persistencia.AreaFaturadaDao;
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

	public PieChartModel getQuebraPorSetor() {
		PieChartModel model = new PieChartModel();
		model.setTitle("% Quebra por Setor");
		model.setLegendPosition("e");
		model.setShowDataLabels(true);
		model.setDatatipFormat("%s - %#.4f");
		StringBuilder cores = new StringBuilder();
		Collection<Object[]> lista = quebraDao.quebraPorSetor(Uteis.getDataInicio(), Uteis.getDataFim());
		for (Object[] l : lista) {
			if (cores.toString().length() != 0)
				cores.append(',');
			model.set(String.format("%s: %.4f", ((SetorModel) l[0]).getNome(), l[1]), (Double) l[1]);
			cores.append(((SetorModel) l[0]).getCor());
		}

		model.setSeriesColors(cores.toString());

		return model;
	}

	public LineChartModel getQuebraAnual() {
		LineChartModel model = new LineChartModel();
		model.setTitle("Metragem de Quebra Mensal");
		model.setShowPointLabels(true);
		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%m/%Y");
		eixoX.setTickAngle(-15);
		eixoX.setTickInterval("2592000000");
		model.getAxes().put(AxisType.X, eixoX);
		Axis eixoY = model.getAxis(AxisType.Y);
		eixoY.setTickFormat("%.4f");
		eixoY.setMin(0);
		eixoY.setLabel("Quebra (m²)");

		LineChartSeries quebra = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double areaQuebra = quebraDao.areaPorPeriodo(Uteis.getDataInicio(c.getTime()),
					Uteis.getDataFim(c.getTime()));
			if (areaQuebra != null) {
				c.set(Calendar.DATE, 1);
				quebra.set(c.getTimeInMillis(), areaQuebra);
			} else
				break;
		}
		model.addSeries(quebra);
		model.setSeriesColors("ff8000");

		model.setExtender("ext");
		return model;
	}

	public LineChartModel getTaxaQuebraAnual() {
		LineChartModel model = new LineChartModel();
		model.setTitle("Porcentual de Quebra Mensal");
		model.setShowPointLabels(true);
		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%m/%Y");
		eixoX.setTickAngle(-15);
		eixoX.setTickInterval("2592000000");
		model.getAxes().put(AxisType.X, eixoX);
		Axis eixoY = model.getAxis(AxisType.Y);
		eixoY.setTickFormat("%.2f%");
		eixoY.setMin(0);
		eixoY.setLabel("Porcentual");

		LineChartSeries taxa = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double areaQuebra = quebraDao.areaPorPeriodo(Uteis.getDataInicio(c.getTime()),
					Uteis.getDataFim(c.getTime()));
			Float areaFaturada = areaFaturadaDao.maiorAreaPorPeriodo(Uteis.getDataInicio(c.getTime()),
					Uteis.getDataFim(c.getTime()));
			if (areaQuebra != null && areaFaturada != null) {
				c.set(Calendar.DATE, 1);
				taxa.set(c.getTimeInMillis(), areaQuebra / areaFaturada * 100);
			} else
				break;
		}
		model.addSeries(taxa);

		model.setExtender("ext");
		return model;
	}

	public Float getAreaFaturada() {
		return areaFaturadaDao.maiorAreaPorPeriodo(Uteis.getDataInicio(), Uteis.getDataFim());
	}

	public Double getAreaQuebra() {
		return quebraDao.areaPorPeriodo(Uteis.getDataInicio(), Uteis.getDataFim());
	}

}