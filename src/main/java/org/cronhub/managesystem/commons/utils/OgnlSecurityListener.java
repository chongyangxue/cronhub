package org.cronhub.managesystem.commons.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ognl.OgnlRuntime;

public class OgnlSecurityListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		OgnlRuntime.setSecurityManager(null);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}
	
}
