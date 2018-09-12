package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.Date;

public class SemanaAgendaPortaoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Date primeiroDia;
	private Date ultimoDia;
	private FuncionarioModel abertura;
	private FuncionarioModel fechamento;

	public SemanaAgendaPortaoModel() {
	}

	public SemanaAgendaPortaoModel(Date primeiroDia, Date ultimoDia) {
		this.primeiroDia = primeiroDia;
		this.ultimoDia = ultimoDia;
	}

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

	public FuncionarioModel getAbertura() {
		return abertura;
	}

	public void setAbertura(FuncionarioModel abertura) {
		this.abertura = abertura;
	}

	public FuncionarioModel getFechamento() {
		return fechamento;
	}

	public void setFechamento(FuncionarioModel fechamento) {
		this.fechamento = fechamento;
	}

	@Override
	public String toString() {
		return "SemanaAgendaPortaoModel [primeiroDia=" + primeiroDia + ", ultimoDia=" + ultimoDia + ", abertura="
				+ abertura + ", fechamento=" + fechamento + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SemanaAgendaPortaoModel other = (SemanaAgendaPortaoModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}