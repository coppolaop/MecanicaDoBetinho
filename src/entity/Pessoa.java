package entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public abstract class Pessoa implements Serializable, Cloneable{

	private static final long serialVersionUID = 1L;
	
	@Column(unique=true)
	private String nome;
	private String email;
	@Column(length=11)
	private Long cpf;
	@Column(length=10)//codigo de area + numero
	private Long telefone;
	@Column(length=11)//codigo de area + 9 + numero
	private Long celular;
	
	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "id_endereco") 
	private Endereco endereco;
	
	public Pessoa() {
		
	}

	public Pessoa(String nome, String email, Long cpf, Long telefone,
			Long celular) {
		super();
		this.nome = nome;
		this.email = email;
		this.cpf = cpf;
		this.telefone = telefone;
		this.celular = celular;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public Long getTelefone() {
		return telefone;
	}

	public void setTelefone(Long telefone) {
		this.telefone = telefone;
	}

	public Long getCelular() {
		return celular;
	}

	public void setCelular(Long celular) {
		this.celular = celular;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Pessoa [nome=" + nome + ", email=" + email + ", cpf=" + cpf
				+ ", telefone=" + telefone + ", celular=" + celular
				+ ", endereco=" + endereco + "]";
	}

	//Design Pattern - Prototype
	public Object clone() throws CloneNotSupportedException{
		Object clone = null;
		
		try{
			clone = super.clone();
		}catch(CloneNotSupportedException ex){
			ex.printStackTrace();
		}
		return clone;
	}
}
