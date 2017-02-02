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

import org.hibernate.exception.ConstraintViolationException;

import entity.Cliente;
import entity.Endereco;
import entity.ItemServico;
import entity.Peca;
import entity.Veiculo;
import persistence.GenericDao;

/**
 * Servlet implementation class CadastroCliente
 */
@WebServlet("/ControleCliente")
public class ControleCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleCliente() {
        super();
        // TODO Auto-generated constructor stub
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
			mesclar(request,response,Integer.parseInt(request.getParameter("id")),Integer.parseInt(request.getParameter("peca")));
		}else if(cmd.equalsIgnoreCase("atualizar")){
			atualizar(request,response,Integer.parseInt(request.getParameter("id")));
		}else if(cmd.equalsIgnoreCase("selecionar")){
			selecionar(request,response,Integer.parseInt(request.getParameter("id")));
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		try{
			Cliente c = new Cliente();
			c.setNome(request.getParameter("nome"));
			c.setCpf(Long.parseLong(request.getParameter("cpf")));
			c.setEmail(request.getParameter("email"));
			c.setTelefone(Long.parseLong(request.getParameter("telefone")));
			c.setCelular(Long.parseLong(request.getParameter("celular")));
			
			Endereco e = new Endereco();
			e.setRua(request.getParameter("rua"));
			e.setNumero(Integer.parseInt(request.getParameter("numero")));
			e.setLogradouro(request.getParameter("logradouro"));
			e.setBairro(request.getParameter("bairro"));
			e.setCidade(request.getParameter("cidade"));
			e.setEstado(request.getParameter("estado"));
			e.setCep(Integer.parseInt(request.getParameter("cep")));
			
			c.setEndereco(e);
			
			GenericDao<Cliente> cd = new GenericDao<Cliente>();
			cd.create(c);
			resposta = "Dados Armazenados";
		}catch(NumberFormatException ex){
			resposta = "Valor Inv�lido";
			ex.printStackTrace();
		}catch(ConstraintViolationException ex){
			resposta = "J� Existe um Cliente cadastrado com esse nome";
			ex.printStackTrace();
		}catch(Exception ex){
			resposta = ex.getMessage();
			ex.printStackTrace();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("/cliente.html");
        rd.include(request, response);
	}

	protected void deletar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		String resposta;
		GenericDao<Cliente> cd = new GenericDao<Cliente>();
		GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
		GenericDao<Endereco> ed = new GenericDao<Endereco>();
		
         try
        {
            Cliente c = cd.findById(id, Cliente.class);
            if(c.getVeiculos() != null){
            	for(Veiculo v : c.getVeiculos()){
            		vd.delete(v);
            	}
            }
            if(c.getEndereco()!=null){
            	ed.delete(c.getEndereco());
            }
            cd.delete(c);
            resposta = "Dados Excluidos";
        } catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         RequestDispatcher rd = null;
         out.println(resposta);
         rd = request.getRequestDispatcher("/ControleCliente?cmd=listar");
         rd.include(request, response);
	}
	

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> CLIENTES</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Clinte</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Clientes Cadastrados no Sistema");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"icon_profile\"></i> Nome do Cliente</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Email</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Telefone</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Celular</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> CPF</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> Ação</th>");
        pw.println("</tr>");
        
        try {
        	GenericDao<Cliente> cd = new GenericDao<Cliente>();
        	List<Cliente> lst = cd.findAll(Cliente.class);
        	List<Cliente> lista = new ArrayList<Cliente>();
        	
        	for(Cliente c : lst){
				if(!lista.contains(c)){
					lista.add(c);
				}
			}
        	
	        for(Cliente c : lista){
	        
		        pw.println("<tr>");
		        pw.println("<td>"+c.getNome()+"</td>");
		        pw.println("<td>"+c.getEmail()+"</td>");
		        pw.println("<td>"+c.getTelefone()+"</td>");
		        pw.println("<td>"+c.getCelular()+"</td>");
		        pw.println("<td>"+c.getCelular()+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-primary\" href=\"./ControleCliente?cmd=editar&id=" + c.getIdCliente() + "\"><i class=\"icon_pencil\"></i></a>");
		        pw.println("<a class=\"btn btn-danger\" href=\"./ControleCliente?cmd=deletar&id=" + c.getIdCliente() + "\"><i class=\"icon_close_alt2\"></i></a>");
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
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> CLIENTES</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Cliente</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12 col-lg-10\">");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleCliente?cmd=alterar&id=" + id + "';\" type=\"button\">Alterar Dados</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleCliente?cmd=selecionar&id=" + id + "';\" type=\"button\">Atribuir Veiculos a outro Cliente</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/base2.html").include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
		
        GenericDao<Cliente> cd = new GenericDao<Cliente>();
        Cliente c = cd.findById(id, Cliente.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> CLIENTES</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Registro</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Cliente</li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControleCliente?Atualizar\">");
        pw.println("<h5>Dados Basicos</h5>");
        pw.println("<div class=\"form-group \">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"atualizar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Nome <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"nome\" name=\"nome\" type=\"text\" value=\""+ c.getNome() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">CPF <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cpf\" type=\"text\" name=\"cpf\" value=\""+ c.getCpf() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Email <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"email\" type=\"text\" name=\"email\" value=\""+ c.getEmail() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Telefone <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"telefone\" type=\"text\" name=\"telefone\" value=\""+ c.getTelefone() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Celular <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"celular\" type=\"text\" name=\"celular\" value=\""+ c.getCelular() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<h5> Endereço </h5>");
        pw.println("<hr/>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Rua <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"rua\" type=\"text\" name=\"rua\" value=\""+ c.getEndereco().getRua() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Numero <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"numero\" type=\"text\" name=\"numero\" value=\""+ c.getEndereco().getNumero() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Logradouro <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"logradouro\" type=\"text\" name=\"logradouro\" value=\""+ c.getEndereco().getLogradouro() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Bairro <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"bairro\" type=\"text\" name=\"bairro\" value=\""+ c.getEndereco().getBairro() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Cidade <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cidade\" type=\"text\" name=\"cidade\" value=\""+ c.getEndereco().getCidade() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Estado <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"estado\" type=\"text\" name=\"estado\" value=\""+ c.getEndereco().getEstado() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">CEP <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cep\" type=\"text\" name=\"cep\" value=\""+ c.getEndereco().getCep() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Atualizar</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleCliente?cmd=editar&id=" + id + "';\" type=\"button\">Voltar</button>");
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
	
	protected void selecionar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> CLIENTES</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Cliente</li>");
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
	
	protected void mesclar(HttpServletRequest request, HttpServletResponse response, Integer id, Integer cliente) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        try
        {   
        	GenericDao<Cliente> cd = new GenericDao<Cliente>();
        	GenericDao<Endereco> ed = new GenericDao<Endereco>();
        	GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
        	Cliente c1 = cd.findById(id, Cliente.class);
        	Cliente c2 = cd.findById(cliente, Cliente.class);
        	List<Veiculo> lista = c1.getVeiculos();
        	if(c1.getVeiculos()!= null){
	        	for(Veiculo v : lista){
	        		c2.adicionar(v);
	        		v.setCliente(c2);
	        		vd.update(v);
	        	}
        	}
        	if(c1.getEndereco()!=null){
        		ed.delete(c1.getEndereco());
        		c1.setEndereco(null);
        	}
        	cd.update(c1);
        	cd.delete(c1);
        	cd.update(c2);
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("/ControleCliente?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
	
	protected void atualizar(HttpServletRequest request, HttpServletResponse response, Integer id) throws ServletException, IOException {
		String resposta = "";
		response.setContentType("text/html;charset=ISO-8859-1");
        PrintWriter out = response.getWriter();
        try
        {   
        	GenericDao<Cliente> cd = new GenericDao<Cliente>();
        	Cliente c = cd.findById(Integer.parseInt(request.getParameter("id")), Cliente.class);
        	
        	c.setNome(request.getParameter("nome"));
        	c.setCpf(Long.parseLong(request.getParameter("cpf")));
        	c.setEmail(request.getParameter("email"));
        	c.setTelefone(Long.parseLong(request.getParameter("telefone")));
        	c.setCelular(Long.parseLong(request.getParameter("celular")));
        	
        	Endereco e = new Endereco();
        	
        	e.setRua(request.getParameter("rua"));
        	e.setNumero(Integer.parseInt(request.getParameter("numero")));
        	e.setLogradouro(request.getParameter("logradouro"));
        	e.setBairro(request.getParameter("bairro"));
        	e.setCidade(request.getParameter("cidade"));
        	e.setEstado(request.getParameter("estado"));
        	e.setCep(Integer.parseInt(request.getParameter("cep")));

        	c.setEndereco(e);
        	
        	cd.update(c);
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("/ControleCliente?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
}
