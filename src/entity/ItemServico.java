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

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.IndexColumn;

@Entity
public class ItemServico implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idItemServico;
	private Double valor;
	private Date dataAdicao;
	private Boolean autorizacao;
	
	@ManyToMany
	@JoinTable	(	name="itemservico_ordemdeservico",
					joinColumns=@JoinColumn(name="id_itemservico"),
					inverseJoinColumns=@JoinColumn(name="id_ordemdeservico")
				)
	private List<OrdemDeServico> OrdensDeServico;
	
	@CollectionOfElements(fetch = FetchType.EAGER)
	@IndexColumn(name = "peca")
	private List<Peca> pecas;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_mecanico")
	private Mecanico mecanico;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_servico")
	private Servico servico;
	
	public ItemServico() {
		
	}

	public ItemServico(Integer idItemServico, Double valor, Date dataAdicao,
			Boolean autorizacao) {
		super();
		this.idItemServico = idItemServico;
		this.valor = valor;
		this.dataAdicao = dataAdicao;
		this.autorizacao = autorizacao;
	}

	@Override
	public String toString() {
		return "ItemServico [idItemServico=" + idItemServico + ", valor="
				+ valor + ", dataAdicao=" + dataAdicao + ", autorizacao="
				+ autorizacao + "]";
	}

	public Integer getIdItemServico() {
		return idItemServico;
	}

	public void setIdItemServico(Integer idItemServico) {
		this.idItemServico = idItemServico;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataAdicao() {
		return dataAdicao;
	}

	public void setDataAdicao(Date dataAdicao) {
		this.dataAdicao = dataAdicao;
	}

	public Boolean getAutorizacao() {
		return autorizacao;
	}

	public void setAutorizacao(Boolean autorizacao) {
		this.autorizacao = autorizacao;
	}

	public List<OrdemDeServico> getOrdensDeServico() {
		return OrdensDeServico;
	}

	public void setOrdensDeServico(List<OrdemDeServico> ordensDeServico) {
		OrdensDeServico = ordensDeServico;
	}

	public List<Peca> getPecas() {
		return pecas;
	}

	public void setPecas(List<Peca> pecas) {
		this.pecas = pecas;
	}

	public Mecanico getMecanico() {
		return mecanico;
	}

	public void setMecanico(Mecanico mecanico) {
		this.mecanico = mecanico;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}