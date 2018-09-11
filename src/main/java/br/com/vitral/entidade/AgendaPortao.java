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

}