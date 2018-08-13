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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "areacortada")
@Entity

@NamedQueries({ @NamedQuery(name = "AreaCortada.findAll", query = "SELECT a FROM AreaCortada a ORDER BY a.data DESC"),
	@NamedQuery(name = "AreaCortada.findAreasCortadasDoDia", query = "SELECT a FROM AreaCortada a WHERE a.data = :data ORDER BY a.area DESC") })
public class AreaCortada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "data")
	@Temporal(TemporalType.DATE)
	private Date data;

	@OneToOne
	private Funcionario funcionario;

	@OneToOne
	private Setor setor;

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

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public float getArea() {
		return area;
	}

	public void setArea(float area) {
		this.area = area;
	}

}