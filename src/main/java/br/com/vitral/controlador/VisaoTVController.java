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
		PieChartModel quebraPorSetor = new PieChartModel();
		quebraPorSetor.setTitle("% Quebra por Setor");
		quebraPorSetor.setLegendPosition("e");
		quebraPorSetor.setShowDataLabels(true);
		quebraPorSetor.setDatatipFormat("%s - %#.4f");
		quebraPorSetor.setSeriesColors("aaf,afa,faa,ffa,aff,faf,ddd");
		Collection<Object[]> lista = quebraDao.quebraPorSetor(Uteis.getDataInicio(), Uteis.getDataFim());
		for (Object[] l : lista) {
			quebraPorSetor.set(String.format("%s: %.4f", l[0], l[1]), (Double) l[1]);
		}
		return quebraPorSetor;
	}

	public LineChartModel getQuebraAnual() {
		LineChartModel quebraAnual = new LineChartModel();
		quebraAnual.setTitle("Quebra Mensal");
		quebraAnual.setShowPointLabels(true);
		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%m/%Y");
		eixoX.setTickAngle(-15);
		eixoX.setTickInterval("2592000000");
		quebraAnual.getAxes().put(AxisType.X, eixoX);
		Axis eixoY = quebraAnual.getAxis(AxisType.Y);
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
			} else break;
		}
		quebraAnual.addSeries(quebra);
		quebraAnual.setSeriesColors("ff8000");
		return quebraAnual;
	}

	public LineChartModel getTaxaQuebraAnual() {
		LineChartModel taxaQuebraAnual = new LineChartModel();
		taxaQuebraAnual.setTitle("% Quebra Anual");
		taxaQuebraAnual.setShowPointLabels(true);
		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%m/%Y");
		eixoX.setTickAngle(-15);
		eixoX.setTickInterval("2592000000");
		taxaQuebraAnual.getAxes().put(AxisType.X, eixoX);
		Axis eixoY = taxaQuebraAnual.getAxis(AxisType.Y);
		eixoY.setTickFormat("%.2f%");
		eixoY.setMin(0);
		eixoY.setLabel("Taxa");

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
			} else break;
		}
		taxaQuebraAnual.addSeries(taxa);

		return taxaQuebraAnual;
	}

	public Float getAreaFaturada() {
		return areaFaturadaDao.maiorAreaPorPeriodo(Uteis.getDataInicio(), Uteis.getDataFim());
	}

	public Double getAreaQuebra() {
		return quebraDao.areaPorPeriodo(Uteis.getDataInicio(), Uteis.getDataFim());
	}

}