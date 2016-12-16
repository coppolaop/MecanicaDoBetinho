package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
public class OrdemDeServico implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idOrdemDeServico;
	private Date dataEmissao;
	private Double valor;
	private Date dataConclusao;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_veiculo")
	private Veiculo veiculo;
	
	//mappedBy="ordensDeServico",
	@ManyToMany(fetch=FetchType.LAZY)
	@IndexColumn(name="idOrdemDeServico")
	private List<OrdemDeServico> itensServico;
	
	public OrdemDeServico() {
		
	}

	public OrdemDeServico(Integer idOrdemDeServico, Date dataEmissao,
			Double valor, Date dataConclusao) {
		super();
		this.idOrdemDeServico = idOrdemDeServico;
		this.dataEmissao = dataEmissao;
		this.valor = valor;
		this.dataConclusao = dataConclusao;
	}

	@Override
	public String toString() {
		return "OrdemDeServico [idOrdemDeServico=" + idOrdemDeServico
				+ ", dataEmissao=" + dataEmissao + ", valor=" + valor
				+ ", dataConclusao=" + dataConclusao + "]";
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

	public Veiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	public List<OrdemDeServico> getItensServico() {
		return itensServico;
	}

	public void setItensServico(List<OrdemDeServico> itensServico) {
		this.itensServico = itensServico;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}