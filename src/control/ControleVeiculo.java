package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Cliente;
import entity.Endereco;
import entity.ItemServico;
import entity.OrdemDeServico;
import entity.Veiculo;
import persistence.GenericDao;

/**
 * Servlet implementation class CadastroVeiculo
 */
@WebServlet("/ControleVeiculo")
public class ControleVeiculo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleVeiculo() {
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
		}
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
			c.adicionar(v);
			
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
        rd = request.getRequestDispatcher("FormVeiculo");
        rd.include(request, response);
	}
	
	protected void deletar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<Cliente> cd = new GenericDao<Cliente>();
		GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
		GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
		
         try
        {
            Veiculo v = vd.findById(id, Veiculo.class);
            if(v.getCliente() != null){
            	v.getCliente().remover(v);
            }
            
            if(v.getOrdensDeServico()!=null){
            	for(OrdemDeServico o : v.getOrdensDeServico()){
            		od.delete(o);
            	}
            }
            vd.delete(v);
            resposta = "Dados Excluidos";
        } catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         RequestDispatcher rd = null;
         out.println(resposta);
         rd = request.getRequestDispatcher("/ControleVeiculo?cmd=listar");
         rd.include(request, response);
	}
	

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> VEICULOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Veiculo</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Veiculo Cadastrados no Sistema");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"icon_profile\"></i> Placa do Veiculo</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Nome do Cliente</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Descrição</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Numero de Ordens de Servico</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");
        
        try {
        	GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
        	List<Veiculo> lst = vd.findAll(Veiculo.class);
        	List<Veiculo> lista = new ArrayList<Veiculo>();
        	
        	for(Veiculo v : lst){
				if(!lista.contains(v)){
					lista.add(v);
				}
			}
        	
	        for(Veiculo v : lista){
	        
		        pw.println("<tr>");
		        pw.println("<td>"+v.getPlaca()+"</td>");
		        pw.println("<td>"+v.getCliente().getNome()+"</td>");
		        pw.println("<td>"+v.getDescricao()+"</td>");
		        List<OrdemDeServico> quantidade = new ArrayList<OrdemDeServico>();
		        for(OrdemDeServico o : v.getOrdensDeServico()){
		        	if(o!=null&!(quantidade.contains(o))){//ignorando os diversos valores nulos que sao trazidos na consulta
		        		quantidade.add(o);
		        	}
		        }
		        pw.println("<td>"+quantidade.size()+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-primary\" href=\"./ControleVeiculo?cmd=editar&id=" + v.getIdVeiculo() + "\"><i class=\"icon_pencil\"></i></a>");
		        pw.println("<a class=\"btn btn-danger\" href=\"./ControleVeiculo?cmd=deletar&id=" + v.getIdVeiculo() + "\"><i class=\"icon_close_alt2\"></i></a>");
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
	
	protected void editar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> VEICULOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Serviço</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Ordens de Serviço</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12 col-lg-10\">");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleVeiculo?cmd=alterar&id=" + id + "';\" type=\"button\">Alterar Dados</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleVeiculo?cmd=selecionar&id=" + id + "';\" type=\"button\">Atribuir Ordens de Servico a outro Veiculo</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
		
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        GenericDao<Cliente> cd = new GenericDao<Cliente>();
        GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
        Veiculo v = vd.findById(id, Veiculo.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> VEICULOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Cadastro</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Veiculo</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Cliente");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControleVeiculo?Atualizar\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome do Cliente</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"cliente\" id=\"cliente\">");
        
        try {
			List<Cliente> l = cd.findAll(Cliente.class);
			List<Cliente> lista = new ArrayList<Cliente>();
			
			for(Cliente cli : l){
				if(!lista.contains(cli)){
					lista.add(cli);
				}
			}
			
			for(Cliente c : lista){
				if(v.getCliente().equals(c)){
					pw.println("<option value=\""+v.getIdVeiculo()+"\" selected>"+v.getPlaca()+"</option>");
				}else{
					pw.println("<option value=\""+v.getIdVeiculo()+"\">"+v.getPlaca()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"placa\" class=\"control-label col-lg-2\">Placa <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"placa\" name=\"placa\" minlength=\"5\" type=\"text\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"descricao\" class=\"control-label col-lg-2\">Descrição <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"descricao\" type=\"text\" name=\"descricao\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Atualizar</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleVeiculo?cmd=editar&id=" + id + "';\" type=\"button\">Voltar</button>");
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
	
	protected void selecionar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> CLIENTES</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Servi�o</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Agendamento</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Atribuir Veiculos a outro Cliente");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleCliente\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"mesclar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome do Cliente</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"cliente\" id=\"cliente\">");
        
        try {
        	
        	GenericDao<Cliente> cd = new GenericDao<Cliente>();
            Cliente c = cd.findById(id, Cliente.class);

			List<Cliente> l = cd.findAll(Cliente.class);
			List<Cliente> lista = new ArrayList<Cliente>();
			
			for(Cliente cli : l){
				if(!lista.contains(cli)){
					lista.add(cli);
				}
			}
			
			for(Cliente cli : lista){
				if(!cli.getIdCliente().equals(id)){
					pw.println("<option value=\""+cli.getIdCliente()+"\">"+cli.getNome()+"</option>");
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
        
        
        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void mesclar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer veiculo = Integer.parseInt(request.getParameter("veiculo"));
        try
        {   
        	GenericDao<Cliente> cd = new GenericDao<Cliente>();
        	GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
        	GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
        	Veiculo v1 = vd.findById(id, Veiculo.class);
        	Veiculo v2 = vd.findById(veiculo, Veiculo.class);
        	Cliente c = v1.getCliente();
        	if(c!=null){
        		c.remover(v1);
        		v1.setCliente(null);
	        	cd.update(c);
	        }
        	if(v1.getOrdensDeServico()!=null){
        		for(OrdemDeServico o : v1.getOrdensDeServico()){
        			v2.adicionar(o);
        			o.setVeiculo(v2);
        			vd.update(v2);
        			od.update(o);
        		}
        	}
        	vd.update(v1);
        	vd.delete(v1);
        	vd.update(v2);
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("/ControleVeiculo?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
	
	protected void atualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        
        Integer id = Integer.parseInt(request.getParameter("id"));
        Integer cliente = Integer.parseInt(request.getParameter("cliente"));
        
        try
        {   
        	GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
        	Veiculo v = vd.findById(Integer.parseInt(request.getParameter("id")), Veiculo.class);
        	
        	v.setCliente(new GenericDao<Cliente>().findById(cliente, Cliente.class));
        	v.setPlaca(request.getParameter("placa"));
        	v.setDescricao(request.getParameter("descricao"));
        	
        	vd.update(v);
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("/ControleVeiculo?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
}