package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class Agendamento
 */
@WebServlet("/Agendamento")
public class Agendamento extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Agendamento() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		try{
			GenericDao<Cliente> cd = new GenericDao<Cliente>();
			GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
			Cliente c = cd.findByName(request.getParameter("cliente"), Cliente.class);
			Veiculo v = vd.findById(Integer.parseInt(request.getParameter("placa")), Veiculo.class);
			
			GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
			OrdemDeServico ordem = new OrdemDeServico();
			
			ordem.setDataEmissao(new Date(System.currentTimeMillis()));
			ordem.setValor(0.);
			ordem.setVeiculo(v);
			
			od.create(ordem);
			
			resposta = "Dados Armazenados";
		}catch(NumberFormatException ex){
			resposta = "Valor Inválido";
			ex.printStackTrace();
		}catch(Exception ex){
			resposta = ex.getMessage();
			ex.printStackTrace();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("/form.html");
        rd.include(request, response);
	}
}
