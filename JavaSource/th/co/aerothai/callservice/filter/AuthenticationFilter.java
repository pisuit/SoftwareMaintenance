package th.co.aerothai.callservice.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.co.aerothai.callservice.customtype.UserRole;
import th.co.aerothai.callservice.model.Authorization;
import th.co.aerothai.callservice.model.SystemUser;
import th.co.aerothai.callservice.security.UserSession;

import com.novell.ldap.LDAPConnection;

public class AuthenticationFilter implements Filter {
	
	 private FilterConfig config;

	  public void doFilter(ServletRequest req, ServletResponse resp,
	      FilterChain chain) throws IOException, ServletException {
	    if (((HttpServletRequest) req).getSession().getAttribute(
	        "userSession") == null) {
	      ((HttpServletResponse) resp).sendRedirect("./login.jsf?requested_page="+(((HttpServletRequest) req).getRequestURI()).replace("/it_service/", ""));
	    } else {
	    	SystemUser user = (SystemUser) ((HttpServletRequest) req).getSession().getAttribute( "userSession");
	    	List<UserRole> roleList = new ArrayList<UserRole>();
	    	for(Authorization auth : user.getRoles()){
	    		roleList.add(auth.getUserRole());
	    	}
	    	
//	    	if(((HttpServletRequest) req).getRequestURI().contains("request")){
//	    		if(roleList.contains(UserRole.REQUESTER)){
//	    			chain.doFilter(req, resp);
//	    		} else {
//	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
//	    		}
//	    	} 
	    	if(((HttpServletRequest) req).getRequestURI().contains("userdirector")){
	    		if(roleList.contains(UserRole.USER_DIRECTOR)){
	    			chain.doFilter(req, resp);
	    		} else {
	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
	    		}
	    	}  
	    	if(((HttpServletRequest) req).getRequestURI().contains("callservice")){
	    		if(roleList.contains(UserRole.CALL_SERVICE)){
	    			chain.doFilter(req, resp);
	    		} else {
	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
	    		}
	    	}
	    	if(((HttpServletRequest) req).getRequestURI().contains("inspector")){
	    		if(roleList.contains(UserRole.INSPECTOR)){
	    			chain.doFilter(req, resp);
	    		} else {
	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
	    		}
	    	}
	    	if(((HttpServletRequest) req).getRequestURI().contains("providerdirector")){
	    		if(roleList.contains(UserRole.PROVIDER_DIRECTOR)){
	    			chain.doFilter(req, resp);
	    		} else {
	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
	    		}
	    	}
	    	if(((HttpServletRequest) req).getRequestURI().contains("admin")){
	    		if(roleList.contains(UserRole.ADMIN)){
	    			chain.doFilter(req, resp);
	    		} else {
	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
	    		}
	    	}
	    	if(((HttpServletRequest) req).getRequestURI().contains("operator")){
	    		if(roleList.contains(UserRole.OPERATOR)){
	    			chain.doFilter(req, resp);
	    		} else {
	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
	    		}
	    	}
	    	if(((HttpServletRequest) req).getRequestURI().contains("projectmanager")){
	    		if(roleList.contains(UserRole.PROJECT_MANAGER)){
	    			chain.doFilter(req, resp);
	    		} else {
	    			((HttpServletResponse) resp).sendRedirect("./forbid.jsf");
	    		}
	    	}
	    	if(((HttpServletRequest) req).getRequestURI().contains("request")){
	    		chain.doFilter(req, resp);
	    	}
	    }
	  }

	  public void init(FilterConfig config) throws ServletException {
	    this.config = config;
	  }

	  public void destroy() {
	    config = null;
	  }

//	private FilterConfig filterConfig = null;
//	private ServletContext servletContext = null;
//	
//	public void init(FilterConfig aFilterConfig) throws ServletException {
//		filterConfig = aFilterConfig;
//		servletContext = filterConfig.getServletContext();
//	}
//	
//	public void destroy() {
//		filterConfig = null;
//	}
//
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		
//		HttpServletRequest httpRequest = (HttpServletRequest)request;
//		HttpServletResponse httpResponse = (HttpServletResponse)response;
//		HttpSession session = httpRequest.getSession();
//		
//		String requestPath = httpRequest.getServletPath();
//		String contextPath = httpRequest.getContextPath();
//		UserSession user = (UserSession) session.getAttribute("UserSession");
//		//System.out.println("User : "+user);
//		//System.out.println("Context :"+contextPath);
//		System.out.println("Request :"+requestPath);
//		if (user == null && !requestPath.equals("/pages/login.jsf")) {
//			// redirect to welcome page
//			//System.out.println("Redirect");
//			session.setAttribute("origin", requestPath);
//			httpResponse.sendRedirect(contextPath+"/pages/login.jsf");
//		} else {
//			//System.out.println("Render");
//			session.removeAttribute("origin");
//			chain.doFilter(request, response);
//		}
//		
//	}


}
