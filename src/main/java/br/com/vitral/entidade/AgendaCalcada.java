package br.com.vitral.entidade;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Table(name = "agendacalcada")
@Entity

@NamedQueries({
		@NamedQuery(name = "AgendaCalcada.findAll", query = "SELECT a FROM AgendaCalcada a ORDER BY a.ano DESC"),
		@NamedQuery(name = "AgendaCalcada.findAnosDistintos", query = "SELECT a.ano FROM AgendaCalcada a ORDER BY a.ano DESC") })

public class AgendaCalcada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ano")
	private Integer ano;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("data ASC")
	private List<DiaAgendaCalcada> dias;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<DiaAgendaCalcada> getDias() {
		return dias;
	}

	public void setDias(List<DiaAgendaCalcada> dias) {
		this.dias = dias;
	}

	@Override
	public String toString() {
		return "AgendaCalcada [ano=" + ano + ", dias=" + dias + "]";
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
		AgendaCalcada other = (AgendaCalcada) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		return true;
	}

}