package br.com.vitral.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "areafaturada")
@Entity

@NamedQueries({ @NamedQuery(name = "AreaFaturada.findAll", query = "SELECT a FROM AreaFaturada a ORDER BY a.data DESC"),
		@NamedQuery(name = "AreaFaturada.findMaiorAreaPorPeriodo", query = "SELECT MAX(a.area) FROM AreaFaturada a WHERE a.data >= :dataInicio AND a.data <= :dataFim") })
public class AreaFaturada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "data", unique = true)
	private Date data;

	@Column(name = "area")
	private float area;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}
}