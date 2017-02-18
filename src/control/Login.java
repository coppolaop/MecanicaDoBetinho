package control;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import persistence.GenericDao;
import entity.Usuario;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String cmd = request.getParameter("cmd");
		if(request.getParameter("cmd").equalsIgnoreCase("pass")){
			changePassword(request,response);
		}else{
			logar(request,response);
		}
	}
	protected void logar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta = "";
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		
		Usuario u = new Usuario();
		u.setUsername(login);
		u.setSenha(senha);
		
		GenericDao<Usuario> ud = new GenericDao<Usuario>();
		HttpSession session = (HttpSession) request.getSession();
		
		Usuario usuario = ud.get(u);
		
		if(usuario!=null){
			session.setAttribute("logado", usuario.getPerfil());
			session.setAttribute("user", usuario.getIdUsuario());
			if(usuario.getPerfil().equalsIgnoreCase("usu")){
        		if(senha.equalsIgnoreCase("senhapadrao")){
        			response.sendRedirect(request.getContextPath()+"/password.html");
        		}else{
        			response.sendRedirect(request.getContextPath()+"/usu/index.html");
        		}
        	}else if(usuario.getPerfil().equalsIgnoreCase("adm")){
        		if(senha.equalsIgnoreCase("senhapadrao")){
        			response.sendRedirect(request.getContextPath()+"/password.html");
        		}else{
        			response.sendRedirect(request.getContextPath()+"/adm/index.html");
        		}
		    }
		}else{
			u = new Usuario();
			usuario = new Usuario();
			session.invalidate();
			resposta = "Combinação de Usuario e Senha Inválidos";
			response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	        out.println(resposta);
			RequestDispatcher rd = request.getRequestDispatcher("login.html");
            rd.include(request, response);
		}
	}
	
	protected void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resposta;
		HttpSession session = (HttpSession) request.getSession();
		Integer id = (Integer) session.getAttribute("user");
		GenericDao<Usuario> ud = new GenericDao<Usuario>();
		Usuario u = ud.findById(id, Usuario.class);
		
		try {
			String senha1 = request.getParameter("password");
			String senha2 = request.getParameter("confirm_password");
			
			if(senha1.equals(senha2)){
				u.setSenha(senha1);
				Criptografia.criptografia(u);
			}else{
				throw new Exception("As senhas n�o batem");
			}
			
			ud.update(u);
			if(u.getPerfil().equalsIgnoreCase("adm")){
				response.sendRedirect(request.getContextPath()+"/adm/index.html");
			}else{
				response.sendRedirect(request.getContextPath()+"/usu/index.html");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			resposta = ex.getMessage();
			RequestDispatcher rd = request.getRequestDispatcher("password.html");
            rd.include(request, response);
		}
	}
}