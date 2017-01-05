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
import entity.Veiculo;

/**
 * Servlet implementation class FormAgendamento2
 */
@WebServlet("/FormAgendamento2")
public class FormAgendamento2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormAgendamento2() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/agenda1.html").include(request, response);
        String cliente = request.getParameter("cliente");
        
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"post\" action=\"Agendamento\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Placa do Veículo</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"placa\" id=\"placa\">");
        
        try {
			GenericDao<Cliente> cd = new GenericDao<Cliente>();
			Cliente c = cd.findByName(cliente, Cliente.class);
			GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
			List<Veiculo> lista = vd.findAll(Veiculo.class);
			for(Veiculo v : lista){
				if(v.getCliente().getIdCliente().equals(c.getIdCliente())){
					pw.println("<option value=\""+v.getIdVeiculo()+"\">"+v.getPlaca()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Agendar</button>");
        pw.println("<button class=\"btn btn-default\" href=\"FormAgendamento\" onclick=\"location.href='FormAgendamento';\" type=\"button\">Voltar</button>");
        
        request.getRequestDispatcher("/agenda2.html").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
