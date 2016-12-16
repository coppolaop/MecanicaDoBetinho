package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
public class Cliente extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idCliente;
	
	@OneToMany(mappedBy="cliente",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Veiculo> veiculos;

	public Cliente() {
		
	}

	public Cliente(Integer idCliente, List<Veiculo> veiculos, String nome, String email, Long cpf,
			Long telefone, Long celular, Endereco endereco) {
		super(nome, email, cpf, telefone, celular, endereco);
		this.idCliente = idCliente;
		this.veiculos = veiculos;
	}

	@Override
	public String toString() {
		return "Cliente [idCliente=" + idCliente + ", veiculos=" + veiculos
				+ "]";
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> veiculos) {
		this.veiculos = veiculos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
//	@Id
//	private Integer idCliente;
//	private String nome;
//	@Column(length=11)//codigo de area + numero
//	private Integer telefone;
//	private Endereco endereco;
//	
//	public Cliente() {
//		
//	}
//
//	public Cliente(Integer idCliente, String nome, Integer telefone,
//			Endereco endereco) {
//		super();
//		this.idCliente = idCliente;
//		this.nome = nome;
//		this.telefone = telefone;
//		this.endereco = endereco;
//	}
//
//	@Override
//	public String toString() {
//		return "Cliente [idCliente=" + idCliente + ", nome=" + nome
//				+ ", telefone=" + telefone + ", endereco=" + endereco + "]";
//	}
//
//	public Integer getIdCliente() {
//		return idCliente;
//	}
//
//	public void setIdCliente(Integer idCliente) {
//		this.idCliente = idCliente;
//	}
//
//	public String getNome() {
//		return nome;
//	}
//
//	public void setNome(String nome) {
//		this.nome = nome;
//	}
//
//	public Integer getTelefone() {
//		return telefone;
//	}
//
//	public void setTelefone(Integer telefone) {
//		this.telefone = telefone;
//	}
//
//	public Endereco getEndereco() {
//		return endereco;
//	}
//
//	public void setEndereco(Endereco endereco) {
//		this.endereco = endereco;
//	}
}
