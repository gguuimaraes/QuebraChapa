package br.com.vitral.entidade;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "agendaportao")
@Entity

@NamedQueries({ @NamedQuery(name = "AgendaPortao.findAll", query = "SELECT a FROM AgendaPortao a ORDER BY a.id DESC"),
		@NamedQuery(name = "AgendaPortao.findAnosDistintos", query = "SELECT DISTINCT(a.id.ano) FROM AgendaPortao a ORDER BY a.id.ano DESC") })

public class AgendaPortao implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AgendaPortaoId id;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DiaAgendaPortao> dias;

	public AgendaPortaoId getId() {
		return id;
	}

	public void setId(AgendaPortaoId id) {
		this.id = id;
	}

	public List<DiaAgendaPortao> getDias() {
		return dias;
	}

	public void setDias(List<DiaAgendaPortao> dias) {
		this.dias = dias;
	}
}