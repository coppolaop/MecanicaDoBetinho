package control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericDao;
import entity.Cliente;
import entity.OrdemDeServico;

/**
 * Servlet implementation class EditaOrdem
 */
@WebServlet("/EditaOrdem")
public class EditaOrdem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditaOrdem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
        Integer id = Integer.parseInt(request.getParameter("id"));
        PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/edit1.html").include(request, response);

         try
        {
            GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
            OrdemDeServico o = od.findById(id, OrdemDeServico.class);
            GenericDao<Cliente> cd = new GenericDao<Cliente>();

            String cliente = o.getVeiculo().getCliente().getNome();
            Integer oid = o.getIdOrdemDeServico();
            String placa = o.getVeiculo().getPlaca();

            pw.println("<div class=\"form-group\">");
            pw.println("<label for=\"cname\" class=\"control-label col-lg-2\"> Cliente<span class=\"required\">*</span></label>");
            pw.println("<div class=\"col-lg-10\">");
            pw.println("<select class=\"form-control m-bot15\" onchange=\"\" name=\"cliente\" id=\"cliente\">");
            pw.println("<option value=\"1\">Adriana</option>");
            pw.println("<option value=\"2\">Julio</option>");
            pw.println("<option value=\"3\">Lucas</option>");
            pw.println("<option value=\"4\">Luiz Pedro</option>");
            pw.println("</select>");
            pw.println("</div>");
            pw.println("</div>");
            
            
            pw.println("<div class=\"form-group\">");
            pw.println("<div class=\"col-lg-10\">");
            pw.println("<input type=\"text\" style=\"display:none\" id=\"oid\" name=\"id\" value='" + oid + "'>");
            pw.println("</div>");
            pw.println("</div>");
            
            pw.println("<div class=\"form-group\">");
            pw.println("<label for=\"cplaca\" class=\"control-label col-lg-2\">Placa do Carro <span class=\"required\">*</span></label>");
            pw.println("<div class=\"col-lg-10\">");
            pw.println("<select class=\"form-control m-bot15\" name=\"placa\" id=\"placa\">");
            pw.println("<option value=\"1\">AAA1111</option>");
            pw.println("<option value=\"2\">BBB2222</option>");
            pw.println("<option value=\"3\">CCC3333</option>");
            pw.println("<option value=\"4\">DDD4444</option>");
            pw.println("</select>");
            pw.println("</div>");
            pw.println("</div>");

            request.getRequestDispatcher("/edit2.html").include(request, response);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
