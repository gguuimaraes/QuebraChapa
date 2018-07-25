package br.com.vitral.modelo;

import java.io.Serializable;

import br.com.vitral.entidade.Usuario.TipoUsuario;

public class UsuarioModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nome;
	private String senha;
	private TipoUsuario tipo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public TipoUsuario getTipo() {
		return tipo;
	}

	public void setTipo(TipoUsuario tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "UsuarioModel [id=" + id + ", nome=" + nome + ", senha=" + senha + ", tipo=" + tipo + "]";
	}
	
	
}