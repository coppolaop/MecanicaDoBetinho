package control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;

import persistence.GenericDao;
import entity.Cliente;
import entity.Endereco;
import entity.Mecanico;

/**
 * Servlet implementation class CadastroMecanico
 */
@WebServlet("/CadastroMecanico")
public class CadastroMecanico extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CadastroMecanico() {
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
			Mecanico m = new Mecanico();
			m.setNome(request.getParameter("nome"));
			m.setCpf(Long.parseLong(request.getParameter("cpf")));
			m.setEmail(request.getParameter("email"));
			m.setTelefone(Long.parseLong(request.getParameter("telefone")));
			m.setCelular(Long.parseLong(request.getParameter("celular")));
			
			Endereco e = new Endereco();
			e.setRua(request.getParameter("rua"));
			e.setNumero(Integer.parseInt(request.getParameter("numero")));
			e.setLogradouro(request.getParameter("logradouro"));
			e.setBairro(request.getParameter("bairro"));
			e.setCidade(request.getParameter("cidade"));
			e.setEstado(request.getParameter("estado"));
			e.setCep(Integer.parseInt(request.getParameter("cep")));
			
			m.setEndereco(e);
			
			GenericDao<Mecanico> cd = new GenericDao<Mecanico>();
			cd.create(m);
			resposta = "Dados Armazenados";
		}catch(NumberFormatException ex){
			resposta = "Valor Inválido";
			ex.printStackTrace();
		}catch(ConstraintViolationException ex){
			resposta = "Já Existe um Mecânico cadastrado com esse nome";
			ex.printStackTrace();
		}catch(Exception ex){
			resposta = ex.getMessage();
			ex.printStackTrace();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("/mecanico.html");
        rd.include(request, response);
	}

}
