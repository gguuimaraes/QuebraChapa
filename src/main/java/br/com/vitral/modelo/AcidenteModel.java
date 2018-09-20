package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.Date;

public class AcidenteModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String obs;
	private Date data;
	private FuncionarioModel funcionario;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public FuncionarioModel getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioModel funcionario) {
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

	@Override
	public String toString() {
		return "AcidenteModel [id=" + id + ", obs=" + obs + ", data=" + data + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AcidenteModel other = (AcidenteModel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}