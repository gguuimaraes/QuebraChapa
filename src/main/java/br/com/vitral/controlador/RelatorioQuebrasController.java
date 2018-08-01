package br.com.vitral.controlador;

import java.io.Serializable;
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
		quebraPorSetor.setTitle("Quebra por Setor");
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
		quebraAnual.setTitle("Quebra Anual");
		quebraAnual.setShowPointLabels(true);
		quebraAnual.setExtender("quebraAnualExt");
		DateAxis eixoX = new DateAxis();
		eixoX.setTickFormat("%b");
		quebraAnual.getAxes().put(AxisType.X, eixoX);
		Axis eixoY = quebraAnual.getAxis(AxisType.Y);
		eixoY.setTickFormat("%.4f");
		eixoY.setMin(0);

		LineChartSeries quebra = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			c.set(Calendar.DATE, 21);
			Double areaQuebra = quebraDao.areaPorPeriodo(getDataInicio(c.getTime()), getDataFim(c.getTime()));
			System.out.printf("Quebra do periodo %02d/%02d/%d: %.4f\n", c.get(Calendar.DATE), c.get(Calendar.MONTH) + 1,
					c.get(Calendar.YEAR), areaQuebra);
			if (areaQuebra != null)
				quebra.set(c.getTimeInMillis(), areaQuebra);
			else
				i = 12;
		}
		quebraAnual.addSeries(quebra);
		

		
		/*LineChartSeries faturado = new LineChartSeries();
		for (int i = 0; i < 12; i++) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.MONTH, i * -1);
			c.set(Calendar.DATE, 21);
			Float areaFaturada = areaFaturadaDao.maiorAreaPorPeriodo(getDataInicio(c.getTime()),
					getDataFim(c.getTime()));
			if (areaFaturada != null)
				faturado.set(c.getTimeInMillis(), areaFaturada);
			else
				i = 12;
		}
		quebraAnual.addSeries(faturado);*/
		
		return quebraAnual;
	}

	public Float getAreaFaturada() {
		return areaFaturadaDao.maiorAreaPorPeriodo(getDataInicio(), getDataFim());
	}

	public Double getAreaQuebra() {
		return quebraDao.areaPorPeriodo(getDataInicio(), getDataFim());
	}

	/*
	 * private void calculaPeriodo() { cInicio = Calendar.getInstance();
	 * cInicio.setTime(new Date());
	 * 
	 * cFim = Calendar.getInstance();
	 * 
	 * cInicio.set(Calendar.MILLISECOND, 0); cInicio.set(Calendar.SECOND, 0);
	 * cInicio.set(Calendar.MINUTE, 0); cInicio.set(Calendar.HOUR, 12); if
	 * (cInicio.get(Calendar.DATE) <= 21) { cInicio.add(Calendar.MONTH, -1); }
	 * cInicio.set(Calendar.DATE, 22); cFim.setTime(cInicio.getTime());
	 * cFim.set(Calendar.DATE, 21); cFim.add(Calendar.MONTH, 1); }
	 */

	private Date getDataInicio(Date data) {
		Calendar inicio = Calendar.getInstance();
		inicio.setTime(data);
		inicio.set(Calendar.MILLISECOND, 0);
		inicio.set(Calendar.SECOND, 0);
		inicio.set(Calendar.MINUTE, 0);
		inicio.set(Calendar.HOUR, 12);
		if (inicio.get(Calendar.DATE) <= 21) {
			inicio.add(Calendar.MONTH, -1);
		}
		inicio.set(Calendar.DATE, 22);
		return inicio.getTime();
	}

	private Date getDataFim(Date data) {
		Calendar fim = Calendar.getInstance();
		fim.setTime(getDataInicio(data));
		fim.set(Calendar.DATE, 21);
		fim.add(Calendar.MONTH, 1);
		return fim.getTime();
	}

	private Date getDataInicio() {
		return getDataInicio(new Date());
	}

	private Date getDataFim() {
		Calendar fim = Calendar.getInstance();
		fim.setTime(getDataInicio());
		fim.set(Calendar.DATE, 21);
		fim.add(Calendar.MONTH, 1);
		return fim.getTime();
	}

}