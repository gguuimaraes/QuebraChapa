package br.com.vitral.modelo;

import java.io.Serializable;
import java.util.Date;

public class QuebraModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int id;
	private Date data;
	private TipoVidroModel tipoVidro;
	private Float areaTotal;
	private Float areaAproveitada = 0f;
	private SetorModel setor;
	private FuncionarioModel funcionario;
	private String motivo;

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

	public TipoVidroModel getTipoVidro() {
		return tipoVidro;
	}

	public void setTipoVidro(TipoVidroModel tipoVidro) {
		this.tipoVidro = tipoVidro;
	}

	public Float getAreaTotal() {
		return areaTotal;
	}

	public void setAreaTotal(Float areaTotal) {
		this.areaTotal = areaTotal;
	}

	public Float getAreaAproveitada() {
		return areaAproveitada;
	}

	public void setAreaAproveitada(Float areaAproveitada) {
		this.areaAproveitada = areaAproveitada;
	}

	public SetorModel getSetor() {
		return setor;
	}

	public void setSetor(SetorModel setor) {
		this.setor = setor;
	}

	public FuncionarioModel getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioModel funcionario) {
		this.funcionario = funcionario;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
		QuebraModel other = (QuebraModel) obj;
		return id != other.id;
	}

	@Override
	public String toString() {
		return "QuebraModel [id=" + id + ", data=" + data + ", tipoVidro=" + tipoVidro + ", areaTotal=" + areaTotal
				+ ", areaAproveitada=" + areaAproveitada + ", setor=" + setor + ", funcionario=" + funcionario
				+ ", motivo=" + motivo + "]";
	}

}