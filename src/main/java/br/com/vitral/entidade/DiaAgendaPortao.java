package br.com.vitral.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name = "diaagendaportao")
@Entity

public class DiaAgendaPortao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "data", unique = true)
	@Temporal(TemporalType.DATE)
	private Date data;

	@OneToOne
	private Funcionario abertura;

	@OneToOne
	private Funcionario fechamento;

	@Column(name = "feriado")
	private boolean feriado;

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

	public Funcionario getAbertura() {
		return abertura;
	}

	public void setAbertura(Funcionario abertura) {
		this.abertura = abertura;
	}

	public Funcionario getFechamento() {
		return fechamento;
	}

	public void setFechamento(Funcionario fechamento) {
		this.fechamento = fechamento;
	}

	public boolean isFeriado() {
		return feriado;
	}

	public void setFeriado(boolean feriado) {
		this.feriado = feriado;
	}
}