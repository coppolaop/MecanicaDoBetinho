package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.exception.ConstraintViolationException;

import persistence.GenericDao;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import entity.Endereco;
import entity.ItemServico;
import entity.OrdemDeServico;
import entity.Peca;
import entity.Usuario;
import entity.Veiculo;

@WebServlet("/adm/ControleUsuario")
public class ControleUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ControleUsuario() {
        super();
    }
    
    public boolean checkLetters(String str) 
    {
        return str.matches("[/s ' a-zA-Z�-��-�]+");
    }
    
    public boolean checkCPF(String CPF){
    	// considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") || CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
           (CPF.length() != 11))
           return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

    // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
    // Calculo do 1o. Digito Verificador
          sm = 0;
          peso = 10;
          for (i=0; i<9; i++) {              
    // converte o i-esimo caractere do CPF em um numero:
    // por exemplo, transforma o caractere '0' no inteiro 0         
    // (48 eh a posicao de '0' na tabela ASCII)         
            num = (int)(CPF.charAt(i) - 48); 
            sm = sm + (num * peso);
            peso = peso - 1;
          }

          r = 11 - (sm % 11);
          if ((r == 10) || (r == 11))
             dig10 = '0';
          else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

    // Calculo do 2o. Digito Verificador
          sm = 0;
          peso = 11;
          for(i=0; i<10; i++) {
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
          }

          r = 11 - (sm % 11);
          if ((r == 10) || (r == 11))
             dig11 = '0';
          else dig11 = (char)(r + 48);

    // Verifica se os digitos calculados conferem com os digitos informados.
          if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
             return(true);
          else return(false);
        } catch (InputMismatchException erro) {
            return(false);
        }
      }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(cmd.equalsIgnoreCase("gravar")){
			gravar(request,response);
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
		}else if(cmd.equalsIgnoreCase("endereco")){
			endereco(request,response);
		}else if(cmd.equalsIgnoreCase("confirmacao")){
			confirmacao(request,response);
		}else if(cmd.equalsIgnoreCase("selecionar")){
			selecionar(request,response);
		}else if(cmd.equalsIgnoreCase("mesclar")){
			mesclar(request,response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(cmd.equalsIgnoreCase("gravar")){
			gravar(request,response);
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
		}else if(cmd.equalsIgnoreCase("endereco")){
			endereco(request,response);
		}else if(cmd.equalsIgnoreCase("confirmacao")){
			confirmacao(request,response);
		}else if(cmd.equalsIgnoreCase("selecionar")){
			selecionar(request,response);
		}else if(cmd.equalsIgnoreCase("mesclar")){
			mesclar(request,response);
		}
	}
	
	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/adm/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> USU�RIOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Usu�rios</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Usuarios Cadastrados no Sistema");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"icon_profile\"></i> Nome Completo</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Username</th>");
        pw.println("<th><i class=\"fa fa-envelope-o\"></i> Email</th>");
        pw.println("<th><i class=\"fa fa-users\"></i> Perfil</th>");
        pw.println("<th><i class=\"fa fa-phone\"></i> Telefone</th>");
        pw.println("<th><i class=\"fa fa-mobile\"></i> Celular</th>");
        pw.println("<th><i class=\"icon_id\"></i> CPF</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> A��o</th>");
        pw.println("</tr>");
        
        try {
        	GenericDao<Usuario> ud = new GenericDao<Usuario>();
        	List<Usuario> lst = ud.findAll(Usuario.class);
        	List<Usuario> lista = new ArrayList<Usuario>();
        	
        	for(Usuario u : lst){
				if(!lista.contains(u)){
					lista.add(u);
				}
			}
        	
	        for(Usuario u : lista){
	        
		        pw.println("<tr>");
		        pw.println("<td>"+u.getNome()+"</td>");
		        pw.println("<td>"+u.getUsername()+"</td>");
		        pw.println("<td>"+u.getEmail()+"</td>");
		        if(u.getPerfil().equalsIgnoreCase("adm")){
		        	pw.println("<td>Administrador</td>");
		        }else if(u.getPerfil().equalsIgnoreCase("usu")){
		        	pw.println("<td>Usu�rio</td>");	
		        }else{
		        	pw.println("<td>Cliente</td>");
		        }
		        pw.println("<td>"+u.getTelefone()+"</td>");
		        pw.println("<td>"+u.getCelular()+"</td>");
		        pw.println("<td>"+u.getCpf()+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-info\" href=\"./ControleUsuario?cmd=endereco&id=" + u.getEndereco().getIdEndereco() + "\"><i class=\"icon_info_alt\"></i></a>");
		        pw.println("<a class=\"btn btn-primary\" href=\"./ControleUsuario?cmd=alterar&id=" + u.getIdUsuario() + "\"><i class=\"icon_pencil\"></i></a>");
		        if(u.getPerfil().equals("cli")){
		        	pw.println("<a class=\"btn btn-danger\" href=\"./ControleUsuario?cmd=confirmacao&id=" + u.getIdUsuario() + "\"><i class=\"icon_close_alt2\"></i></a>");
		        }else{
		        	pw.println("<a class=\"btn btn-danger\" href=\"./ControleUsuario?cmd=deletar&id=" + u.getIdUsuario() + "\"><i class=\"icon_close_alt2\"></i></a>");
		        }
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

        request.getRequestDispatcher("/adm/base2.html").include(request, response);
	}
	
	protected void gravar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		try{
			if(!checkLetters(request.getParameter("nome"))){
				throw new Exception("Um Nome n�o pode possuir n�meros");
			}
			
			if(!checkCPF(request.getParameter("cpf"))&&(!request.getParameter("cpf").equalsIgnoreCase(""))){
				throw new Exception("Digite um CPF V�lido!");
			}
			
			Usuario u = new Usuario();
			u.setNome(request.getParameter("nome"));
			if(!request.getParameter("cpf").equalsIgnoreCase("")){
				u.setCpf(Long.parseLong(request.getParameter("cpf")));
			}
			u.setEmail(request.getParameter("email"));
			u.setTelefone(Long.parseLong(request.getParameter("telefone")));
			u.setCelular(Long.parseLong(request.getParameter("celular")));
			u.setUsername(request.getParameter("username"));
			u.setSenha("senhapadrao");//Usuario deve trocar a senha padr�o ao primeiro Login
			u.setPerfil(request.getParameter("perfil"));
			Criptografia.criptografia(u);
			
			Endereco e = new Endereco();
			e.setRua(request.getParameter("rua"));
			e.setNumero(Integer.parseInt(request.getParameter("numero")));
			e.setLogradouro(request.getParameter("logradouro"));
			e.setBairro(request.getParameter("bairro"));
			e.setCidade(request.getParameter("cidade"));
			e.setEstado(request.getParameter("estado"));
			e.setCep(Integer.parseInt(request.getParameter("cep")));
			
			u.setEndereco(e);
			
			GenericDao<Usuario> ud = new GenericDao<Usuario>();
			ud.create(u);
			resposta = "Dados Armazenados";
		}catch(NumberFormatException ex){
			resposta = "Valor Inv�lido";
			ex.printStackTrace();
		}catch(ConstraintViolationException ex){
			resposta = "J� Existe um Usuario cadastrado com esse nome";
			ex.printStackTrace();
		}catch(MySQLIntegrityConstraintViolationException ex){
			resposta = "J� Existe um Usuario cadastrado com esse nome";
			ex.printStackTrace();
		}catch(Exception ex){
			resposta = ex.getMessage();
			ex.printStackTrace();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("./ControleUsuario?cmd=formulario");
        rd.include(request, response);
	}
	
	protected void deletar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<Usuario> ud = new GenericDao<Usuario>();
		GenericDao<Endereco> ed = new GenericDao<Endereco>();
		
        try
        {
        	 Usuario u = ud.findById(id, Usuario.class);
        	 if(u.getPerfil().equals("cli")){
        		 
        		GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
    			GenericDao<OrdemDeServico> od = new GenericDao<OrdemDeServico>();
    			GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
    			GenericDao<Peca> pd = new GenericDao<Peca>();
    		 
        		 if(u.getVeiculos() != null){
                 	List<Veiculo> veiculos = new ArrayList<Veiculo>();
                 	for(Veiculo v : u.getVeiculos()){
                 		veiculos.add(v);
                 	}
                 	for(Veiculo v : veiculos){
                 		if(v!=null){
     	            		if(v.getOrdensDeServico()!=null){
     	            			List<OrdemDeServico> ordens = new ArrayList<OrdemDeServico>();
     	            			for(OrdemDeServico o : v.getOrdensDeServico()){
     	            				ordens.add(o);
     	            			}
     	            			for(OrdemDeServico o : ordens){
     	            				if(o!=null){
     		            				if(o.getItensServico()!=null){
     		            					List<ItemServico> itens = new ArrayList<ItemServico>();
     		            					for(ItemServico is : o.getItensServico()){
     		            						itens.add(is);
     		            					}
     		            					for(ItemServico is : itens){
     		            						if(is!=null){
     			            						if(is.getPecas()!=null){
     			            							List<Peca> pecas = new ArrayList<Peca>();
     			            							for(Peca p : is.getPecas()){
     			            								pecas.add(p);
     			            							}
     			            							for(Peca p : pecas){
     			            								if(p!=null){
     				            								p.remover(is);
     				            								is.remover(p);
     				            								pd.update(p);
     				            								isd.update(is);
     			            								}
     			            							}
     			            						}
     			            						o.remover(is);
     			            						is.setOrdemDeServico(null);
     			            						od.update(o);
     			            						isd.update(is);
     			            						isd.delete(is);
     			            					}
     		            					}
     		            					v.remover(o);
     		            					o.setVeiculo(null);
     		            					od.update(o);
     		            					vd.update(v);
     		            					od.delete(o);
     		            				}
     		            			}
     	            			}
     	            			u.remover(v);
     	            			v.setCliente(null);
     	            			vd.update(v);
     	            			ud.update(u);
     	            			vd.delete(v);
     	            		}
     	            	}
                 	}
                 }
                 if(u.getEndereco()!=null){
                 	Endereco e = u.getEndereco();
                 	u.setEndereco(null);
                 	ud.update(u);
                 	ed.delete(e);
                 }
                 ud.delete(u);
                 resposta = "Dados Excluidos";
        	 }else{
	        	 if(u.getEndereco()!=null){
	        		 Endereco e = u.getEndereco();
	        		 u.setEndereco(null);
	        		 ud.update(u);
	        		 ed.delete(e);
	        	 }
	        	 ud.delete(u);
	        	 resposta = "Dados Excluidos";
        	 }
        }catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("./ControleUsuario?cmd=listar");
        rd.include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Integer id = Integer.parseInt(request.getParameter("id"));
        request.getRequestDispatcher("/adm/base1.html").include(request, response);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        GenericDao<Usuario> ud = new GenericDao<Usuario>();
        Usuario u = ud.findById(id, Usuario.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> USU�RIOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleUsuario?cmd=listar\">Usu�rio</a></li>");
        pw.println("<li><i class=\"fa fa-files-o\"></i>Alterar</li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControleUsuario?Atualizar\" accept-charset='UTF-8'>");
        pw.println("<h5>Dados Basicos</h5>");
        pw.println("<div class=\"form-group \">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"atualizar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Nome Completo <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"nome\" name=\"nome\" type=\"text\" value=\""+ u.getNome() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Username <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"username\" type=\"text\" name=\"username\" value=\""+ u.getUsername() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Perfil <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"perfil\" id=\"perfil\">");
        if(u.getPerfil().equalsIgnoreCase("adm")){
        	pw.println("<option value=\"adm\" selected>Administrador</option>");
        	pw.println("<option value=\"usu\">Usuario</option>");
        	pw.println("<option value=\"cli\">Cliente</option>");
        }else if(u.getPerfil().equalsIgnoreCase("cli")){
        	pw.println("<option value=\"adm\">Administrador</option>");
        	pw.println("<option value=\"usu\">Usuario</option>");
        	pw.println("<option value=\"cli\" selected>Cliente</option>");
        }else{
        	pw.println("<option value=\"adm\">Administrador</option>");
        	pw.println("<option value=\"usu\" selected>Usuario</option>");
        	pw.println("<option value=\"cli\">Cliente</option>");
        }
        pw.println("</select>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">CPF </label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cpf\" type=\"text\" name=\"cpf\" value=\""+ u.getCpf() +"\"/>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Email </label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"email\" type=\"text\" name=\"email\" value=\""+ u.getEmail() +"\" />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Telefone <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"telefone\" type=\"text\" name=\"telefone\" value=\""+ u.getTelefone() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Celular <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"celular\" type=\"text\" name=\"celular\" value=\""+ u.getCelular() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<h5> Endere�o </h5>");
        pw.println("<hr/>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Rua <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"rua\" type=\"text\" name=\"rua\" value=\""+ u.getEndereco().getRua() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">N�mero <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"numero\" type=\"text\" name=\"numero\" value=\""+ u.getEndereco().getNumero() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Tipo de Logradouro <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"logradouro\" type=\"text\" name=\"logradouro\" value=\""+ u.getEndereco().getLogradouro() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Bairro <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"bairro\" type=\"text\" name=\"bairro\" value=\""+ u.getEndereco().getBairro() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Cidade <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cidade\" type=\"text\" name=\"cidade\" value=\""+ u.getEndereco().getCidade() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Estado <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"estado\" type=\"text\" name=\"estado\" value=\""+ u.getEndereco().getEstado() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">CEP <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cep\" type=\"text\" name=\"cep\" value=\""+ u.getEndereco().getCep() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Atualizar</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleUsuario?cmd=listar';\" type=\"button\">Voltar</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/adm/base2.html").include(request, response);
	}
	
	protected void atualizar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		Integer id = Integer.parseInt(request.getParameter("id"));
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {   
        	GenericDao<Usuario> ud = new GenericDao<Usuario>();
        	Usuario u = ud.findById(Integer.parseInt(request.getParameter("id")), Usuario.class);
        	
        	if(!checkLetters(request.getParameter("nome"))){
				throw new Exception("Um Nome n�o pode possuir n�meros");
			}
        	
        	if(!checkCPF(request.getParameter("cpf"))&&(!request.getParameter("cpf").equalsIgnoreCase(""))){
				throw new Exception("Digite um CPF V�lido!");
			}
        	
        	u.setNome(request.getParameter("nome"));
        	if(!request.getParameter("cpf").equalsIgnoreCase("")){
        		u.setCpf(Long.parseLong(request.getParameter("cpf")));
        	}
        	u.setEmail(request.getParameter("email"));
        	u.setTelefone(Long.parseLong(request.getParameter("telefone")));
        	u.setCelular(Long.parseLong(request.getParameter("celular")));
        	u.setUsername(request.getParameter("username"));
        	u.setPerfil(request.getParameter("perfil"));
        	
        	Endereco e = new Endereco();
        	
        	e.setRua(request.getParameter("rua"));
        	e.setNumero(Integer.parseInt(request.getParameter("numero")));
        	e.setLogradouro(request.getParameter("logradouro"));
        	e.setBairro(request.getParameter("bairro"));
        	e.setCidade(request.getParameter("cidade"));
        	e.setEstado(request.getParameter("estado"));
        	e.setCep(Integer.parseInt(request.getParameter("cep")));

        	u.setEndereco(e);
        	
        	ud.update(u);
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("./ControleUsuario?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
	
	protected void formulario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/adm/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"icon_document_alt\"></i> USU�RIO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_profile\"></i>Novo Usu�rio</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Usu�rio");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"post\" action=\"./ControleUsuario\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"gravar\">");
        pw.println("<h5>Dados Basicos</h5>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"nome\" class=\"control-label col-lg-2\">Nome Completo <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"nome\" name=\"nome\" minlength=\"5\" type=\"text\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"username\" class=\"control-label col-lg-2\">Username <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"username\" type=\"text\" name=\"username\" required/>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Perfil <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"perfil\" id=\"perfil\">");
        pw.println("<option value=\"adm\">Administrador</option>");
        pw.println("<option value=\"usu\" selected>Usu�rio</option>");
        pw.println("<option value=\"cli\">Cliente</option>");
        pw.println("</select>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"cpf\" class=\"control-label col-lg-2\">CPF </label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cpf\" type=\"text\" name=\"cpf\"/>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"email\" class=\"control-label col-lg-2\">Email </label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"email\" type=\"text\" name=\"email\" />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"telefone\" class=\"control-label col-lg-2\">Telefone <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"telefone\" type=\"text\" name=\"telefone\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"celular\" class=\"control-label col-lg-2\">Celular <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"celular\" type=\"text\" name=\"celular\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<h5> Endere�o </h5>");
        pw.println("<hr/>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"rua\" class=\"control-label col-lg-2\">Rua <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"rua\" type=\"text\" name=\"rua\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"numero\" class=\"control-label col-lg-2\">N�mero <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"numero\" type=\"text\" name=\"numero\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"logradouro\" class=\"control-label col-lg-2\">Tipo de Logradouro </label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"logradouro\" type=\"text\" name=\"logradouro\" />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"bairro\" class=\"control-label col-lg-2\">Bairro <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"bairro\" type=\"text\" name=\"bairro\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"cidade\" class=\"control-label col-lg-2\">Cidade <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cidade\" type=\"text\" name=\"cidade\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"estado\" class=\"control-label col-lg-2\">Estado <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"estado\" type=\"text\" name=\"estado\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"cep\" class=\"control-label col-lg-2\">CEP <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cep\" type=\"text\" name=\"cep\" required />");
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
        
        request.getRequestDispatcher("/adm/base2.html").include(request, response);
	}
	
	protected void endereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Integer id = Integer.parseInt(request.getParameter("id"));
        request.getRequestDispatcher("/adm/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ENDERE�O</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleUsuario?cmd=listar\">Usuario</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Endereco</li>");
        
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
        pw.println("<th><i class=\"icon_profile\"></i> Rua</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> Numero</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Tipo de Logradouro</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Bairro</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Cidade</th>");
        pw.println("<th><i class=\"icon_profile\"></i> Estado</th>");
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> CEP</th>");
        pw.println("</tr>");
        
		try {
			GenericDao<Endereco> ed = new GenericDao<Endereco>();
			
			Endereco e = ed.findById(id, Endereco.class);
			
			pw.println("<tr>");
	        pw.println("<td>"+e.getRua()+"</td>");
	        pw.println("<td>"+e.getNumero()+"</td>");
	        pw.println("<td>"+e.getLogradouro()+"</td>");
	        pw.println("<td>"+e.getBairro()+"</td>");
	        pw.println("<td>"+e.getCidade()+"</td>");
	        pw.println("<td>"+e.getEstado()+"</td>");
	        pw.println("<td>"+e.getCep()+"</td>");
	        pw.println("</tr>");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

        pw.println("</tbody>");
        pw.println("</table>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");

        request.getRequestDispatcher("/adm/base2.html").include(request, response);
	}
	
	protected void confirmacao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Integer id = Integer.parseInt(request.getParameter("id"));
        request.getRequestDispatcher("/adm/base1.html").include(request, response);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> CLIENTES</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Usu�rio</li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Exclus�o</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12 col-lg-10\">");
        pw.println("<button class=\"btn btn-danger\" onclick=\"location.href='./ControleUsuario?cmd=deletar&id=" + id + "';\" type=\"button\">Excluir Permanentemente o Cliente, seus Ve�culos e suas Ordens de Servi�o</button>");
        pw.println("<button class=\"btn btn-danger\" onclick=\"location.href='./ControleUsuario?cmd=selecionar&id=" + id + "';\" type=\"button\">Atribuir Veiculos a outro Cliente e Excluir seus dados pessoais</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleUsuario?cmd=listar';\" type=\"button\">Voltar</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/adm/base2.html").include(request, response);
	}
	
	protected void selecionar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Integer id = Integer.parseInt(request.getParameter("id"));
        request.getRequestDispatcher("/adm/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> CLIENTES</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Usu�rio</li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Exclus�o</li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleUsuario\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"mesclar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome Completo do Cliente</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"cliente\" id=\"cliente\">");
        
        try {
        	
        	GenericDao<Usuario> cd = new GenericDao<Usuario>();
        	Usuario c = cd.findById(id, Usuario.class);

			List<Usuario> lista = cd.findAll(Usuario.class);
			
			for(Usuario cli : lista){
				if(cli.getPerfil().endsWith("cli")&&!cli.getIdUsuario().equals(id)){
					pw.println("<option value=\""+cli.getIdUsuario()+"\">"+cli.getNome()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-Danger\" type=\"submit\">Selecionar Cliente</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</form>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        
        request.getRequestDispatcher("/adm/base2.html").include(request, response);
	}
	
	protected void mesclar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		Integer id = Integer.parseInt(request.getParameter("id"));
		Integer cliente = Integer.parseInt(request.getParameter("cliente"));
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try
        {   
        	GenericDao<Usuario> cd = new GenericDao<Usuario>();
        	GenericDao<Veiculo> vd = new GenericDao<Veiculo>();
        	GenericDao<Endereco> ed = new GenericDao<Endereco>();
        	Usuario c1 = cd.findById(id, Usuario.class);
        	Usuario c2 = cd.findById(cliente, Usuario.class);
        	if(c1.getVeiculos()!= null){
        		List<Veiculo> lista = new ArrayList<Veiculo>();
        		for(Veiculo v : c1.getVeiculos()){
        			lista.add(v);
        		}
        		for(Veiculo v : lista){
	        		if(v!=null){
	        			c1.remover(v);
		        		c2.adicionar(v);
		        		v.setCliente(c2);
		        		vd.update(v);
		        		cd.update(c1);
		        		cd.update(c2);
	        		}
	        	}
        	}
        	resposta = "Dados Alterados";
        	
        	if(c1.getEndereco()!=null){
	       		 Endereco e = c1.getEndereco();
	       		 c1.setEndereco(null);
	       		 cd.update(c1);
	       		 ed.delete(e);
       	 	}
	       	 cd.delete(c1);
	       	 resposta += " e Excluidos";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("./ControleUsuario?cmd=listar");
            rd.include(request, response);
			out.close();
        }
	}
}
