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
import entity.OrdemDeServico;
import entity.Peca;
import entity.Veiculo;

/**
 * Servlet implementation class CadastroPeca
 */
@WebServlet("/ControlePeca")
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
			deletar(request,response,Integer.parseInt(request.getParameter("id")));
		}else if(cmd.equalsIgnoreCase("editar")){
			editar(request,response,Integer.parseInt(request.getParameter("id")));
		}else if(cmd.equalsIgnoreCase("alterar")){
			alterar(request,response,Integer.parseInt(request.getParameter("id")));
		}else if(cmd.equalsIgnoreCase("mesclar")){
			mesclar(request,response,Integer.parseInt(request.getParameter("id")));
		}else if(cmd.equalsIgnoreCase("atualizar")){
			atualizar(request,response,Integer.parseInt(request.getParameter("id")));
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
			p.setValor(Double.parseDouble(request.getParameter("valor")));
			
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
        rd = request.getRequestDispatcher("/peca.html");
        rd.include(request, response);
	}
	
	protected void deletar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		String resposta;
		GenericDao<Peca> pd = new GenericDao<Peca>();
		
         try
        {
            Peca p = pd.findById(id, Peca.class);
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
         rd = request.getRequestDispatcher("/ControlePeca?cmd=listar");
         rd.include(request, response);
	}
	

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> PEÇAS</h3>");
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
        pw.println("Peças Cadastradas no Sistema");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"icon_profile\"></i> Nome da Peça</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Valor</th>");
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

        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void editar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> PEÇAS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Ordens de Serviço</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12 col-lg-10\">");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControlePeca?cmd=alterar&id=" + id + "';\" type=\"button\">Alterar Dados</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControlePeca?cmd=mesclar&id=" + id + "';\" type=\"button\">Substituir por pe�a existente</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
		
        GenericDao<Peca> pd = new GenericDao<Peca>();
        Peca p = pd.findById(id, Peca.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> Peça</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Alterar Peça</li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControlePeca?Atualizar\">");
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
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Agendar</button>");
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
        
        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void mesclar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		
	}
	
	protected void atualizar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        try
        {   
        	GenericDao<Peca> pd = new GenericDao<Peca>();
        	Peca p = pd.findById(Integer.parseInt(request.getParameter("id")), Peca.class);
        	
        	p.setNome(request.getParameter("nome"));
        	p.setValor(Double.parseDouble(request.getParameter("valor")));
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
            rd = request.getRequestDispatcher("/ControlePeca?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
	
	protected void trocar(HttpServletRequest request, HttpServletResponse response, Integer id1, Integer id2) throws ServletException, IOException {
		
	}
}
