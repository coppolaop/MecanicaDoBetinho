package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericDao;
import entity.Cliente;
import entity.Endereco;
import entity.ItemServico;
import entity.OrdemDeServico;
import entity.Peca;
import entity.Veiculo;

/**
 * Servlet implementation class ControleOrdem
 */
@WebServlet("/usu/ControleOrdem")
public class ControleOrdem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleOrdem() {
        super();
    }
    
    public static void calculaValor(){
    	GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
		GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
		
		try {
			List<OrdemDeServico> ordens = od.findAll(OrdemDeServico.class);
			for(OrdemDeServico o : ordens){
				o.setValor(0.);
				for(ItemServico is : o.getItensServico()){
					if(is!=null){
						Double valor = 0.;
						if(is.getServico()!=null){
							if(is.getServico().getValor()!=null){
								valor = is.getServico().getValor();
							}
						}
						is.setValor(valor);
						for(Peca p : is.getPecas()){
							valor = is.getValor();
							valor += p.getValor();
							is.setValor(valor);
							isd.update(is);
						}
						
						valor = o.getValor();
						valor += is.getValor();
						o.setValor(valor);
					}
				}
				od.update(o);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(cmd.equalsIgnoreCase("gravar")){
			doPost(request,response);
		}else if(cmd.equalsIgnoreCase("listar")){
			listar(request,response);
		}else if(cmd.equalsIgnoreCase("deletar")){
			deletar(request,response);
		}else if(cmd.equalsIgnoreCase("editar1")){
			editar1(request,response);
		}else if(cmd.equalsIgnoreCase("editar2")){
			editar2(request,response);
		}else if(cmd.equalsIgnoreCase("atualizar")){
			atualizar(request,response);
		}else if(cmd.equalsIgnoreCase("formulario1")){
			formulario1(request,response);
		}else if(cmd.equalsIgnoreCase("formulario2")){
			formulario2(request,response);
		}else if(cmd.equalsIgnoreCase("state")){
			state(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		try{
			GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
			Veiculo v = vd.findById(Integer.parseInt(request.getParameter("placa")), Veiculo.class);
			
			GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
			OrdemDeServico ordem = new OrdemDeServico();
			
			ordem.setDataEmissao(new Date(System.currentTimeMillis()));
			ordem.setValor(0.);
			ordem.setVeiculo(v);
			
			List<OrdemDeServico> lista = od.findAll(OrdemDeServico.class);
			for(OrdemDeServico o : lista){
				if(o.getVeiculo().getIdVeiculo().equals(v.getIdVeiculo())){
					if(o.getStatus().equals("ativo")){
						throw new Exception("Já existe uma Ordem em Aberto para esse Veículo");
					}
				}
			}
			
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
        rd = request.getRequestDispatcher("./ControleOrdem?cmd=listar");
        rd.include(request, response);
	}
	
	protected void deletar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
		
         try
        {
            OrdemDeServico o = od.findById(Integer.parseInt(request.getParameter("id")), OrdemDeServico.class);
            od.delete(o);
            resposta = "Dados Excluidos";
        } catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         RequestDispatcher rd = null;
         out.println(resposta);
         rd = request.getRequestDispatcher("./ControleOrdem?cmd=listar");
         rd.include(request, response);
	}
	

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ORDENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Ordens de Serviço</li>");
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
        pw.println("<th><i class=\"icon_profile\"></i> Cliente</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Inicio</th>");
        pw.println("<th><i class=\"fa fa-car\" aria-hidden=\"true\"></i> Veiculo</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Atual</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	List<OrdemDeServico> lst = od.findAll(OrdemDeServico.class);
//        	List<OrdemDeServico> lista = new ArrayList<OrdemDeServico>();
//        	
//        	for(OrdemDeServico o : lst){
//				if(!lista.contains(o)){
//					lista.add(o);
//				}
//			}
        	
        	calculaValor();
        	
	        for(OrdemDeServico o : lst){
	        	
		        pw.println("<tr>");
		        pw.println("<td>"+o.getVeiculo().getCliente().getNome()+"</td>");
		        pw.println("<td>"+o.getDataEmissao()+"</td>");
		        pw.println("<td>"+o.getVeiculo().getDescricao()+"</td>");
		        NumberFormat nf = NumberFormat.getInstance();
		        nf.setMaximumFractionDigits(2);
		        nf.setMinimumFractionDigits(2);
		        pw.println("<td>R$ "+nf.format(o.getValor())+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-primary\" href=\"./ControleItemServico?cmd=formulario&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_plus\"></i></a>");
		        pw.println("<a class=\"btn btn-info\" href=\"./ControleItemServico?cmd=listar&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_info_alt\"></i></a>");
		        pw.println("<a class=\"btn btn-primary\" href=\"./ControleOrdem?cmd=editar1&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_pencil\"></i></a>");
		        if(o.getStatus().equals("ativo")){
		        	pw.println("<a class=\"btn btn-success\" href=\"./ControleOrdem?cmd=state&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_check_alt2\"></i></a>");	
		        }else{
		        	pw.println("<a class=\"btn btn-warning\" href=\"./ControleOrdem?cmd=state&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_check_alt2\"></i></a>");
		        }
		        pw.println("<a class=\"btn btn-danger\" href=\"./ControleOrdem?cmd=deletar&id=" + o.getIdOrdemDeServico() + "\"><i class=\"icon_close_alt2\"></i></a>");
		        pw.println("</div>");
		        pw.println("</td>");
		
		        pw.println("</tr>");
	        
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

        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void editar1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = Integer.parseInt(request.getParameter("id"));
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> AGENDAMENTO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Agendamento</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Agendamento");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleOrdem\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"editar2\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+id+"\">");
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
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void editar2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        String cliente = request.getParameter("cliente");
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> AGENDAMENTO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Agendamento</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Agendamento");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleOrdem\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"atualizar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+id+"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Placa do Veículo</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"placa\" id=\"placa\">");
        
        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
			GenericDao<Cliente> cd = new GenericDao<Cliente>();
			Cliente c = cd.findByName(cliente, Cliente.class);
			OrdemDeServico o = od.findById(id, OrdemDeServico.class);
			GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
			List<Veiculo> lista = vd.findAll(Veiculo.class);
			for(Veiculo v : lista){
				if(v.getCliente().getIdCliente().equals(c.getIdCliente())){
					if(v.getIdVeiculo().equals(o.getVeiculo().getIdVeiculo())){
						pw.println("<option value=\""+v.getIdVeiculo()+"\" selected>"+v.getPlaca()+"</option>");
					}else{
						pw.println("<option value=\""+v.getIdVeiculo()+"\">"+v.getPlaca()+"</option>");
					}
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
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='ControleOrdem?cmd=editar1&id="+ id +"';\" type=\"button\">Voltar</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void atualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        try
        {   
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	OrdemDeServico o = od.findById(Integer.parseInt(request.getParameter("id")), OrdemDeServico.class);
        	GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
        	Veiculo v = vd.findById(Integer.parseInt(request.getParameter("placa")), Veiculo.class);
        	
        	List<OrdemDeServico> lista = od.findAll(OrdemDeServico.class);
        	for(OrdemDeServico ordem : lista){
				if(ordem.getVeiculo().getIdVeiculo().equals(v.getIdVeiculo())){
					if(ordem.getStatus().equalsIgnoreCase("ativo")&&o.getStatus().equalsIgnoreCase("ativo")){
						throw new Exception("Já existe uma Ordem em Aberto para esse Veículo");
					}
				}
			}
        	
        	OrdemDeServico o2 = (OrdemDeServico) o.clone();//PROTOTYPE
        	o2.setVeiculo(v);
        	od.update(o2);
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("./ControleOrdem?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
	
	protected void formulario1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> AGENDAMENTO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Agendamento</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Agendamento");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleOrdem\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"formulario2\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome do Cliente</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"cliente\" id=\"cliente\">");
        
        try {
			GenericDao<Cliente> cd = new GenericDao<Cliente>();
			List<Cliente> l = cd.findAll(Cliente.class);
			List<Cliente> lista = new ArrayList<>();
			
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
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Selecionar Cliente</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void formulario2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        String cliente = request.getParameter("cliente");
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> AGENDAMENTO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Agendamento</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Agendamento");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleOrdem\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"gravar\">");
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
        pw.println("<button class=\"btn btn-default\" href=\"ControleOrdem?cmd=formulario1\" onclick=\"location.href='FormAgendamento';\" type=\"button\">Voltar</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void state(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
					if(o.getStatus().equalsIgnoreCase("ativo")){
						if(ordem.getStatus().equalsIgnoreCase("inativo")){
							throw new Exception("Não é possivel ter mais de uma Ordem De Serviço em Aberto para o mesmo Veículo");
						}
					}
				}
			}
			
			ods.changeStatus();
			od.update(ods);
			
			resposta = "Estado Alterado";
		} catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		}finally{
			response.setContentType("text/html");
	        RequestDispatcher rd = null;
	        out.println(resposta);
	        rd = request.getRequestDispatcher("./ControleOrdem?cmd=listar");
	        rd.include(request, response);
			out.close();
		}
	}
}