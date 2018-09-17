package br.com.vitral.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Table(name = "acidente")
@Entity

@NamedQueries({

		// @NamedQuery(name = "Acidente.findAcidente", query = "SELECT a FROM Acidente a
		// WHERE a.id = :a"),
		@NamedQuery(name = "Acidente.findAll", query = "SELECT a FROM Acidente a"),
		@NamedQuery(name = "Acidente.findDatasOrdenadas", query = "SELECT a.data FROM Acidente a ORDER BY a.data"),
		@NamedQuery(name = "Acidente.findDataMaisRecente", query = "SELECT MAX(a.data) FROM Acidente a"),
		@NamedQuery(name = "Acidente.findAcidenteMaisAntigo", query = "SELECT MIN(a.data) FROM Acidente a")

})
public class Acidente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@OneToOne
	private Funcionario funcionario;

	@Column(name = "obs")
	private String obs;

	@Column(name = "data")
	private Date data;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

}