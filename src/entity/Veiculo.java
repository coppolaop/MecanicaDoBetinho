package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Veiculo implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idVeiculo;
	private String placa;
	private String descricao;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="id_cliente")
	@IndexColumn(name = "id_cliente")
	private Usuario cliente;
	
	@OneToMany(mappedBy="veiculo",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name = "idOrdemDeServico")
	private List<OrdemDeServico> ordensDeServico;
	
	public Veiculo() {
		
	}

	public Veiculo(Integer idVeiculo, String placa, String descricao) {
		super();
		this.idVeiculo = idVeiculo;
		this.placa = placa;
		this.descricao = descricao;
	}
	
	@Override
	public String toString() {
		return "Veiculo [idVeiculo=" + idVeiculo + ", placa=" + placa
				+ ", descricao=" + descricao + "]";
	}

	public Integer getIdVeiculo() {
		return idVeiculo;
	}

	public void setIdVeiculo(Integer idVeiculo) {
		this.idVeiculo = idVeiculo;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Usuario getCliente() {
		return cliente;
	}

	public void setCliente(Usuario cliente) {
		this.cliente = cliente;
	}

	public List<OrdemDeServico> getOrdensDeServico() {
		return ordensDeServico;
	}

	public void setOrdensDeServico(List<OrdemDeServico> ordensDeServico) {
		this.ordensDeServico = ordensDeServico;
	}

	public void adicionar(OrdemDeServico o){
		if(ordensDeServico==null){
			ordensDeServico = new ArrayList<OrdemDeServico>();
		}
		ordensDeServico.add(o);
	}
	
	public void remover(OrdemDeServico o){
		if(ordensDeServico!=null){
			if(ordensDeServico.contains(o)){
				ordensDeServico.remove(o);
			}
		}
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
