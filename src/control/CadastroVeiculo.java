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
import entity.Cliente;
import entity.Endereco;
import entity.Veiculo;

/**
 * Servlet implementation class CadastroVeiculo
 */
@WebServlet("/CadastroVeiculo")
public class CadastroVeiculo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CadastroVeiculo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		try{
			GenericDao<Cliente> cd = new GenericDao<Cliente>();
			Cliente c = cd.findByName(request.getParameter("cliente"), Cliente.class);
			Veiculo v = new Veiculo();
			
			v.setPlaca(request.getParameter("placa"));
			v.setDescricao(request.getParameter("descricao"));
			v.setCliente(c);
			c.addVeiculo(v);
			
			GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
			vd.create(v);
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
        rd = request.getRequestDispatcher("/carro.html");
        rd.include(request, response);
	}

}
