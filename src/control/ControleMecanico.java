package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
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
import entity.Endereco;
import entity.ItemServico;
import entity.Mecanico;
import entity.Peca;

/**
 * Servlet implementation class ControleMecanico
 */
@WebServlet("/usu/ControleMecanico")
public class ControleMecanico extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleMecanico() {
        super();
        // TODO Auto-generated constructor stub
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
		}else if(cmd.equalsIgnoreCase("endereco")){
			endereco(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		try{
			if(!checkLetters(request.getParameter("nome"))){
				throw new Exception("Um Nome n�o pode possuir n�meros");
			}
			
			if(!checkCPF(request.getParameter("cpf"))&&(!request.getParameter("cpf").equalsIgnoreCase(""))){
				throw new Exception("Digite um CPF V�lido!");
			}
			
			Mecanico m = new Mecanico();
			m.setNome(request.getParameter("nome"));
			if(!request.getParameter("cpf").equalsIgnoreCase("")){
				m.setCpf(Long.parseLong(request.getParameter("cpf")));
			}
			m.setEmail(request.getParameter("email"));
			m.setTelefone(Long.parseLong(request.getParameter("telefone")));
			m.setCelular(Long.parseLong(request.getParameter("celular")));
			
			Endereco e = new Endereco();
			e.setRua(request.getParameter("rua"));
			e.setNumero(Integer.parseInt(request.getParameter("numero")));
			e.setLogradouro(request.getParameter("logradouro"));
			e.setBairro(request.getParameter("bairro"));
			e.setCidade(request.getParameter("cidade"));
			e.setEstado(request.getParameter("estado"));
			e.setCep(Integer.parseInt(request.getParameter("cep")));
			
			m.setEndereco(e);
			
			GenericDao<Mecanico> cd = new GenericDao<Mecanico>();
			cd.create(m);
			resposta = "Dados Armazenados";
		}catch(NumberFormatException ex){
			resposta = "Valor Inv�lido";
			ex.printStackTrace();
		}catch(ConstraintViolationException ex){
			resposta = "J� Existe um Mec�nico cadastrado com esse nome";
			ex.printStackTrace();
		}catch(Exception ex){
			resposta = ex.getMessage();
			ex.printStackTrace();
		}
		response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher rd = null;
        out.println(resposta);
        rd = request.getRequestDispatcher("./ControleMecanico?cmd=formulario");
        rd.include(request, response);
	}

	protected void deletar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		Integer id = Integer.parseInt(request.getParameter("id"));
		GenericDao<Mecanico> md = new GenericDao<Mecanico>();
		GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
		GenericDao<Endereco> ed = new GenericDao<Endereco>();
		GenericDao<Peca> pd = new GenericDao<Peca>();
		
         try
        {
            Mecanico m = md.findById(id, Mecanico.class);
            
            if(m.getItensServico() != null){
            	List<ItemServico> itens = new ArrayList<ItemServico>();
            	List<Peca> pecas = new ArrayList<Peca>();
            	
            	for(ItemServico is : m.getItensServico()){
            		itens.add(is);
            		if(is!=null){
            			if(is.getPecas()!=null){
		            		for(Peca p : is.getPecas()){
		            			pecas.add(p);
		            		}
	            		}
            		}
            	}
            	
            	for(ItemServico is : itens){
            		if(is!=null){
            			if(is.getPecas()!=null){
	            			for(Peca p : pecas){
	            				p.remover(is);
	            				is.remover(p);
	            				pd.update(p);
	            				isd.update(is);
	            			}
	            		}
	            		is.setMecanico(null);
	            		m.remover(is);
	            		isd.update(is);
	            		md.update(m);
	            		isd.delete(is);
            		}
            	}
            	m.setItensServico(null);
            }
            
            if(m.getEndereco()!=null){
            	Endereco e = m.getEndereco();
            	m.setEndereco(null);
            	md.update(m);
            	ed.delete(e);
            }
            md.delete(m);
            resposta = "Dados Excluidos";
        } catch (Exception ex) {
        	resposta = ex.getMessage();
			ex.printStackTrace();
		}
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         RequestDispatcher rd = null;
         out.println(resposta);
         rd = request.getRequestDispatcher("./ControleMecanico?cmd=listar");
         rd.include(request, response);
	}
	

	protected void listar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> MEC�NICOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i>Mec�nico</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Mecanicos Cadastrados no Sistema");
        pw.println("</header>");
        pw.println("<table class=\"table table-striped table-advance table-hover\">");
        pw.println("<tbody>");
        pw.println("<tr>");
        pw.println("<th><i class=\"icon_profile\"></i> Nome Completo do Mec�nico</th>");
        pw.println("<th><i class=\"fa fa-envelope-o\"></i> Email</th>");
        pw.println("<th><i class=\"fa fa-phone\"></i> Telefone</th>");
        pw.println("<th><i class=\"fa fa-mobile\"></i> Celular</th>");
        pw.println("<th><i class=\"icon_id\"></i> CPF</th>");
        pw.println("<th><i class=\"icon_cogs\"></i> A��o</th>");
        pw.println("</tr>");
        
        try {
        	GenericDao<Mecanico> md = new GenericDao<Mecanico>();
        	List<Mecanico> lst = md.findAll(Mecanico.class);
        	List<Mecanico> lista = new ArrayList<Mecanico>();
        	
        	for(Mecanico m : lst){
				if(!lista.contains(m)){
					lista.add(m);
				}
			}
        	
	        for(Mecanico m : lista){
	        
		        pw.println("<tr>");
		        pw.println("<td>"+m.getNome()+"</td>");
		        pw.println("<td>"+m.getEmail()+"</td>");
		        pw.println("<td>"+m.getTelefone()+"</td>");
		        pw.println("<td>"+m.getCelular()+"</td>");
		        pw.println("<td>"+m.getCpf()+"</td>");
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
		        pw.println("<a class=\"btn btn-info\" href=\"./ControleMecanico?cmd=endereco&id=" + m.getEndereco().getIdEndereco() + "\"><i class=\"icon_info_alt\"></i></a>");
		        pw.println("<a class=\"btn btn-primary\" href=\"./ControleMecanico?cmd=editar&id=" + m.getIdMecanico() + "\"><i class=\"icon_pencil\"></i></a>");
		        pw.println("<a class=\"btn btn-danger\" href=\"./ControleMecanico?cmd=deletar&id=" + m.getIdMecanico() + "\"><i class=\"icon_close_alt2\"></i></a>");
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
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> MEC�NICOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleMecanico?cmd=listar\">Mec�nico</a></li>");
        pw.println("<li><i class=\"icon_pencil\"></i>Edi��o</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12 col-lg-10\">");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleMecanico?cmd=alterar&id=" + id + "';\" type=\"button\">Alterar Dados</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleMecanico?cmd=selecionar&id=" + id + "';\" type=\"button\">Atribuir Itens de Servico a outro Mecanico</button>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</section>");
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void alterar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        Integer id = Integer.parseInt(request.getParameter("id"));
		
        GenericDao<Mecanico> md = new GenericDao<Mecanico>();
        Mecanico m = md.findById(id, Mecanico.class);
        
		pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> MEC�NICOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleMecanico?cmd=listar\">Mec�nico</a></li>");
        pw.println("<li><i class=\"icon_pencil\"></i><a href=\"./ControleMecanico?cmd=editar&id="+id+"\">Edi��o</a></li>");
        pw.println("<li><i class=\"fa fa-pencil\"></i>Alterar</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Mecanico");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"./ControleMecanico?Atualizar\" accept-charset='UTF-8'>");
        pw.println("<h5>Dados Basicos</h5>");
        pw.println("<div class=\"form-group \">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"atualizar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Nome Completo <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"nome\" name=\"nome\" type=\"text\" value=\""+ m.getNome() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">CPF <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cpf\" type=\"text\" name=\"cpf\" value=\""+ m.getCpf() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Email <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"email\" type=\"text\" name=\"email\" value=\""+ m.getEmail() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Telefone <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"telefone\" type=\"text\" name=\"telefone\" value=\""+ m.getTelefone() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Celular <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"celular\" type=\"text\" name=\"celular\" value=\""+ m.getCelular() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<h5> Endereço </h5>");
        pw.println("<hr/>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Rua <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"rua\" type=\"text\" name=\"rua\" value=\""+ m.getEndereco().getRua() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">N�mero <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"numero\" type=\"text\" name=\"numero\" value=\""+ m.getEndereco().getNumero() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Tipo de Logradouro <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"logradouro\" type=\"text\" name=\"logradouro\" value=\""+ m.getEndereco().getLogradouro() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Bairro <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"bairro\" type=\"text\" name=\"bairro\" value=\""+ m.getEndereco().getBairro() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Cidade <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cidade\" type=\"text\" name=\"cidade\" value=\""+ m.getEndereco().getCidade() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">Estado <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"estado\" type=\"text\" name=\"estado\" value=\""+ m.getEndereco().getEstado() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<label for=\"\" class=\"control-label col-lg-2\">CEP <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"cep\" type=\"text\" name=\"cep\" value=\""+ m.getEndereco().getCep() +"\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Atualizar</button>");
        pw.println("<button class=\"btn btn-default\" onclick=\"location.href='./ControleMecanico?cmd=editar&id=" + id + "';\" type=\"button\">Voltar</button>");
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
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> MEC�NICOS</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleMecanico?cmd=listar\">Mec�nico</a></li>");
        pw.println("<li><i class=\"icon_pencil\"></i><a href=\"./ControleMecanico?cmd=editar&id="+id+"\">Edi��o</a></li>");
        pw.println("<li><i class=\"fa fa-pencil\"></i>Atribuir Ve�culos a outro Cliente</li>");
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
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleMecanico\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"mesclar\">");
        pw.println("<input type=\"hidden\" id=\"id\" name=\"id\" value=\""+ id +"\">");
        pw.println("<label class=\"control-label col-lg-2\" for=\"inputSuccess\">Nome Completo do Mecanico</label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<select class=\"form-control m-bot15\" name=\"mecanico\" id=\"mecanico\">");
        
        try {
        	
        	GenericDao<Mecanico> md = new GenericDao<Mecanico>();
        	Mecanico m = md.findById(id, Mecanico.class);

			List<Mecanico> l = md.findAll(Mecanico.class);
			List<Mecanico> lista = new ArrayList<Mecanico>();
			
			for(Mecanico mec : l){
				if(!lista.contains(mec)){
					lista.add(mec);
				}
			}
			
			for(Mecanico mec : lista){
				if(!mec.getIdMecanico().equals(id)){
					pw.println("<option value=\""+mec.getIdMecanico()+"\">"+mec.getNome()+"</option>");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
        
        pw.println("</select>");
        pw.println("</div>");
        pw.println("<div class=\"form-group\">");
        pw.println("<div class=\"col-lg-offset-2 col-lg-10\">");
        pw.println("<button class=\"btn btn-primary\" type=\"submit\">Selecionar Mecanico</button>");
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
        Integer mecanico = Integer.parseInt(request.getParameter("mecanico"));
        try
        {   
        	GenericDao<Mecanico> md = new GenericDao<Mecanico>();
        	GenericDao<ItemServico> isd = new GenericDao<ItemServico>();
        	Mecanico m1 = md.findById(id, Mecanico.class);
        	Mecanico m2 = md.findById(mecanico, Mecanico.class);
        	List<ItemServico> itens = new ArrayList<ItemServico>();
        	
        	if(m1.getItensServico()!= null){
        		for(ItemServico is : m1.getItensServico()){
            		itens.add(is);
            	}
	        	for(ItemServico is : itens){
	        		if(is!=null){
		        		m1.remover(is);
	        			m2.adicionar(is);
		        		is.setMecanico(m2);
		        		isd.update(is);
		        		md.update(m1);
		        		md.update(m2);
	        		}
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
            rd = request.getRequestDispatcher("./ControleMecanico?cmd=listar");
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
        	GenericDao<Mecanico> md = new GenericDao<Mecanico>();
        	Mecanico m = md.findById(Integer.parseInt(request.getParameter("id")), Mecanico.class);
        	
        	if(!checkLetters(request.getParameter("nome"))){
				throw new Exception("Um Nome n�o pode possuir n�meros");
			}
        	
        	if(!checkCPF(request.getParameter("cpf"))&&(!request.getParameter("cpf").equalsIgnoreCase(""))){
				throw new Exception("Digite um CPF V�lido!");
			}
        	
        	m.setNome(request.getParameter("nome"));
        	if(!request.getParameter("cpf").equalsIgnoreCase("")){
				m.setCpf(Long.parseLong(request.getParameter("cpf")));
			}
        	m.setEmail(request.getParameter("email"));
        	m.setTelefone(Long.parseLong(request.getParameter("telefone")));
        	m.setCelular(Long.parseLong(request.getParameter("celular")));
        	
        	Endereco e = new Endereco();
        	
        	e.setRua(request.getParameter("rua"));
        	e.setNumero(Integer.parseInt(request.getParameter("numero")));
        	e.setLogradouro(request.getParameter("logradouro"));
        	e.setBairro(request.getParameter("bairro"));
        	e.setCidade(request.getParameter("cidade"));
        	e.setEstado(request.getParameter("estado"));
        	e.setCep(Integer.parseInt(request.getParameter("cep")));

        	m.setEndereco(e);
        	
        	md.update(m);
        	
        	resposta = "Dados Alterados";
        	
        } catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
		} finally
        {
			response.setContentType("text/html");
            RequestDispatcher rd = null;
            out.println(resposta);
            rd = request.getRequestDispatcher("./ControleMecanico?cmd=listar");
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
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-files-o\"></i> MEC�NICO</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"usu/index.html\">Home</a></li>");
        pw.println("<li><i class=\"icon_document_alt\"></i>Cadastro</li>");
        pw.println("<li><i class=\"icon_profile\"></i>Mec�nico</li>");
        pw.println("</ol>");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<section class=\"panel\">");
        pw.println("<header class=\"panel-heading\">");
        pw.println("Mecânico");
        pw.println("</header>");
        pw.println("<div class=\"panel-body\">");
        pw.println("<div class=\"form\">");
        pw.println("<form class=\"form-validate form-horizontal\" id=\"feedback_form\" method=\"get\" action=\"ControleMecanico\">");
        pw.println("<input type=\"hidden\" id=\"cmd\" name=\"cmd\" value=\"gravar\">");
        pw.println("<h5>Dados Basicos</h5>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"nome\" class=\"control-label col-lg-2\">Nome Completo <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control\" id=\"nome\" name=\"nome\" minlength=\"5\" type=\"text\" required />");
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
        pw.println("<h5> Endereço </h5>");
        pw.println("<hr/>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"rua\" class=\"control-label col-lg-2\">Rua <span class=\"required\">*</span></label>");
        pw.println("<div class=\"col-lg-10\">");
        pw.println("<input class=\"form-control \" id=\"rua\" type=\"text\" name=\"rua\" required />");
        pw.println("</div>");
        pw.println("</div>");
        pw.println("<div class=\"form-group \">");
        pw.println("<label for=\"numero\" class=\"control-label col-lg-2\">Numero <span class=\"required\">*</span></label>");
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
        
        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
	
	protected void endereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw = response.getWriter();
		Integer id = Integer.parseInt(request.getParameter("id"));
        request.getRequestDispatcher("/usu/base1.html").include(request, response);

        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i> ENDERE�O</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"./index.html\">Home</a></li>");
        pw.println("<li><i class=\"fa fa-table\"></i>Registros</li>");
        pw.println("<li><i class=\"fa fa-th-list\"></i><a href=\"./ControleMecanico?cmd=listar\">Mec�nico</a></li>");
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
        pw.println("<th><i class=\"fa fa-money\" aria-hidden=\"true\"></i> N�mero</th>");
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

        request.getRequestDispatcher("/usu/base2.html").include(request, response);
	}
}
