package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericDao;
import entity.OrdemDeServico;

/**
 * Servlet implementation class StateOrdem
 */
@WebServlet("/StateOrdem")
public class StateOrdem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StateOrdem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		PrintWriter out = response.getWriter();
		try {	
			Integer id = Integer.parseInt(request.getParameter("id"));
			GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
			OrdemDeServico ordem = od.findById(id, OrdemDeServico.class);
			OrdemDeServico ods = (OrdemDeServico) ordem.clone();
			
			List<OrdemDeServico> lista = od.findAll(OrdemDeServico.class);
			for(OrdemDeServico o : lista){
				if(o.getVeiculo().getIdVeiculo().equals(ordem.getVeiculo().getIdVeiculo())){
					if(o.getStatus().equalsIgnoreCase("Ativo")){
						if(ordem.getStatus().equalsIgnoreCase("Inativo")){
							throw new Exception("Não é possivel ter mais de uma Ordem De Serviço em Aberto para o mesmo Veículo");
						}
					}
				}
			}
			
			ods.changeState();
			od.update(ods);
			
			resposta = "Estado Alterado";
		} catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		}finally{
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
		doGet(request, response);
	}

}
