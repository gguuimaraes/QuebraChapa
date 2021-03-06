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

@Table(name = "peso")
@Entity

@NamedQueries({ @NamedQuery(name = "Peso.findAll", query = "SELECT p FROM Peso p ORDER BY p.data DESC"),
		@NamedQuery(name = "Peso.findPesosDoDia", query = "SELECT p FROM Peso p WHERE p.data = :data ORDER BY p.peso DESC"),
		@NamedQuery(name = "Peso.findPesoTotalSetorDia", query = "SELECT SUM(p.peso) FROM Peso p WHERE p.setor.id = :setorId AND p.data = :data"),
		@NamedQuery(name = "Peso.findPesosPorSetorDia", query = "SELECT p FROM Peso p WHERE p.setor.id = :setorId AND p.data = :data ORDER BY p.peso DESC"),
		@NamedQuery(name = "Peso.findPesoTotalDia", query = "SELECT SUM(p.peso) FROM Peso p WHERE p.data = :data"),
		@NamedQuery(name = "Peso.findPesoTotalPeriodo", query = "SELECT SUM(p.peso) FROM Peso p WHERE p.data >= :dataInicio AND p.data <= :dataFim") })

public class Peso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne
	private Funcionario funcionario;

	@OneToOne
	private Setor setor;

	@Column(name = "data")
	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(name = "peso")
	private float peso;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

}