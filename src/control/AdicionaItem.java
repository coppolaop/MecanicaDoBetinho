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
import entity.ItemServico;
import entity.Mecanico;
import entity.OrdemDeServico;
import entity.Servico;

/**
 * Servlet implementation class AdicionaItem
 */
@WebServlet("/AdicionaItem")
public class AdicionaItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdicionaItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer idOrdem = Integer.parseInt(request.getParameter("id"));
		GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
		OrdemDeServico o = od.findById(idOrdem, OrdemDeServico.class);
		
		GenericDao<Mecanico> md = new GenericDao<Mecanico>();
		Mecanico m = md.findByName(request.getParameter("mecanico"), Mecanico.class);
		
		GenericDao<Servico> sd = new GenericDao<Servico>();
		Servico s = sd.findByName(request.getParameter("servico"), Servico.class);
		
		Double valor = 0.;
		if(!(s.getValor()==null)){
			valor = s.getValor();
		}
		
		ItemServico item = new ItemServico(null, valor, new Date(), true);
		item.setMecanico(m);
		item.setServico(s);
		item.setOrdemDeServico(o);
		GenericDao<ItemServico> id = new GenericDao<ItemServico>();
		try {
			id.create(item);
			OrdemDeServico ordem = (OrdemDeServico) o.clone();
			od.update(ordem);
			resposta = "Dados Armazenados";
		} catch (Exception ex) {
			resposta = ex.getMessage();
			ex.printStackTrace();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("ListaOrdem");
        rd.include(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
