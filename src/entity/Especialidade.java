package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Especialidade implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer idEspecialidade;
	private String nome;
	
	@OneToMany(mappedBy="especialidade",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Servico> servico;
	
	@ManyToMany(mappedBy="especialidades",fetch=FetchType.EAGER)
	@IndexColumn(name="idEspecialidade")
	private List<Mecanico> mecanicos;
	
	public Especialidade() {
		
	}

	public Especialidade(Integer idEspecialidade, String nome) {
		super();
		this.idEspecialidade = idEspecialidade;
		this.nome = nome;
	}

	@Override
	public String toString() {
		return "Especialidade [idEspecialidade=" + idEspecialidade + ", nome="
				+ nome + "]";
	}

	public Integer getIdEspecialidade() {
		return idEspecialidade;
	}

	public void setIdEspecialidade(Integer idEspecialidade) {
		this.idEspecialidade = idEspecialidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Servico> getServico() {
		return servico;
	}

	public void setServico(List<Servico> servico) {
		this.servico = servico;
	}

	public List<Mecanico> getMecanicos() {
		return mecanicos;
	}

	public void setMecanicos(List<Mecanico> mecanicos) {
		this.mecanicos = mecanicos;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
