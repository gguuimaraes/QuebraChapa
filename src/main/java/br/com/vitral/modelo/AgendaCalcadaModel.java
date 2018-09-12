package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgendaCalcadaModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer ano;
	private List<DiaAgendaCalcadaModel> dias = new ArrayList<>();

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<DiaAgendaCalcadaModel> getDias() {
		return dias;
	}

	public void setDias(List<DiaAgendaCalcadaModel> dias) {
		this.dias = dias;
	}

	@Override
	public String toString() {
		return "AgendaCalcadaModel [ano=" + ano + ", dias=" + dias + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
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
		AgendaCalcadaModel other = (AgendaCalcadaModel) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		return true;
	}

}