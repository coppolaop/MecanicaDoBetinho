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
import entity.Peca;
import entity.Veiculo;

/**
 * Servlet implementation class ControlePeca
 */
@WebServlet("/usu/ControlePeca")
public class ControlePeca extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControlePeca() {
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
		}else if(cmd.equalsIgnoreCase("mesclar")){
			mesclar(request,response);
		}else if(cmd.equalsIgnoreCase("atualizar")){
			atualizar(request,response);
		}else if(cmd.equalsIgnoreCase("selecionar")){
			selecionar(request,response);
		}else if(cmd.equalsIgnoreCase("formulario")){
			formulario(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		try{
			Peca p = new Peca();
			p.setNome(request.getParameter("nome"));
			Double valor = Double.parseDouble(request.getParameter("valor").replace(",", "."));
			p.setValor(valor);
			
			GenericDao<Peca> pd = new GenericDao<Peca>();
			pd.create(p);
			resposta = "Dados Armazenados";
		}catch(NumberFormatException ex){
			resposta = "Valor Inválido";
		}catch(Exception ex){
			resposta = ex.getMessage();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("./ControlePeca?cmd=formulario");
        rd.include(request, response);
	}
	
	protected void deletar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<Peca> pd = new GenericDao<Peca>();
		GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
		
         try
        {
            Peca p = pd.findById(id, Peca.class);
            if(p.getItensServico() != null){
            	for(ItemServico is : p.getItensServico()){
            		is.remover(p);
            		isd.update(is);
            	}
            }
            pd.delete(p);
            resposta = "Dados Excluidos";
        } catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         RequestDispatcher rd = null;
         out.println(resposta);
         rd = request.getRequestDispatcher("./ControlePeca?cmd=listar");
         rd.include(request, response);
	}
	

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> PEÇAS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Peça</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Peças Cadastradas no Sistema");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"fa fa-cog\"></i> Nome da Peça</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor</th>");
        pw.println("<th><i class=\"icon_document_alt\"></i> Número de Itens</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");
        
        try {
        	GenericDao<Peca> pd = new GenericDao<Peca>();
        	List<Peca> lst = pd.findAll(Peca.class);
        	List<Peca> lista = new ArrayList<Peca>();
        	
        	for(Peca p : lst){
				if(!lista.contains(p)){
					lista.add(p);
				}
			}
        	
	        for(Peca p : lista){
	        
		        pw.println("<tr>");
		        pw.println("<td>"+p.getNome()+"</td>");
		        NumberFormat nf = NumberFormat.getInstance();
		        nf.setMaximumFractionDigits(2);
		        nf.setMinimumFractionDigits(2);
		        pw.println("<td>R$ "+nf.format(p.getValor())+"</td>");
		        pw.println("<td>"+p.getItensServico().size()+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-primary\" href=\"./ControlePeca?cmd=editar&id=" + p.getIdPeca() + "\"><i class=\"icon_pencil\"></i></a>");
		        pw.println("<a class=\"btn btn-danger\" href=\"./ControlePeca?cmd=deletar&id=" + p.getIdPeca() + "\"><i class=\"icon_close_alt2\"></i></a>");
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
	
	protected void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        Integer id = Integer.parseInt(request.getParameter("id"));
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> PEÇAS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControlePeca?cmd=listar\">Peça</a></li>");
        pw.println("<li><i class=\"icon_pencil\"></i>Edição</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12 col-lg-10\">");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControlePeca?cmd=alterar&id=" + id + "';\" type=\"button\">Alterar Dados</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControlePeca?cmd=selecionar&id=" + id + "';\" type=\"button\">Atribuir Itens a outra Peça</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        GenericDao<Peca> pd = new GenericDao<Peca>();
        Peca p = pd.findById(id, Peca.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> Peça</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControlePeca?cmd=listar\">Peça</a></li>");
        pw.println("<li><i class=\"icon_pencil\"></i><a href=\"./ControlePeca?cmd=editar&id="+id+"\">Edição</a></li>");
        pw.println("<li><i class=\"fa fa-pencil\"></i>Alterar</li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControlePeca\" accept-charset='UTF-8'>");
        pw.println("<div class=\"form-group \">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"atualizar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Nome <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"nome\" name=\"nome\" type=\"text\" value=\""+ p.getNome() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Valor <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"valor\" type=\"text\" name=\"valor\" value=\""+ p.getValor() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Salvar</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControlePeca?cmd=editar&id=" + id + "';\" type=\"button\">Voltar</button>");
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
	
	protected void selecionar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> PEÇA</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControlePeca?cmd=listar\">Peça</a></li>");
        pw.println("<li><i class=\"icon_pencil\"></i><a href=\"./ControlePeca?cmd=editar&id="+id+"\">Edição</a></li>");
        pw.println("<li><i class=\"fa fa-pencil\"></i>Atribuir Itens a outra Peça</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Substituindo Peça por outra existente");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControlePeca\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"mesclar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome da Peça</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"peca\" id=\"peca\">");
        
        try {
        	
        	GenericDao<Peca> pd = new GenericDao<Peca>();
            Peca p = pd.findById(id, Peca.class);

			List<Peca> l = pd.findAll(Peca.class);
			List<Peca> lista = new ArrayList<Peca>();
			
			for(Peca peca : l){
				if(!lista.contains(peca)){
					lista.add(peca);
				}
			}
			
			for(Peca peca : lista){
				if(!peca.getIdPeca().equals(id)){
					pw.println("<option value=\""+peca.getIdPeca()+"\">"+peca.getNome()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Selecionar Peça</button>");
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
	
	protected void mesclar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer peca = Integer.parseInt(request.getParameter("peca"));
        try
        {   
        	GenericDao<Peca> pd = new GenericDao<Peca>();
        	GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
        	Peca p1 = pd.findById(id, Peca.class);
        	Peca p2 = pd.findById(peca, Peca.class);
        	List<ItemServico> itens = new ArrayList<ItemServico>();
        	
        	for(ItemServico item : p1.getItensServico()){
        		itens.add(item);
        	}
        	
        	if(p1.getItensServico()!= null){
	        	for(ItemServico is : itens){
	        		p1.remover(is);
	        		p2.adicionar(is);
	        		is.adicionar(p2);
	        		is.remover(p1);
	        		isd.update(is);
	        		pd.update(p1);
	        		pd.update(p2);
	        	}
        	}
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("./ControlePeca?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
	
	protected void atualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        Integer id = Integer.parseInt(request.getParameter("id"));
        try
        {   
        	GenericDao<Peca> pd = new GenericDao<Peca>();
        	Peca p = pd.findById(Integer.parseInt(request.getParameter("id")), Peca.class);
        	
        	p.setNome(request.getParameter("nome"));
        	p.setValor(Double.parseDouble(request.getParameter("valor").replace(",", ".")));
        	pd.update(p);
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("./ControlePeca?cmd=listar");
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
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> PEÇA</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"usu/index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Cadastro</li>");
        pw.println("<li><i class=\"fa fa-cog\"></i>Peça</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Peça");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControlePeca\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"gravar\">");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Nome <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"nome\" name=\"nome\" type=\"text\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Valor <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"valor\" type=\"text\" name=\"valor\" required />");
        pw.println("</div>");
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
