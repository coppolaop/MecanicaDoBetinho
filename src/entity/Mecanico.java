package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Mecanico extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer idMecanico;
	
	@OneToMany(mappedBy="mecanico",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<ItemServico> itensServico;
	
	@ManyToMany
	@JoinTable	(	name="especialidade_mecanico",
					joinColumns=@JoinColumn(name="id_especialidade"),
					inverseJoinColumns=@JoinColumn(name="id_mecanico")
				)
	private List<Especialidade> especialidades;

	public Mecanico() {
		super();
	}

	public Mecanico(Integer idMecanico, List<ItemServico> itensServico,
			List<Especialidade> especialidades, Integer idPessoa, String nome, String email, Integer cpf,
			Integer telefone, Integer celular, Endereco endereco) {
		super(idPessoa, nome, email, cpf, telefone, celular, endereco);
		this.idMecanico = idMecanico;
		this.itensServico = itensServico;
		this.especialidades = especialidades;
	}

	@Override
	public String toString() {
		return "Mecanico [idMecanico=" + idMecanico + ", itensServico="
				+ itensServico + ", especialidades=" + especialidades + "]";
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

	public List<Especialidade> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
//	@Id
//	private Integer idMecanico;
//	private String nome;
//	private Endereco endereco;
//	
//	public Mecanico() {
//		
//	}
//
//	public Mecanico(Integer idMecanico, String nome, Endereco endereco) {
//		super();
//		this.idMecanico = idMecanico;
//		this.nome = nome;
//		this.endereco = endereco;
//	}
//
//	@Override
//	public String toString() {
//		return "Mecanico [idMecanico=" + idMecanico + ", nome=" + nome
//				+ ", endereco=" + endereco + "]";
//	}
//
//	public Integer getIdMecanico() {
//		return idMecanico;
//	}
//
//	public void setIdMecanico(Integer idMecanico) {
//		this.idMecanico = idMecanico;
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
//	public Endereco getEndereco() {
//		return endereco;
//	}
//
//	public void setEndereco(Endereco endereco) {
//		this.endereco = endereco;
//	}
}
