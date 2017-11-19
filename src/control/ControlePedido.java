package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import persistence.GenericDao;
import entity.ItemServico;
import entity.Peca;
import entity.Usuario;
import entity.OrdemDeServico;

/**
 * Servlet implementation class ControlePedidos
 */
@WebServlet("/cli/ControlePedido")
public class ControlePedido extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControlePedido() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(cmd.equalsIgnoreCase("pedidosAtivos")){
			pedidosAtivos(request,response);
		}else if(cmd.equalsIgnoreCase("aprovar")){
			aprovar(request,response);
		}else if(cmd.equalsIgnoreCase("listar")){
			listar(request,response);
		}else if(cmd.equalsIgnoreCase("informacoes")){
			informacoes(request,response);
		}else if(cmd.equalsIgnoreCase("listarPecas")){
			listarPecas(request,response);
		}else if(cmd.equalsIgnoreCase("state")){
			state(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(cmd.equalsIgnoreCase("pedidosAtivos")){
			pedidosAtivos(request,response);
		}else if(cmd.equalsIgnoreCase("aprovar")){
			aprovar(request,response);
		}else if(cmd.equalsIgnoreCase("listar")){
			listar(request,response);
		}else if(cmd.equalsIgnoreCase("informacoes")){
			informacoes(request,response);
		}else if(cmd.equalsIgnoreCase("listarPecas")){
			listarPecas(request,response);
		}else if(cmd.equalsIgnoreCase("state")){
			state(request,response);
		}
	}
	
	protected void pedidosAtivos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = (HttpSession) request.getSession(); 
		Integer id = Integer.parseInt(session.getAttribute("user").toString());
		GenericDao<Usuario> ud = new GenericDao<Usuario>();
    	Usuario user = ud.findById(id,Usuario.class);
    	
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/cli/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i>ORDENS DE SERVI�O</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Meu Pedido</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Chamadas em Aberto");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-car\" aria-hidden=\"true\"></i> Ve�culo</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Inicio</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Atual</th>");
        pw.println("<th><i class=\"fa fa-info\" aria-hidden=\"true\"></i> Informa��es</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> A��o</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	List<OrdemDeServico> lst = od.findAll(OrdemDeServico.class);
        	
	        for(OrdemDeServico o : lst){
	        	if(o.getStatus().equals("ativo") && o.getVeiculo().getCliente().getNome().equalsIgnoreCase(user.getNome())){
	        	
	        		boolean info = false;
			        for(ItemServico i : o.getItensServico()){
			        	if(!(i == null)){
				        	if(i.getAutorizacao()==false){
				        		info = true;
				        	}
			        	}
			        }
	        		
			        if(info){
			        	pw.println("<tr class='warning'>");
			        }else{
			        	pw.println("<tr>");
			        }
			        pw.println("<td>"+o.getVeiculo().getDescricao()+"</td>");
			        pw.println("<td>"+o.getDataEmissao()+"</td>");
			        NumberFormat nf = NumberFormat.getInstance();
			        nf.setMaximumFractionDigits(2);
			        nf.setMinimumFractionDigits(2);
			        pw.println("<td>R$ "+nf.format(o.getValor())+"</td>");
			        if(info){
			        	pw.println("<td>H� Itens aguardando Aprova��o</td>");
			        }else{
			        	pw.println("<td></td>");
			        }
			        pw.println("<td>");
			        pw.println("<div class=\"btn-group\">");
			        pw.println("<a class=\"btn btn-info\" href=\"./ControlePedido?cmd=aprovar&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_info_alt\"></i></a>");
			        pw.println("</div>");
			        pw.println("</td>");
			
			        pw.println("</tr>");
	        	}
	        
	        }
        } catch (Exception ex) {
			ex.printStackTrace();
		}

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");

        request.getRequestDispatcher("/cli/base2.html").include(request, response);
	}
	
	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = (HttpSession) request.getSession(); 
		Integer id = Integer.parseInt(session.getAttribute("user").toString());
		GenericDao<Usuario> ud = new GenericDao<Usuario>();
    	Usuario user = ud.findById(id,Usuario.class);
    	
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/cli/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i>ORDENS DE SERVI�O</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Ultimos Pedidos</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Chamadas em Aberto");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-car\" aria-hidden=\"true\"></i> Ve�culo</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Inicio</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Total</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> A��o</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	List<OrdemDeServico> lst = od.findAll(OrdemDeServico.class);
        	
	        for(OrdemDeServico o : lst){
	        	if(o.getStatus().equals("inativo") && o.getVeiculo().getCliente().getNome().equalsIgnoreCase(user.getNome())){
	        	
			        pw.println("<tr>");
			        pw.println("<td>"+o.getVeiculo().getDescricao()+"</td>");
			        pw.println("<td>"+o.getDataEmissao()+"</td>");
			        NumberFormat nf = NumberFormat.getInstance();
			        nf.setMaximumFractionDigits(2);
			        nf.setMinimumFractionDigits(2);
			        pw.println("<td>R$ "+nf.format(o.getValor())+"</td>");
			        pw.println("<td>");
			        pw.println("<div class=\"btn-group\">");
			        pw.println("<a class=\"btn btn-info\" href=\"./ControlePedido?cmd=informacoes&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_info_alt\"></i></a>");
			        pw.println("</div>");
			        pw.println("</td>");
			
			        pw.println("</tr>");
	        	}
	        
	        }
        } catch (Exception ex) {
			ex.printStackTrace();
		}

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");

        request.getRequestDispatcher("/cli/base2.html").include(request, response);
	}
	
	protected void aprovar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = Integer.parseInt(request.getParameter("id"));
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/cli/base1.html").include(request, response);
        HttpSession session = (HttpSession) request.getSession(); 
		Integer idUsuario = Integer.parseInt(session.getAttribute("user").toString());

        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ORDENS DE SERVI�O</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"ControlePedido?cmd=pedidosAtivos\">Meu Pedido</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i>Servi�os Executados</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Itens de Servi�o");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-wrench\"></i> Servi�o</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Adi��o</th>");
        pw.println("<th><i class=\"fa fa-cog\" aria-hidden=\"true\"></i> Quantidade de Pe�as</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Atual</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Mec�nico Respons�vel</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> A��o</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	OrdemDeServico o = od.findById(id,OrdemDeServico.class);
        	List<ItemServico> lista = o.getItensServico();
        	
        	if(!o.getVeiculo().getCliente().getIdUsuario().equals(idUsuario)){
        		throw new Exception("N�o permitido acesso a informa��es de outro Usu�rio");
        	}
        	
        	ControleOrdem.calculaValor();
        	
	        for(ItemServico i : lista){
	        
	        	if(!(i == null)){
	        	
			        pw.println("<tr>");
			        pw.println("<td>"+i.getServico().getNome()+"</td>");
			        pw.println("<td>"+i.getDataAdicao()+"</td>");
			        pw.println("<td>"+i.getPecas().size()+"</td>");
			        NumberFormat nf = NumberFormat.getInstance();
			        nf.setMaximumFractionDigits(2);
			        nf.setMinimumFractionDigits(2);
			        pw.println("<td>R$ "+nf.format(i.getValor())+"</td>");
			        pw.println("<td>"+i.getMecanico().getNome()+"</td>");
			        pw.println("<td>");
			        pw.println("<div class=\"btn-group\">");
			        if(!i.getAutorizacao()){
			        	pw.println("<a class=\"btn btn-success\" href=\"./ControlePedido?cmd=state&id=" + i.getIdItemServico() + "\"><i class=\"icon_circle-empty\"></i></a>");
			        }else{
			        	pw.println("<a class=\"btn btn-success disabled\"><i class=\"icon_check_alt2\"></i></a>");
			        }
			        pw.println("</div>");
			        pw.println("</td>");
			
			        pw.println("</tr>");
	        	}
	        }
        } catch (Exception ex) {
			ex.printStackTrace();
		}

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");

        request.getRequestDispatcher("/cli/base2.html").include(request, response);
	}
	
	protected void informacoes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = (HttpSession) request.getSession(); 
		Integer idUsuario = Integer.parseInt(session.getAttribute("user").toString());
		
		Integer id = Integer.parseInt(request.getParameter("id"));
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/cli/base1.html").include(request, response);

        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ORDENS DE SERVI�O</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"ControlePedido?cmd=listar\">Ultimos Pedidos</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i>Servi�os Executados</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Itens de Servi�o");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-wrench\"></i> Servi�o</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Adi��o</th>");
        pw.println("<th><i class=\"fa fa-cog\" aria-hidden=\"true\"></i> Quantidade de Pe�as</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Mec�nico Respons�vel</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> A��o</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	OrdemDeServico o = od.findById(id,OrdemDeServico.class);
        	List<ItemServico> lista = o.getItensServico();
        	
        	if(!o.getVeiculo().getCliente().getIdUsuario().equals(idUsuario)){
        		throw new Exception("N�o permitido acesso a informa��es de outro Usu�rio");
        	}
        	
        	ControleOrdem.calculaValor();
        	
	        for(ItemServico i : lista){
	        
	        	if(!(i == null)){
	        	
	        		if(i.getAutorizacao()==true){
				        pw.println("<tr>");
				        pw.println("<td>"+i.getServico().getNome()+"</td>");
				        pw.println("<td>"+i.getDataAdicao()+"</td>");
				        pw.println("<td>"+i.getPecas().size()+"</td>");
				        NumberFormat nf = NumberFormat.getInstance();
				        nf.setMaximumFractionDigits(2);
				        nf.setMinimumFractionDigits(2);
				        pw.println("<td>R$ "+nf.format(i.getValor())+"</td>");
				        pw.println("<td>"+i.getMecanico().getNome()+"</td>");
				        pw.println("<td>");
				        pw.println("<div class=\"btn-group\">");
				        pw.println("<a class=\"btn btn-info\" href=\"./ControlePedido?cmd=listarPecas&id=" + i.getIdItemServico() + "\"><i class=\"icon_info_alt\"></i></a>");
				        pw.println("</div>");
				        pw.println("</td>");
				
				        pw.println("</tr>");
	        		}
	        	}
	        }
        } catch (Exception ex) {
			ex.printStackTrace();
		}

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");

        request.getRequestDispatcher("/cli/base2.html").include(request, response);
	}
	
	protected void listarPecas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
    	ItemServico is = isd.findById(id,ItemServico.class);
    	
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/cli/base1.html").include(request, response);

        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ORDENS DE SERVI�O</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"ControlePedido?cmd=listar\">Ultimos Pedidos</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i><a href=\"ControlePedido?cmd=informacoes&id="+ is.getOrdemDeServico().getIdOrdemDeServico() +"\">Servi�os Executados</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i>Pe�as Utilizadas</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Itens de Servi�o");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-wrench\"></i> Pe�a</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor</th>");
        pw.println("</tr>");

        try {
        	List<Peca> lista = is.getPecas();
        	
	        for(Peca p : lista){
	        
	        	if(!(p == null)){
	        	
			        pw.println("<tr>");
			        pw.println("<td>"+p.getNome()+"</td>");
			        NumberFormat nf = NumberFormat.getInstance();
			        nf.setMaximumFractionDigits(2);
			        nf.setMinimumFractionDigits(2);
			        pw.println("<td>R$ "+nf.format(p.getValor())+"</td>");
			
			        pw.println("</tr>");
	        	}
	        }
        } catch (Exception ex) {
			ex.printStackTrace();
		}

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");

        request.getRequestDispatcher("/cli/base2.html").include(request, response);
	}
	
	protected void state(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		PrintWriter out = response.getWriter();
		Integer idOrdem = 0;
		try {	
			Integer id = Integer.parseInt(request.getParameter("id"));
			GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
			ItemServico is = isd.findById(id, ItemServico.class);
			ItemServico ids = (ItemServico) is.clone();
			idOrdem = is.getOrdemDeServico().getIdOrdemDeServico();
			
			if(is.getOrdemDeServico().getStatus().equalsIgnoreCase("ativo")){
				if(is.getAutorizacao()==false){
					ids.changeStatus();
					isd.update(ids);
				}else{
					throw new Exception("Um clienten n�o pode desautorizar um Servi�o j� aceito, entre em contato com a oficina");
				}
			}else{
				throw new Exception("N�o � poss�vel (des)autorizar um Servi�o de uma ordem j� finalizada");
			}
			
			resposta = "Autoriza��o Alterada";
		} catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		}finally{
			response.setContentType("text/html");
	        RequestDispatcher rd = null;
	        out.println(resposta);
	        if(idOrdem.equals(0)){
	        	rd = request.getRequestDispatcher("./ControlePedido?cmd=listar");
	        }else{
	        	rd = request.getRequestDispatcher("./ControlePedido?cmd=aprovar&id="+idOrdem);
	        }
	       	rd.include(request, response);
			out.close();
		}
	}
}
