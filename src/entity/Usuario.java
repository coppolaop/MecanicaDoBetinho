package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Usuario extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idUsuario;
	@Column(unique=true)
	private String username;
	private String senha;
	@Column(length=3)
	private String perfil;
	
	@OneToMany(mappedBy="cliente",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name = "idVeiculo")
	private List<Veiculo> veiculos;
	
	public Usuario() {
		
	}

	public Usuario(Integer idUsuario, String nome, String email, Long cpf, Long telefone,
			Long celular, String username, String senha) {
		super(nome, email, cpf, telefone, celular);
		this.idUsuario = idUsuario;
		this.username = username;
		this.senha = senha;
	}

	@Override
	public String toString() {
		return "Usuario [idUsuario=" + idUsuario + ", username=" + username
				+ ", Nome=" + getNome() + ", Email=" + getEmail()
				+ ", Cpf=" + getCpf() + ", Telefone=" + getTelefone()
				+ ", Celular=" + getCelular() + ", Endereco="
				+ getEndereco() + "]";
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
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

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}
	
	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}
	
	public void adicionar(Veiculo v){
		if(veiculos == null){
			veiculos = new ArrayList<Veiculo>();
		}
		veiculos.add(v);
	}
	
	public void remover(Veiculo v){
		if(veiculos!=null){
			if(veiculos.contains(v)){
				veiculos.remove(v);
			}
		}
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
