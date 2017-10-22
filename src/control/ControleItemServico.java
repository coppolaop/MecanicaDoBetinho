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

import entity.ItemServico;
import entity.Mecanico;
import entity.OrdemDeServico;
import entity.Peca;
import entity.Servico;
import persistence.GenericDao;

/**
 * Servlet implementation class ControleItemServico
 */
@WebServlet("/usu/ControleItemServico")
public class ControleItemServico extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleItemServico() {
        super();
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
		}else if(cmd.equalsIgnoreCase("editar")){
			editar(request,response);
		}else if(cmd.equalsIgnoreCase("alterar")){
			alterar(request,response);
		}else if(cmd.equalsIgnoreCase("atualizar")){
			atualizar(request,response);
		}else if(cmd.equalsIgnoreCase("formulario")){
			formulario(request,response);
		}else if(cmd.equalsIgnoreCase("state")){
			state(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(cmd.equalsIgnoreCase("gravar")){
			String resposta;
			try{
				ItemServico is = new ItemServico();
				
				GenericDao<Mecanico> md = new GenericDao<Mecanico>();
				GenericDao<Servico> sd = new GenericDao<Servico>();
				GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
				
				Mecanico m = md.findById(Integer.parseInt(request.getParameter("mecanico")),Mecanico.class);
				Servico s = sd.findById(Integer.parseInt(request.getParameter("servico")), Servico.class);
				OrdemDeServico o = od.findById(Integer.parseInt(request.getParameter("id")), OrdemDeServico.class);
				
				if(o.getStatus().equalsIgnoreCase("inativo")){
					throw new Exception("Ordem de Serviço inativa");
				}
				
				is.setAutorizacao(false);
				is.setDataAdicao(new Date());
				is.setValor(s.getValor());
				is.setMecanico(m);
				is.setServico(s);
				is.setOrdemDeServico(o);
				
				m.adicionar(is);
				s.adicionar(is);
				o.adicionar(is);
				
				GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
				isd.create(is);
				md.update(m);
				sd.update(s);
				od.update(o);
				
				ControleOrdem.calculaValor();
				
				resposta = "Dados Armazenados";
			}catch(NumberFormatException ex){
				resposta = "Valor Inválido";
				ex.printStackTrace();
			}catch(Exception ex){
				ex.printStackTrace();
				resposta = ex.getMessage();
			}
			response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        RequestDispatcher rd = null;
	        out.println(resposta);
	        rd = request.getRequestDispatcher("./ControleOrdem?cmd=listar");
	        rd.include(request, response);
		}else{
			doGet(request,response);
		}
	}
	
	protected void deletar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<Peca> pd = new GenericDao<Peca>();
		GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
		GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
		GenericDao<Mecanico> md = new GenericDao<Mecanico>();
		GenericDao<Servico> sd = new GenericDao<Servico>();
		Integer ordem = 0;
         try
        {
            ItemServico is = isd.findById(id, ItemServico.class);
            ordem = is.getOrdemDeServico().getIdOrdemDeServico();
            if(is.getOrdemDeServico() != null){
            	OrdemDeServico o = is.getOrdemDeServico();
            		o.remover(is);
            		is.setOrdemDeServico(null);
            		od.update(o);
            }
            if(is.getServico()!=null){
            	Servico s = is.getServico();
            		s.remover(is);
            		is.setServico(null);
            		sd.update(s);
            }
            if(is.getMecanico()!=null){
            	Mecanico m = is.getMecanico();
            		m.remover(is);
            		is.setMecanico(null);
            		md.update(m);
            }
            if(is.getPecas()!=null){
            	for(Peca p : is.getPecas()){
            		p.remover(is);
            		pd.update(p);
            	}
            	is.setPecas(null);
            }
            isd.update(is);
            isd.delete(is);
            
            ControleOrdem.calculaValor();
            
            resposta = "Dados Excluidos";
        } catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         RequestDispatcher rd = null;
         out.println(resposta);
         rd = request.getRequestDispatcher("./ControleItemServico?cmd=listar&id="+ordem);
         rd.include(request, response);
	}

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = Integer.parseInt(request.getParameter("id"));
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);

        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ORDENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleOrdem?cmd=listar\">Ordens de Serviço</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i>Serviços Alocados</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Itens de Serviço");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-wrench\"></i> Serviço</th>");
        pw.println("<th><i class=\"icon_calendar\"></i> Data de Adição</th>");
        pw.println("<th><i class=\"fa fa-cog\" aria-hidden=\"true\"></i> Quantidade de Peças</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor Atual</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Mecânico Responsável</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");

        try {
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	OrdemDeServico o = od.findById(id,OrdemDeServico.class);
        	List<ItemServico> lista = o.getItensServico();
        	
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
			        pw.println("<a class=\"btn btn-primary\" href=\"./ControleItemPeca?cmd=formulario&id=" + i.getIdItemServico() + "&ordem="+ o.getIdOrdemDeServico() +"\"><i class=\"icon_plus\"></i></a>");
			        if(i.getOrdemDeServico().getStatus().equalsIgnoreCase("ativo")){
				        if(i.getAutorizacao()){
				        	pw.println("<a class=\"btn btn-success\" href=\"./ControleItemServico?cmd=state&id=" + i.getIdItemServico() + "\"><i class=\"icon_check_alt2\"></i></a>");	
				        }else{
				        	pw.println("<a class=\"btn btn-success\" href=\"./ControleItemServico?cmd=state&id=" + i.getIdItemServico() + "\"><i class=\"icon_circle-empty\"></i></a>");
				        }
			        }else{
			        	if(i.getAutorizacao()){
				        	pw.println("<a class=\"btn btn-success disabled\" href=\"./ControleItemServico?cmd=state&id=" + i.getIdItemServico() + "\"><i class=\"icon_check_alt2\"></i></a>");	
				        }else{
				        	pw.println("<a class=\"btn btn-success disabled\" href=\"./ControleItemServico?cmd=state&id=" + i.getIdItemServico() + "\"><i class=\"icon_circle-empty\"></i></a>");
				        }
			        }
			        
			        pw.println("<a class=\"btn btn-info\" href=\"./ControleItemPeca?cmd=listar&id=" + i.getIdItemServico() + "\"><i class=\"icon_info_alt\"></i></a>");
			        pw.println("<a class=\"btn btn-primary\" href=\"./ControleItemServico?cmd=editar&id=" + i.getIdItemServico() + "\"><i class=\"icon_pencil\"></i></a>");
			        pw.println("<a class=\"btn btn-danger\" href=\"./ControleItemServico?cmd=deletar&id=" + i.getIdItemServico() + "\"><i class=\"icon_close_alt2\"></i></a>");
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

        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        Integer id = Integer.parseInt(request.getParameter("id"));
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ITENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleOrdem?cmd=listar\">Ordens de Serviço</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Itens de Serviço</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12 col-lg-10\">");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleItemServico?cmd=alterar&id=" + id + "';\" type=\"button\">Alterar Dados</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
        GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        GenericDao<Mecanico> md = new GenericDao<Mecanico>();
        GenericDao<Servico> sd = new GenericDao<Servico>();
        ItemServico is = isd.findById(id, ItemServico.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> ITENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ListaOrdem\">Ordens de Serviço</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Itens de Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Alterar Itens</li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControleItemServico?Atualizar\">");
        pw.println("<div class=\"form-group \">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"atualizar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Ordem de Serviço <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"ordem\" id=\"ordem\">");
        try {
			List<OrdemDeServico> l = od.findAll(OrdemDeServico.class);
			List<OrdemDeServico> lista = new ArrayList<OrdemDeServico>();
			
			for(OrdemDeServico ordem : l){
				if(!lista.contains(ordem)){
					lista.add(ordem);
				}
			}
			
			for(OrdemDeServico ordem : lista){
				if(is.getOrdemDeServico().equals(ordem)){
					pw.println("<option value=\""+ordem.getIdOrdemDeServico()+"\" selected>"+ordem.getVeiculo().getPlaca()+"</option>");
				}else{
					pw.println("<option value=\""+ordem.getIdOrdemDeServico()+"\">"+ordem.getVeiculo().getPlaca()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        pw.println("</select>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Mecânico <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        
        pw.println("<select class=\"form-control m-bot15\" name=\"mecanico\" id=\"mecanico\">");
        try {
			List<Mecanico> l = md.findAll(Mecanico.class);
			List<Mecanico> lista = new ArrayList<Mecanico>();
			
			for(Mecanico mecanico : l){
				if(!lista.contains(mecanico)){
					lista.add(mecanico);
				}
			}
			
			for(Mecanico mecanico : lista){
				if(is.getOrdemDeServico().equals(mecanico)){
					pw.println("<option value=\""+mecanico.getIdMecanico()+"\" selected>"+mecanico.getNome()+"</option>");
				}else{
					pw.println("<option value=\""+mecanico.getIdMecanico()+"\">"+mecanico.getNome()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        pw.println("</select>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Serviço <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        
        pw.println("<select class=\"form-control m-bot15\" name=\"servico\" id=\"servico\">");
        try {
			List<Servico> l = sd.findAll(Servico.class);
			List<Servico> lista = new ArrayList<Servico>();
			
			for(Servico s : l){
				if(!lista.contains(s)){
					lista.add(s);
				}
			}
			
			for(Servico servico : lista){
				if(is.getOrdemDeServico().equals(servico)){
					pw.println("<option value=\""+servico.getIdServico()+"\" selected>"+servico.getNome()+"</option>");
				}else{
					pw.println("<option value=\""+servico.getIdServico()+"\">"+servico.getNome()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Alterar</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleItemServico?cmd=editar&id=" + id + "';\" type=\"button\">Voltar</button>");
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
        
        GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        GenericDao<Mecanico> md = new GenericDao<Mecanico>();
        GenericDao<Servico> sd = new GenericDao<Servico>();
        
        OrdemDeServico o = od.findById(Integer.parseInt(request.getParameter("ordem")), OrdemDeServico.class);
        Mecanico m = md.findById(Integer.parseInt(request.getParameter("mecanico")), Mecanico.class);
        Servico s = sd.findById(Integer.parseInt(request.getParameter("servico")), Servico.class);
        
        try
        {   
        	GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
        	ItemServico is = isd.findById(Integer.parseInt(request.getParameter("id")), ItemServico.class);
        	
        	is.getOrdemDeServico().remover(is);
        	is.setOrdemDeServico(o);
        	o.adicionar(is);
        	
        	is.getMecanico().remover(is);
        	is.setMecanico(m);
        	m.adicionar(is);
        	
        	is.getServico().remover(is);
        	is.setServico(s);
        	s.adicionar(is);
        	
        	isd.update(is);
        	
        	ControleOrdem.calculaValor();
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("./ControleItemServico?cmd=listar&id="+o.getIdOrdemDeServico());
            rd.include(request, response);
			out.close();
        }
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
				ids.changeStatus();
				isd.update(ids);
			}else{
				throw new Exception("Não é possível (des)autorizar um Serviço de uma ordem já finalizada");
			}
			
			resposta = "Autorização Alterada";
		} catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		}finally{
			response.setContentType("text/html");
	        RequestDispatcher rd = null;
	        out.println(resposta);
	        if(idOrdem.equals(0)){
	        	rd = request.getRequestDispatcher("./ControleOrdem?cmd=listar");
	        }else{
	        	rd = request.getRequestDispatcher("./ControleItemServico?cmd=listar&id="+idOrdem);
	        }
	       	rd.include(request, response);
			out.close();
		}
	}
	
	protected void formulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> Itens de Servição</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleOrdem?cmd=lista\">Ordens de Serviço</a></li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Itens de Serviço</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Itens de Servico");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleItemServico\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"gravar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ request.getParameter("id") +"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome do Mecânico</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"mecanico\" id=\"mecanico\">");
        
        try {
			GenericDao<Mecanico> md = new GenericDao<Mecanico>();
			List<Mecanico> l = md.findAll(Mecanico.class);
			List<Mecanico> lista = new ArrayList<Mecanico>();
			
			for(Mecanico mec : l){
				if(!lista.contains(mec)){
					lista.add(mec);
				}
			}
			
			for(Mecanico m : lista){
				pw.println("<option value=\""+m.getIdMecanico()+"\">"+m.getNome()+"</option>");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome do Serviço</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"servico\" id=\"servico\">");
        
        try {
			GenericDao<Servico> sd = new GenericDao<Servico>();
			List<Servico> l = sd.findAll(Servico.class);
			List<Servico> lista = new ArrayList<Servico>();
			
			for(Servico serv : l){
				if(!lista.contains(serv)){
					lista.add(serv);
				}
			}
			
			for(Servico s : lista){
				pw.println("<option value=\""+s.getIdServico()+"\">"+s.getNome()+"</option>");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Salvar</button>");
        pw.println("<button class=\"btn btn-default\" type=\"reset\">Limpar</button>");
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
}
