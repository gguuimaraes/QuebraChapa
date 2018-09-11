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

@Table(name = "semanaagendaportao")
@Entity

public class SemanaAgendaPortao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "primeirodia")
	@Temporal(TemporalType.DATE)
	private Date primeiroDia;

	@Column(name = "ultimodia")
	@Temporal(TemporalType.DATE)
	private Date ultimoDia;

	@OneToOne
	private Funcionario abertura;

	@OneToOne
	private Funcionario fechamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPrimeiroDia() {
		return primeiroDia;
	}

	public void setPrimeiroDia(Date primeiroDia) {
		this.primeiroDia = primeiroDia;
	}

	public Date getUltimoDia() {
		return ultimoDia;
	}

	public void setUltimoDia(Date ultimoDia) {
		this.ultimoDia = ultimoDia;
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

}