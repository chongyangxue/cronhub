package org.cronhub.managesystem.commons.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.springframework.web.util.UrlPathHelper;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class AuthInterceptor extends AbstractInterceptor {
	private UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		//Map session = invocation.getInvocationContext().getSession();
		ActionContext context = invocation.getInvocationContext();
		HttpServletRequest request = (HttpServletRequest)context.get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse)context.get(StrutsStatics.HTTP_RESPONSE);
		String requestedPath = urlPathHelper.getLookupPathForRequest(request);
		if(requestedPath.equals("/") || requestedPath.equals("/index")) {
			response.sendRedirect(request.getContextPath() + "index.jsp");
		}
		
		return invocation.invoke();
	}

}
