package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

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
import entity.Peca;
import entity.Servico;

/**
 * Servlet implementation class ControleItemPeca
 */
@WebServlet("/ControleItemPeca")
public class ControleItemPeca extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleItemPeca() {
        super();
    }
    
    public void calculaValor(){
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
		}else if(cmd.equalsIgnoreCase("alterar")){
			alterar(request,response);
		}else if(cmd.equalsIgnoreCase("atualizar")){
			atualizar(request,response);
		}else if(cmd.equalsIgnoreCase("formulario")){
			formulario(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer ordem = Integer.parseInt(request.getParameter("ordem"));
		
        try{
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
    		if(od.findById(ordem, OrdemDeServico.class).getStatus().equalsIgnoreCase("Inativo")){
    			throw new Exception("A Ordem De Servi�o n�o est� aberta");
    		}
    		
			GenericDao<Peca> pd = new GenericDao<Peca>();
			GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
			
			Peca p = pd.findById(Integer.parseInt(request.getParameter("peca")),Peca.class);
			ItemServico is = isd.findById(Integer.parseInt(request.getParameter("id")), ItemServico.class);
				
			p.adicionar(is);
			is.adicionar(p);
			
			pd.update(p);
			isd.update(is);
			
			calculaValor();
			
			resposta = "Dados Armazenados";
		}catch(Exception ex){
			ex.printStackTrace();
			resposta = ex.getMessage();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("/ControleItemServico?cmd=listar&id="+ordem);
        rd.include(request, response);
	}
	
	protected void deletar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<Peca> pd = new GenericDao<Peca>();
		GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
		OrdemDeServico ordem = new OrdemDeServico();
        try
        {
        	if(ordem.getStatus().equalsIgnoreCase("Inativo")){
    			throw new Exception("A Ordem De Servi�o n�o est� aberta");
    		}
        	
            ItemServico is = isd.findById(id, ItemServico.class);
            Peca p = pd.findById(Integer.parseInt(request.getParameter("peca")), Peca.class);
            ordem = is.getOrdemDeServico();
            
            is.remover(p);
            p.remover(is);
            
            pd.update(p);
            isd.update(is);
            
            calculaValor();
            
            resposta = "Dados Excluidos";
        } catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         RequestDispatcher rd = null;
         out.println(resposta);
         rd = request.getRequestDispatcher("/ControleItemServico?cmd=listar&id="+ordem.getIdOrdemDeServico());
         rd.include(request, response);
	}

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
    	ItemServico is = isd.findById(id,ItemServico.class);
    	
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);

        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ORDENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"ListaOrdem\">Ordens de Serviço</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i><a href=\"ControleItemServico?cmd=listar&id="+ is.getOrdemDeServico().getIdOrdemDeServico() +"\">Serviços Alocados</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i>Pe�as Utilizadas</li>");
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
        pw.println("<th><i class=\"fa fa-wrench\"></i> Pe�a</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
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
			        pw.println("<td>");
			        pw.println("<div class=\"btn-group\">");
			        pw.println("<a class=\"btn btn-primary\" href=\"./ControleItemPeca?cmd=alterar&id=" + is.getIdItemServico() + "&peca="+ p.getIdPeca() +"\"><i class=\"icon_pencil\"></i></a>");
			        pw.println("<a class=\"btn btn-danger\" href=\"./ControleItemPeca?cmd=deletar&id=" + is.getIdItemServico() + "&peca="+ p.getIdPeca() +"\"><i class=\"icon_close_alt2\"></i></a>");
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

        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
        
        GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
        GenericDao<Peca> pd = new GenericDao<Peca>();
        ItemServico is = isd.findById(Integer.parseInt(request.getParameter("id")), ItemServico.class);
        Peca p = pd.findById(Integer.parseInt(request.getParameter("peca")), Peca.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> ITENS DE SERVIÇO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"ListaOrdem\">Ordens de Serviço</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i><a href=\"ControleItemServico?cmd=listar&id="+ is.getOrdemDeServico().getIdOrdemDeServico() +"\">Serviços Alocados</a></li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControleItemPeca?Atualizar\">");
        pw.println("<div class=\"form-group \">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"atualizar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ is.getIdItemServico() +"\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"peca\" value=\""+ p.getIdPeca() +"\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Pe�a <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"p\" id=\"p\">");
        try {
			for(Peca peca : pd.findAll(Peca.class)){
				if(peca.equals(p)){
					pw.println("<option value=\""+peca.getIdPeca()+"\" selected>"+peca.getNome()+"</option>");
				}else{
					pw.println("<option value=\""+peca.getIdPeca()+"\">"+peca.getNome()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        pw.println("</select>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Alterar</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleItemPeca?cmd=listar&id=" + is.getIdItemServico() + "';\" type=\"button\">Voltar</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void atualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        
        GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
    	ItemServico is = isd.findById(Integer.parseInt(request.getParameter("id")), ItemServico.class);
        
        try
        {   
        	GenericDao<Peca> pd = new GenericDao<Peca>();
        	Peca p1 = pd.findById(Integer.parseInt(request.getParameter("peca")), Peca.class);
        	Peca p2 = pd.findById(Integer.parseInt(request.getParameter("p")), Peca.class);
        	
        	
        	is.remover(p1);
        	is.adicionar(p2);
        	
        	pd.update(p1);
        	pd.update(p2);
        	isd.update(is);
        	
        	calculaValor();
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("/ControleItemPeca?cmd=listar&id="+is.getIdItemServico());
            rd.include(request, response);
			out.close();
        }
	}
	
	protected void formulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
        
        GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
        ItemServico is = isd.findById(Integer.parseInt(request.getParameter("id")), ItemServico.class);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> Itens de Serviço</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"ListaOrdem\">Ordens de Serviço</a></li>");
        pw.println("<li><i class=\"fa fa-wrench\"></i><a href=\"ControleItemServico?cmd=listar&id="+ is.getOrdemDeServico().getIdOrdemDeServico() +"\">Serviços Alocados</a></li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"post\" action=\"ControleItemPeca\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ request.getParameter("id") +"\">");
        pw.println("<input type=\"hidden\" id=\"ordem\" name=\"ordem\" value=\""+ request.getParameter("ordem") +"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome da Peça</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"peca\" id=\"peca\">");
        
        try {
			GenericDao<Peca> pd = new GenericDao<Peca>();
			List<Peca> l = pd.findAll(Peca.class);
			List<Peca> lista = new ArrayList<Peca>();
			
			for(Peca peca : l){
				if(!lista.contains(peca)){
					lista.add(peca);
				}
			}
			
			for(Peca peca : lista){
				pw.println("<option value=\""+peca.getIdPeca()+"\">"+peca.getNome()+"</option>");
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
        
        request.getRequestDispatcher("/base2.html").include(request, response);
	}
}
