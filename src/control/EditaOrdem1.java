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
        request.getRequestDispatcher("/agenda1.html").include(request, response);
        
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
