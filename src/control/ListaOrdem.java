package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ItemServico;
import entity.OrdemDeServico;
import persistence.GenericDao;

/**
 * Servlet implementation class ListaOrdem
 */
@WebServlet("/ListaOrdem")
public class ListaOrdem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListaOrdem() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public Double calculaValor(OrdemDeServico o){
    	Double valor = 0.;
    	if(!(o.getItensServico()==null)){
	    	List<ItemServico> lst = o.getItensServico();
	    	System.out.println(lst);
	    	for(ItemServico i : lst){
	    		if(!(i==null)){
		    		if(!(i.getValor()==null)){
		    			valor += i.getValor();
	    			}
	    		}
    		}
    	}
    	o.setValor(valor);
    	try {
			GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
			OrdemDeServico ordem = (OrdemDeServico) o.clone();
    		od.update(ordem);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    	return valor;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ORDENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Ordens de Serviço</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Chamadas em Aberto");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"icon_profile\"></i> Cliente</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Inicio</th>");
        pw.println("<th><i class=\"fa fa-car\" aria-hidden=\"true\"></i> Veiculo</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Atual</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	List<OrdemDeServico> lst = od.findAll(OrdemDeServico.class);
        	List<OrdemDeServico> lista = new ArrayList<OrdemDeServico>();
        	
        	for(OrdemDeServico o : lst){
				if(!lista.contains(o)){
					lista.add(o);
				}
			}
        	
	        for(OrdemDeServico o : lista){
	        
	        	calculaValor(o);
	        	
		        pw.println("<tr>");
		        pw.println("<td>"+o.getVeiculo().getCliente().getNome()+"</td>");
		        pw.println("<td>"+o.getDataEmissao()+"</td>");
		        pw.println("<td>"+o.getVeiculo().getDescricao()+"</td>");
		        NumberFormat nf = NumberFormat.getInstance();
		        nf.setMaximumFractionDigits(2);
		        nf.setMinimumFractionDigits(2);
		        pw.println("<td>R$ "+nf.format(o.getValor())+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-primary\" href=\"./FormItem1?id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_plus\"></i></a>");
		        pw.println("<a class=\"btn btn-info\" href=\"./InfoOrdem?id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_info_alt\"></i></a>");
		        pw.println("<a class=\"btn btn-primary\" href=\"./EditaOrdem1?id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_pencil\"></i></a>");
		        if(o.getStatus().equals("ativo")){
		        	pw.println("<a class=\"btn btn-success\" href=\"./StateOrdem?id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_check_alt2\"></i></a>");	
		        }else{
		        	pw.println("<a class=\"btn btn-warning\" href=\"./StateOrdem?id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_check_alt2\"></i></a>");
		        }
		        pw.println("<a class=\"btn btn-danger\" href=\"./DeletaOrdem?id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_close_alt2\"></i></a>");
		        pw.println("</div>");
		        pw.println("</td>");
		
		        pw.println("</tr>");
	        
	        }
        } catch (Exception ex) {
			ex.printStackTrace();
		}

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");

        request.getRequestDispatcher("/base2.html").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
