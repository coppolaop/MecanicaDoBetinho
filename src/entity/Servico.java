package entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.IndexColumn;

@Entity
public class Servico implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer idServico;
	private String nome;
	private Double valor;
	private Integer previsao;
	
	@OneToMany(mappedBy="servico",fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@IndexColumn(name = "idItemServico")
	private List<ItemServico> itensServico;
	
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="id_especialidade")
//	@IndexColumn(name = "id_especialidade")
//	private Especialidade especialidade;
	
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
		return "<td>" + nome
				+ "</td><td>" + valor + "</td><td>" + previsao + "</td>";
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

//	public Especialidade getEspecialidade() {
//		return especialidade;
//	}
//
//	public void setEspecialidade(Especialidade especialidade) {
//		this.especialidade = especialidade;
//	}
	
	public void adicionar(ItemServico i){
		if(itensServico == null){
			itensServico = new ArrayList<ItemServico>();
		}
		itensServico.add(i);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public static void main(String[] args) {
		Peca s = new Peca();
		String x = "";
		Class obj = Servico.class;
		for (Field atributo : obj.getDeclaredFields()) {
        	if(!(atributo.getName().startsWith("id"))){
        		if(!(atributo.getName().equals("serialVersionUID"))){
        			x=atributo.getAnnotations().toString();
        			if((atributo.getAnnotations().toString().equals(x))){
	        			try {
							System.out.println(atributo.getName());
							
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        			}
        		}
        	}
		}
	}
}
