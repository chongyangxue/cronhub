package org.cronhub.managesystem.commons.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PageFilter extends HttpServlet implements Filter{
	@Override
	public void doFilter(ServletRequest servletRequeset, ServletResponse servletResponse, FilterChain filterChain) 
			throws IOException, ServletException {  
	    HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
	    HttpServletRequest request = (HttpServletRequest)servletRequeset; 
        HttpSession session = request.getSession();
        if(session.getAttribute("user_id") == null) {
    	    httpServletResponse.sendRedirect("/index");
    	    return;
        }
        //已通过验证，用户访问继续  
        filterChain.doFilter(servletRequeset, servletResponse);  
        return;  
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}  
}
