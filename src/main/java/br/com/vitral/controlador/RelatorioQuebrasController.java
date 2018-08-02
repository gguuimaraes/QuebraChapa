package br.com.vitral.controlador;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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

@Named(value = "relatorioQuebrasController")
@SessionScoped
public class RelatorioQuebrasController implements Serializable {

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
		Collection<Object[]> lista = quebraDao.quebraPorSetor(getDataInicio(), getDataFim());
		for (Object[] l : lista) {
			quebraPorSetor.set(String.format("%s: %.4f", l[0], l[1]), (Double) l[1]);
		}
		return quebraPorSetor;
	}

	public LineChartModel getQuebraAnual() {
		LineChartModel quebraAnual = new LineChartModel();
		quebraAnual.setTitle("Quebra Mensal");
		quebraAnual.setShowPointLabels(true);
		quebraAnual.setExtender("quebraAnualExt");
		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%b");
		quebraAnual.getAxes().put(AxisType.X, eixoX);
		Axis eixoY = quebraAnual.getAxis(AxisType.Y);
		eixoY.setTickFormat("%.4f");
		eixoY.setMin(0);
		eixoY.setLabel("Quebra (m²)");

		LineChartSeries quebra = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double areaQuebra = quebraDao.areaPorPeriodo(getDataInicio(c.getTime()), getDataFim(c.getTime()));
			if (areaQuebra != null) {
				c.set(Calendar.DATE, 1);
				quebra.set(c.getTimeInMillis(), areaQuebra);
			} else
				i = 12;
		}
		quebraAnual.addSeries(quebra);
		quebraAnual.setSeriesColors("ff8000");
		return quebraAnual;
	}

	public LineChartModel getTaxaQuebraAnual() {
		LineChartModel taxaQuebraAnual = new LineChartModel();
		taxaQuebraAnual.setTitle("% Quebra Anual");
		taxaQuebraAnual.setShowPointLabels(true);
		taxaQuebraAnual.setExtender("quebraAnualExt");
		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%b");
		taxaQuebraAnual.getAxes().put(AxisType.X, eixoX);
		Axis eixoY = taxaQuebraAnual.getAxis(AxisType.Y);
		eixoY.setTickFormat("%.2f%");
		eixoY.setMin(0);
		eixoY.setLabel("Taxa");

		LineChartSeries taxa = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			Double areaQuebra = quebraDao.areaPorPeriodo(getDataInicio(c.getTime()), getDataFim(c.getTime()));
			Float areaFaturada = areaFaturadaDao.maiorAreaPorPeriodo(getDataInicio(c.getTime()), getDataFim(c.getTime()));
			if (areaQuebra != null && areaFaturada != null) {
				c.set(Calendar.DATE, 1);
				taxa.set(c.getTimeInMillis(), areaQuebra / areaFaturada * 100);
			} else
				i = 12;
		}
		taxaQuebraAnual.addSeries(taxa);

		return taxaQuebraAnual;
	}
	
	public Float getAreaFaturada() {
		return areaFaturadaDao.maiorAreaPorPeriodo(getDataInicio(), getDataFim());
	}

	public Double getAreaQuebra() {
		return quebraDao.areaPorPeriodo(getDataInicio(), getDataFim());
	}

	private Date getDataInicio(Date data) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(data);
		inicio.set(Calendar.MILLISECOND, 0);
		inicio.set(Calendar.SECOND, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.HOUR, 12);
		if (inicio.get(Calendar.DATE) < 22) {
			inicio.add(Calendar.MONTH, -1);
		}
		inicio.set(Calendar.DATE, 22);
		// System.out.println("Data Inicio = " + new SimpleDateFormat("dd/MM/yyyy
		// HH:mm:ss.SSS").format(inicio.getTime()));
		return inicio.getTime();
	}

	private Date getDataFim(Date data) {
		Calendar fim = Calendar.getInstance();
		fim.setTime(getDataInicio(data));
		fim.set(Calendar.MILLISECOND, 999);
		fim.set(Calendar.SECOND, 59);
		fim.set(Calendar.MINUTE, 59);
		fim.set(Calendar.HOUR, 23);
		fim.set(Calendar.DATE, 21);
		fim.add(Calendar.MONTH, 1);
		// System.out.println("Data Fim = " + new SimpleDateFormat("dd/MM/yyyy
		// HH:mm:ss.SSS").format(fim.getTime()));
		return fim.getTime();
	}

	private Date getDataInicio() {
		return getDataInicio(new Date());
	}

	private Date getDataFim() {
		return getDataFim(new Date());
	}

}