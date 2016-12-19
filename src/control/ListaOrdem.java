package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericDao;
import entity.Cliente;
import entity.OrdemDeServico;
import entity.Veiculo;

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/ordem1.html").include(request, response);

        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"icon_profile\"></i> Cliente</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Inicio</th>");
        pw.println("<th><i class=\"icon_mail_alt\"></i> Veiculo</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Action</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	List<OrdemDeServico> lista = od.findAll(OrdemDeServico.class);
        	GenericDao<Cliente> cd = new GenericDao<Cliente>();
        	
	        for(OrdemDeServico o : lista){
	        
	        	
		        //Begin Logic while List...
		        pw.println("<tr>");
		        pw.println("<td>"+o.getVeiculo().getCliente().getNome()+"</td>");
		        pw.println("<td>"+o.getDataEmissao()+"</td>");
		        pw.println("<td>"+o.getVeiculo().getDescricao()+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-primary\" href=\"#\"><i class=\"icon_plus_alt2\"></i></a>");
		        pw.println("<a class=\"btn btn-success\" href=\"#\"><i class=\"icon_check_alt2\"></i></a>");
		        pw.println("<a class=\"btn btn-danger\" href=\"#\"><i class=\"icon_close_alt2\"></i></a>");
		        pw.println("</div>");
		        pw.println("</td>");
		
		        pw.println("</tr>");
	        
	        }
        } catch (Exception ex) {
			ex.printStackTrace();
		}

        //End Logic

        pw.println("</tbody>");
        pw.println("</table>");

        request.getRequestDispatcher("/ordem2.html").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
