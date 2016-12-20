package control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericDao;
import entity.OrdemDeServico;
import entity.Veiculo;

/**
 * Servlet implementation class AtualizaOrdem
 */
@WebServlet("/AtualizaOrdem")
public class AtualizaOrdem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AtualizaOrdem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        try
        {
            
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Update</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet Update at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
            
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	OrdemDeServico o = od.findById(Integer.parseInt(request.getParameter("id")), OrdemDeServico.class);
        	GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
//        	GenericDao<Cliente> cd = new GenericDao<Cliente>();
        	Veiculo v = vd.findById(Integer.parseInt(request.getParameter("placa")), Veiculo.class);
//        	Cliente c = cd.findById(Integer.parseInt(request.getParameter("cliente")), Cliente.class);
        	
        	OrdemDeServico o2 = (OrdemDeServico) o.clone();//PROTOTYPE
        	o2.setVeiculo(v);
        	od.update(o2);
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.toString();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("/ListaOrdem");
            rd.include(request, response);
			out.close();
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