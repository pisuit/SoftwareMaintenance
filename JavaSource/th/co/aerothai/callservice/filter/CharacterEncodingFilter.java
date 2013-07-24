package th.co.aerothai.callservice.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CharacterEncodingFilter implements Filter {
	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
	      req.setCharacterEncoding("UTF-8");
	      resp.setCharacterEncoding("UTF-8");
	      
	      HttpServletResponse res = (HttpServletResponse) resp;
	      res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
	      res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
	      res.setDateHeader("Expires", 0); // Proxies.
	      chain.doFilter(req, resp);
	   }

	   public void init(FilterConfig filterConfig) throws ServletException {
	      
	   }
	   
	   public void destroy() {
	      
	   }
}
