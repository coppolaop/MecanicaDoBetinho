package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.exception.ConstraintViolationException;

import persistence.GenericDao;
import entity.Cliente;
import entity.Endereco;
import entity.ItemServico;
import entity.OrdemDeServico;
import entity.Servico;
import entity.Usuario;
import entity.Veiculo;

/**
 * Servlet implementation class ControlePedidos
 */
@WebServlet("/cli/ControlePedido")
public class ControlePedido extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControlePedido() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(cmd.equalsIgnoreCase("aprovar")){
			doPost(request,response);
		}else if(cmd.equalsIgnoreCase("listar")){
			listar(request,response);
		}else if(cmd.equalsIgnoreCase("autorizacao")){
			pedidosAtivos(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void pedidosAtivos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = (HttpSession) request.getSession(); 
		Integer id = Integer.parseInt(session.getAttribute("user").toString());
		GenericDao<Usuario> ud = new GenericDao<Usuario>();
    	Usuario user = ud.findById(id,Usuario.class);
    	
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/cli/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i>ORDENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Ultimos Pedidos</li>");
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
        	
	        for(OrdemDeServico o : lst){
	        	if(o.getStatus().equals("ativo") && o.getVeiculo().getCliente().getNome().equalsIgnoreCase(user.getNome())){
	        	
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
			        pw.println("<a class=\"btn btn-info\" href=\"./ControleItemServico?cmd=listar&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_info_alt\"></i></a>");
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

        request.getRequestDispatcher("/cli/base2.html").include(request, response);
	}
	
	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = (HttpSession) request.getSession(); 
		Integer id = Integer.parseInt(session.getAttribute("user").toString());
		GenericDao<Usuario> ud = new GenericDao<Usuario>();
    	Usuario user = ud.findById(id,Usuario.class);
    	
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/cli/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i>ORDENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Ultimos Pedidos</li>");
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
        pw.println("<th><i class=\"fa fa-car\" aria-hidden=\"true\"></i> Veiculo</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Inicio</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Total</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	List<OrdemDeServico> lst = od.findAll(OrdemDeServico.class);
        	
	        for(OrdemDeServico o : lst){
	        	if(o.getStatus().equals("inativo") && o.getVeiculo().getCliente().getNome().equalsIgnoreCase(user.getNome())){
	        	
			        pw.println("<tr>");
			        pw.println("<td>"+o.getVeiculo().getDescricao()+"</td>");
			        pw.println("<td>"+o.getDataEmissao()+"</td>");
			        NumberFormat nf = NumberFormat.getInstance();
			        nf.setMaximumFractionDigits(2);
			        nf.setMinimumFractionDigits(2);
			        pw.println("<td>R$ "+nf.format(o.getValor())+"</td>");
			        pw.println("<td>");
			        pw.println("<div class=\"btn-group\">");
			        pw.println("<a class=\"btn btn-info\" href=\"./ControleItemServico?cmd=listar&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_info_alt\"></i></a>");
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

        request.getRequestDispatcher("/cli/base2.html").include(request, response);
	}
}
