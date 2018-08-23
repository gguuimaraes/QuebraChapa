package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AgendaPortaoModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private int mes;
	private int ano;
	private List<DiaAgendaPortaoModel> dias = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public List<DiaAgendaPortaoModel> getDias() {
		return dias;
	}

	public void setDias(List<DiaAgendaPortaoModel> dias) {
		this.dias = dias;
	}

	@Override
	public String toString() {
		return "AgendaPortaoModel [id=" + id + ", mes=" + mes + ", ano=" + ano + ", dias=" + dias + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ano;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + mes;
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
		if (ano != other.ano)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return mes == other.mes;
	}
}