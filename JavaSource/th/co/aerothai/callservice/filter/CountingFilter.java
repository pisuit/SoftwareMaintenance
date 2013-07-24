package th.co.aerothai.callservice.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.co.aerothai.callservice.servlet.CountingServletResponse;

public class CountingFilter implements Filter {

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // NOOP.
    }

    @Override
    public void doFilter(ServletRequest request, final ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpres = (HttpServletResponse) response;
        CountingServletResponse counter = new CountingServletResponse(httpres);
        HttpServletRequest httpreq = (HttpServletRequest) request;
        httpreq.setAttribute("counter", counter);
        chain.doFilter(request, counter);
        counter.flushBuffer(); // Push the last bits containing HTML comment.
    }

    @Override
    public void destroy() {
        // NOOP.
    }

}
