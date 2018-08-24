package br.com.vitral.entidade;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AgendaPortaoId implements Serializable {
 
    @Column(name = "mes")
    private Integer mes;
 
    @Column(name = "ano")
    private Integer ano;
    
    public AgendaPortaoId() {
    }
 
    public AgendaPortaoId(Integer mes, Integer ano) {
        this.mes = mes;
        this.ano = ano;
    }
 
    public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgendaPortaoId)) return false;
        AgendaPortaoId that = (AgendaPortaoId) o;
        return Objects.equals(getMes(), that.getMes()) &&
                Objects.equals(getAno(), that.getAno());
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(getMes(), getAno());
    }
}