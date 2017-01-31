package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Peca implements Serializable,Cloneable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idPeca;
	private String nome;
	private Double valor;
	
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "pecas")
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
	
	public void adicionar(ItemServico i){
		if(itensServico==null){
			itensServico = new ArrayList<ItemServico>();
		}
		itensServico.add(i);
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
