package entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Usuario extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idUsuario;
	private String username;
	private String senha;
	
	public Usuario() {
		
	}

	public Usuario(Integer idUsuario, String username, String senha, String nome, String email, Long cpf,
			Long telefone, Long celular, Endereco endereco) {
		super(nome, email, cpf, telefone, celular, endereco);
		this.idUsuario = idUsuario;
		this.username = username;
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Usuario [username=" + username + ", senha=" + senha + "]";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
