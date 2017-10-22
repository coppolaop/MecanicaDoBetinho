package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import persistence.GenericDao;
import entity.Usuario;
import entity.Peca;
import entity.Servico;
import entity.Veiculo;

@WebServlet("/GenericLista")
public class GenericLista<E> extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	
	//Arquivo Conceitual
		//Sua Utilização não é feita no projeto devido a necessidade de personalização dos campos
		//Necessita do metodo ToString configurado para gerar lista HTML
	
	
    public GenericLista() {
        super();
    }
    
    public Object objeto(String classe){
    	Object o = new Object();
    	if(classe.equalsIgnoreCase("Cliente")){
    		o = new Usuario();
    	}else if(classe.equalsIgnoreCase("Veiculo")){
    		o = new Veiculo();
    	}else if(classe.equalsIgnoreCase("Servico")){
    		o = new Servico();
    	}else if(classe.equalsIgnoreCase("Peca")){
    		o = new Peca();
    	}
    	return o;
    }
    
    public GenericDao<E> dao(String classe){
    	GenericDao<E> dao = new GenericDao<E>();
    	if(classe.equalsIgnoreCase("Cliente")){
    		dao = (GenericDao<E>) new GenericDao<Usuario>();
    	}else if(classe.equalsIgnoreCase("Veiculo")){
    		dao = (GenericDao<E>) new GenericDao<Veiculo>();
    	}else if(classe.equalsIgnoreCase("Servico")){
    		dao = (GenericDao<E>) new GenericDao<Servico>();
    	}else if(classe.equalsIgnoreCase("Peca")){
    		dao = (GenericDao<E>) new GenericDao<Peca>();
    	}
    	return dao;
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Object o = objeto(request.getParameter("obj"));
		Class obj = o.getClass();
		
		PrintWriter pw = response.getWriter();
        request.getRequestDispatcher("/usu/base1.html").include(request, response);
        
        pw.println("<section class=\"wrapper\">");
        pw.println("<div class=\"row\">");
        pw.println("<div class=\"col-lg-12\">");
        pw.println("<h3 class=\"page-header\"><i class=\"fa fa-table\"></i>"+ obj.getSimpleName() +"</h3>");
        pw.println("<ol class=\"breadcrumb\">");
        pw.println("<li><i class=\"fa fa-home\"></i><a href=\"index.html\">Home</a></li>");
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
        String annotation = "";
        for (Field atributo : obj.getDeclaredFields()) {
        	if(!(atributo.getName().startsWith("id"))){
        		if(!(atributo.getName().equals("serialVersionUID"))){
        			annotation = atributo.getAnnotations().toString();
        			if(atributo.getAnnotations().toString().equals(annotation)){
        				pw.println("<th><i class=\"\"></i>"+ atributo.getName() +"</th>");
        			}
        		}
        	}
  		}
        
        pw.println("</tr>");

        try {
        	List<E> lst = dao(o.toString()).findAll(obj);
        	List<E> lista = new ArrayList<E>();
        	
        	for(E x : lst){
				if(!lista.contains(x)){
					lista.add(x);
				}
			}
        	
	        for(E x : lista){
	        	
		        pw.println("<tr>");
		        
		        pw.println(x.toString());
		        
		        pw.println("<td>");
		        pw.println("<div class=\"btn-group\">");
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
