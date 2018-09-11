package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgendaPortaoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer ano;
	private List<SemanaAgendaPortaoModel> semanas = new ArrayList<>();

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<SemanaAgendaPortaoModel> getSemanas() {
		return semanas;
	}

	public void setSemanas(List<SemanaAgendaPortaoModel> semanas) {
		this.semanas = semanas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((semanas == null) ? 0 : semanas.hashCode());
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
		AgendaPortaoModel other = (AgendaPortaoModel) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (semanas == null) {
			if (other.semanas != null)
				return false;
		} else if (!semanas.equals(other.semanas))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AgendaPortaoModel [ano=" + ano + ", semanas=" + semanas + "]";
	}

}