package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Peca implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer idPeca;
	private String nome;
	private Double valor;
	
	@ManyToMany
	@JoinTable	(	name="itemservico_peca",
					joinColumns=@JoinColumn(name="id_itemservico"),
					inverseJoinColumns=@JoinColumn(name="id_peca")
				)
	private List<ItemServico> itensServico;
	
	public Peca() {
		
	}

	public Peca(Integer idPeca, String nome, Double valor) {
		super();
		this.idPeca = idPeca;
		this.nome = nome;
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Peca [idPeca=" + idPeca + ", nome=" + nome + ", valor=" + valor
				+ "]";
	}

	public Integer getIdPeca() {
		return idPeca;
	}

	public void setIdPeca(Integer idPeca) {
		this.idPeca = idPeca;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public List<ItemServico> getItensServico() {
		return itensServico;
	}

	public void setItensServico(List<ItemServico> itensServico) {
		this.itensServico = itensServico;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
