package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgendaPortaoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer mes;
	private Integer ano;
	private List<DiaAgendaPortaoModel> dias = new ArrayList<>();

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<DiaAgendaPortaoModel> getDias() {
		return dias;
	}

	public void setDias(List<DiaAgendaPortaoModel> dias) {
		this.dias = dias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((mes == null) ? 0 : mes.hashCode());
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
		if (mes == null) {
			if (other.mes != null)
				return false;
		} else if (!mes.equals(other.mes))
			return false;
		return true;
	}

	

}