package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.bcel.internal.generic.IfInstruction;

import persistence.GenericDao;
import entity.ItemServico;
import entity.OrdemDeServico;

/**
 * Servlet implementation class InfoOrdem
 */
@WebServlet("/InfoOrdem")
public class InfoOrdem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InfoOrdem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(request.getParameter("id"));
		Integer id = Integer.parseInt(request.getParameter("id"));
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
        pw.println("<li><i class=\"fa fa-wrench\"></i>Serviços Alocados</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Itens de Serviço");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-wrench\"></i> Servico</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Adição</th>");
        pw.println("<th><i class=\"fa fa-cog\" aria-hidden=\"true\"></i> Quantidade de Peças</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Atual</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Mecanico Responsavel</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	OrdemDeServico o = od.findById(id,OrdemDeServico.class);
        	List<ItemServico> lista = o.getItensServico();
        	
        	System.out.println(lista);
	        for(ItemServico i : lista){
	        
	        	if(!(i == null)){
	        	
			        pw.println("<tr>");
			        pw.println("<td>"+i.getServico().getNome()+"</td>");
			        pw.println("<td>"+i.getDataAdicao()+"</td>");
			        pw.println("<td>"+i.getPecas().size()+"</td>");
			        NumberFormat nf = NumberFormat.getInstance();
			        nf.setMaximumFractionDigits(2);
			        nf.setMinimumFractionDigits(2);
			        pw.println("<td>R$ "+nf.format(i.getValor())+"</td>");
			        pw.println("<td>"+i.getMecanico().getNome()+"</td>");
			        pw.println("<td>");
			        pw.println("<div class=\"btn-group\">");
			        pw.println("<a class=\"btn btn-info\" href=#><i class=\"icon_info_alt\"></i></a>");
			        pw.println("</div>");
			        pw.println("</td>");
			
			        pw.println("</tr>");
	        	}
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
