package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.Date;

public class AreaCortadaModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Date data = new Date();
	private FuncionarioModel funcionario;
	private SetorModel setor;
	private Float area;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public FuncionarioModel getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioModel funcionario) {
		this.funcionario = funcionario;
	}

	public SetorModel getSetor() {
		return setor;
	}

	public void setSetor(SetorModel setor) {
		this.setor = setor;
	}

	public Float getArea() {
		return area;
	}

	public void setArea(Float area) {
		this.area = area;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		AreaCortadaModel other = (AreaCortadaModel) obj;
		return id != other.id;
	}

	@Override
	public String toString() {
		return "AreaCortadaModel [id=" + id + ", data=" + data + ", funcionarioModel=" + funcionario + ", setorModel="
				+ setor + ", area=" + area + "]";
	}

}