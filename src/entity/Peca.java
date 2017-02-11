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

	@Override
	public boolean equals(Object o) {
		Peca p = (Peca) o;
		if(this.idPeca.equals(p.getIdPeca())&&this.nome.equals(p.getNome())&&this.valor.equals(p.getValor())){
			return true;
		}else{
			return false;
		}
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
	
	public void remover(ItemServico i){
		if(itensServico!=null){
			if(itensServico.contains(i)){
				itensServico.remove(i);
			}
		}
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
