package br.com.vitral.entidade;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "agendaportao")
@Entity

@NamedQueries({ @NamedQuery(name = "AgendaPortao.findAll", query = "SELECT a FROM AgendaPortao a ORDER BY a.id DESC") })

public class AgendaPortao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "mes")
	private int mes;

	@Column(name = "ano")
	private int ano;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DiaAgendaPortao> dias;

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

	public List<DiaAgendaPortao> getDias() {
		return dias;
	}

	public void setDias(List<DiaAgendaPortao> dias) {
		this.dias = dias;
	}
}