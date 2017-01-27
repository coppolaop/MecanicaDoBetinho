package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericDao;
import entity.Cliente;
import entity.OrdemDeServico;

/**
 * Servlet implementation class EditaOrdem1
 */
@WebServlet("/EditaOrdem1")
public class EditaOrdem1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditaOrdem1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = Integer.parseInt(request.getParameter("id"));
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> AGENDAMENTO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Agendamento</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Agendamento");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"post\" action=\"EditaOrdem2?id="+ id +"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome do Cliente</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"cliente\" id=\"cliente\">");
        
        try {
        	
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
            OrdemDeServico o = od.findById(id, OrdemDeServico.class);
            GenericDao<Cliente> cd = new GenericDao<Cliente>();

            Integer cliente = o.getVeiculo().getCliente().getIdCliente();
            
			List<Cliente> l = cd.findAll(Cliente.class);
			List<Cliente> lista = new ArrayList<Cliente>();
			
			for(Cliente cli : l){
				if(!lista.contains(cli)){
					lista.add(cli);
				}
			}
			
			for(Cliente c : lista){
				if(c.getIdCliente().equals(cliente)){
					pw.println("<option value=\""+c.getNome()+"\" selected>"+c.getNome()+"</option>");
				}else{
					pw.println("<option value=\""+c.getNome()+"\">"+c.getNome()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Selecionar Cliente</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
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
