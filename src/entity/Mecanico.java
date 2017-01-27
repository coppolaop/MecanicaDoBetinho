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
public class Mecanico extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idMecanico;
	
	@OneToMany(mappedBy="mecanico",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name = "idItemServico")
	private List<ItemServico> itensServico;

	public Mecanico() {
		super();
	}

	public Mecanico(Integer idMecanico, String nome, String email, Long cpf, Long telefone,
			Long celular) {
		super(nome, email, cpf, telefone, celular);
		this.idMecanico = idMecanico;
	}

	@Override
	public String toString() {
		return "Mecanico [idMecanico=" + idMecanico + ", itensServico="
				+ itensServico + ", Nome=" + getNome() + ", Email="
				+ getEmail() + ", Cpf=" + getCpf() + ", Telefone="
				+ getTelefone() + ", Celular=" + getCelular() + "]";
	}

	public Integer getIdMecanico() {
		return idMecanico;
	}

	public void setIdMecanico(Integer idMecanico) {
		this.idMecanico = idMecanico;
	}

	public List<ItemServico> getItensServico() {
		return itensServico;
	}

	public void setItensServico(List<ItemServico> itensServico) {
		this.itensServico = itensServico;
	}
	
	public void adicionar(ItemServico i){
		if(itensServico == null){
			itensServico = new ArrayList<ItemServico>();
		}
		itensServico.add(i);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}