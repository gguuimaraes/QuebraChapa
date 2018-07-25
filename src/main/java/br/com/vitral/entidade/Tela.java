package br.com.vitral.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import br.com.vitral.modelo.UsuarioModel;

@Table(name = "tela")
@Entity

@NamedQueries({

		@NamedQuery(name = "Tela.findTela", query = "SELECT t FROM Tela t WHERE t.url = :url"),
		@NamedQuery(name = "Tela.findAll", query = "SELECT t FROM Tela t")

})
public class Tela implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "url", unique = true)
	private String url;

	@Column(name = "segundos")
	private Integer segundos;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSegundos() {
		return segundos;
	}

	public void setSegundos(Integer segundos) {
		this.segundos = segundos;
	}
}