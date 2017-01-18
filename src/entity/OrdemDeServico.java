package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.IndexColumn;

@Entity
public class OrdemDeServico implements Serializable, Cloneable, OrdemState{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idOrdemDeServico;
	private Date dataEmissao;
	private Double valor;
	private Date dataConclusao;
	@Column(length=7)
	private String status;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_veiculo")
	private Veiculo veiculo;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable	(	name="itemservico_ordemdeservico",
					joinColumns=@JoinColumn(name="id_itemservico"),
					inverseJoinColumns=@JoinColumn(name="id_ordemdeservico")
				)
	private List<ItemServico> itensServico;
	
	public OrdemDeServico() {
		this.status = "ativo";
	}

	public OrdemDeServico(Integer idOrdemDeServico, Date dataEmissao,
			Double valor, String status) {
		super();
		this.idOrdemDeServico = idOrdemDeServico;
		this.dataEmissao = dataEmissao;
		this.valor = valor;
		this.status = status;
	}

	@Override
	public String toString() {
		return "OrdemDeServico [idOrdemDeServico=" + idOrdemDeServico
				+ ", dataEmissao=" + dataEmissao + ", valor=" + valor
				+ ", dataConclusao=" + dataConclusao + ", status=" + status
				+ "]";
	}

	public Integer getIdOrdemDeServico() {
		return idOrdemDeServico;
	}

	public void setIdOrdemDeServico(Integer idOrdemDeServico) {
		this.idOrdemDeServico = idOrdemDeServico;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(Date dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
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

	//Design Pattern - State
	@Override
	public void changeState() {
		if(status.equals("ativo")){
			status = "inativo";
		}else{
			status = "ativo";
		}
	}
}