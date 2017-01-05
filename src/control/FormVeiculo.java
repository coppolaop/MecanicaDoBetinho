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

/**
 * Servlet implementation class FormVeiculo
 */
@WebServlet("/FormVeiculo")
public class FormVeiculo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormVeiculo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/veiculo1.html").include(request, response);
        try {
			GenericDao<Cliente> cd = new GenericDao<Cliente>();
			List<Cliente> l = cd.findAll(Cliente.class);
			List<Cliente> lista = new ArrayList<Cliente>();
			
			for(Cliente cli : l){
				if(!lista.contains(cli)){
					lista.add(cli);
				}
			}
			
			for(Cliente c : lista){
				pw.println("<option value=\""+c.getNome()+"\">"+c.getNome()+"</option>");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        request.getRequestDispatcher("/veiculo2.html").include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
