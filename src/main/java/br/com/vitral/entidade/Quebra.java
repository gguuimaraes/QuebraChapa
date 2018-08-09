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

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;

@Table(name = "quebra")
@Entity

@NamedQueries({ @NamedQuery(name = "Quebra.findAll", query = "SELECT q FROM Quebra q ORDER BY q.data DESC"),
		@NamedQuery(name = "Quebra.findAreaPorPeriodo", query = "SELECT SUM(q.areaTotal) - SUM(q.areaAproveitada) FROM Quebra q WHERE q.data >= :dataInicio AND q.data <= :dataFim"),
		@NamedQuery(name = "Quebra.findQuebraPorSetorEPeriodo", query = "SELECT q.setor.id, SUM(q.areaTotal) - SUM(q.areaAproveitada) FROM Quebra q WHERE q.data >= :dataInicio AND q.data <= :dataFim GROUP BY q.setor.id")

})
public class Quebra implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "data")
	private Date data;

	@OneToOne
	private TipoVidro tipoVidro;

	@Column(name = "areaTotal")
	private float areaTotal;

	@Column(name = "areaAproveitada")
	private float areaAproveitada;

	@OneToOne
	private Setor setor;

	@OneToOne(optional = true)
	private Funcionario funcionario;

	@Column(name = "motivo")
	private String motivo;

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

	public TipoVidro getTipoVidro() {
		return tipoVidro;
	}

	public void setTipoVidro(TipoVidro tipoVidro) {
		this.tipoVidro = tipoVidro;
	}

	public float getAreaTotal() {
		return areaTotal;
	}

	public void setAreaTotal(float areaTotal) {
		this.areaTotal = areaTotal;
	}

	public float getAreaAproveitada() {
		return areaAproveitada;
	}

	public void setAreaAproveitada(float areaAproveitada) {
		this.areaAproveitada = areaAproveitada;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

}