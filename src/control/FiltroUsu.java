package control;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns={"/usu/*"})
public class FiltroUsu implements Filter {

    public FiltroUsu() {
        
    }

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String log = (String) session.getAttribute("logado");
		
		if(log!=null){
			if(log.equalsIgnoreCase("usu")){
				chain.doFilter(request, response);
			}else{
				resp.sendRedirect("../login.html");
			}
		}else{
			resp.sendRedirect("../login.html");
		}
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
	}

}
