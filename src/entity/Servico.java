package entity;

import java.io.Serializable;
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

@Entity
public class Servico implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idServico;
	private String nome;
	private Double valor;
	private Integer previsao;
	
	@OneToMany(mappedBy="servico",fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	private List<ItemServico> itensServico;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="id_especialidade")
	private Servico especialidade;
	
	public Servico() {
		
	}
	
	public Servico(Integer idServico, String nome, Double valor,
			Integer previsao) {
		super();
		this.idServico = idServico;
		this.nome = nome;
		this.valor = valor;
		this.previsao = previsao;
	}
	
	@Override
	public String toString() {
		return "Servico [idServico=" + idServico + ", nome=" + nome
				+ ", valor=" + valor + ", previsao=" + previsao + "]";
	}
	
	public Integer getIdServico() {
		return idServico;
	}
	
	public void setIdServico(Integer idServico) {
		this.idServico = idServico;
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
	
	public Integer getPrevisao() {
		return previsao;
	}
	
	public void setPrevisao(Integer previsao) {
		this.previsao = previsao;
	}
	
	public List<ItemServico> getItensServico() {
		return itensServico;
	}

	public void setItensServico(List<ItemServico> itensServico) {
		this.itensServico = itensServico;
	}

	public Servico getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(Servico especialidade) {
		this.especialidade = especialidade;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
