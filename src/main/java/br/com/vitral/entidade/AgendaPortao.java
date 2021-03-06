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

@Table(name = "agendaportao")
@Entity

@NamedQueries({ @NamedQuery(name = "AgendaPortao.findAll", query = "SELECT a FROM AgendaPortao a ORDER BY a.ano DESC"),
		@NamedQuery(name = "AgendaPortao.findAnosDistintos", query = "SELECT a.ano FROM AgendaPortao a ORDER BY a.ano DESC") })

public class AgendaPortao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ano")
	private Integer ano;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("primeirodia ASC")
	private List<SemanaAgendaPortao> semanas;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public List<SemanaAgendaPortao> getSemanas() {
		return semanas;
	}

	public void setSemanas(List<SemanaAgendaPortao> semanas) {
		this.semanas = semanas;
	}

	@Override
	public String toString() {
		return "AgendaPortao [ano=" + ano + ", semanas=" + semanas + "]";
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
		AgendaPortao other = (AgendaPortao) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		return true;
	}

}