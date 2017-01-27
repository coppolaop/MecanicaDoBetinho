package entity;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.hibernate.annotations.IndexColumn;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE")
public class Cliente extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idCliente;
	
	@OneToMany(mappedBy="cliente",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name = "idVeiculo")
	private List<Veiculo> veiculos;

	public Cliente() {
		
	}

	public Cliente(String nome, String email, Long cpf, Long telefone,
			Long celular, Integer idCliente) {
		super(nome, email, cpf, telefone, celular);
		this.idCliente = idCliente;
	}
	
	@Override
	public String toString() {
		return "<td>" + getNome()
				+ "</td><td>" + getEmail() + "</td><td>" + getCpf()
				+ "</td><td>" + getTelefone() + "</td><td>"
				+ getCelular() + "</td>";
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
	
	public void adicionar(Veiculo v){
		if(veiculos == null){
			veiculos = new ArrayList<Veiculo>();
		}
		veiculos.add(v);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}