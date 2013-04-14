package org.cronhub.managesystem.modules.task.action;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.cronhub.managesystem.commons.action.IFindByPage;
import org.cronhub.managesystem.commons.action.PageActionUtils;
import org.cronhub.managesystem.commons.dao.bean.Process;
import org.cronhub.managesystem.commons.dao.bean.Task;
import org.cronhub.managesystem.commons.dao.config.AssociateDeleteConfig;
import org.cronhub.managesystem.commons.dao.config.FillConfig;
import org.cronhub.managesystem.commons.utils.FilterSqlGenerater;
import org.cronhub.managesystem.commons.utils.PageIOUtils;
import org.cronhub.managesystem.commons.utils.PageSqlGenerater;
import org.cronhub.managesystem.modules.task.dao.ProcessDao;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ProcessViewAction extends ActionSupport {
	private PageSqlGenerater pageGen;
	private ProcessDao dao;
	private Process process;
	private List<Task> processTask;
	private static final String join_table = " process ";
	private static final String order_update_time_sql= " ORDER BY update_time DESC";

	public void setPageGen(PageSqlGenerater pageGen) {
		this.pageGen = pageGen;
	}
	
	public void setDao(ProcessDao dao) {
		this.dao = dao;
	}

	public String findFirstPage(){
		//final String whereSql = FilterSqlGenerater.genWhereSql();
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = "";
		}else {
			whereSql = "WHERE user_id = '" + user_id + "'";
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Process> ifinder = new IFindByPage<Process>(){
			@Override
			public List<Process> findByPage(int currentPage, int maxPerPage) {
				List<Process> findList =  dao.findByPage(whereSql + 
						order_update_time_sql + pageGen.generateFirst(maxPerPage), fillConfig);
				return findList;
			}
		};
		PageActionUtils.findFirstPage(ifinder, this.pageGen.getTotalCountFromTable(join_table, whereSql));
		return SUCCESS;
	}
	
	public String findNextPageNo(){
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = "";
		}else {
			whereSql = "WHERE user_id = '" + user_id + "'";
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Process> ifinder = new IFindByPage<Process>(){
			@Override
			public List<Process> findByPage(int currentPage, int maxPerPage) {
				List<Process> findList =  dao.findByPage(whereSql + 
						order_update_time_sql + pageGen.generateNext(join_table, 
								currentPage, maxPerPage,whereSql),fillConfig);
				return findList;
			}
		};
		PageActionUtils.findNextPageNo(ifinder, this.pageGen.getTotalCountFromTable(join_table,whereSql));
		return SUCCESS;
	}
	
	public String findPrevPageNo(){
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = "";
		}else {
			whereSql = "WHERE user_id = '" + user_id + "'";
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Process> ifinder = new IFindByPage<Process>(){
			@Override
			public List<Process> findByPage(int currentPage, int maxPerPage) {
				List<Process> findList =  dao.findByPage(whereSql + 
						order_update_time_sql + pageGen.generatePrev(currentPage, maxPerPage),fillConfig);
				return findList;
			}
		};
		PageActionUtils.findPrevPageNo(ifinder, this.pageGen.getTotalCountFromTable(join_table,whereSql));
		return SUCCESS;
	}
	
	public String findLastPage(){
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = "";
		}else {
			whereSql = "WHERE user_id = '" + user_id + "'";
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Process> ifinder = new IFindByPage<Process>(){
			@Override
			public List<Process> findByPage(int currentPage, int maxPerPage) {
				List<Process> findList =  dao.findByPage(whereSql +	order_update_time_sql 
						+ pageGen.generateLast(join_table, maxPerPage,whereSql),fillConfig);
				return findList;
			}
		};
		PageActionUtils.findLastPage(ifinder, this.pageGen.getTotalCountFromTable(join_table,whereSql));
		return SUCCESS;
	}
	
	public String deleteOneById(){
		Long id = Long.valueOf(ServletActionContext.getRequest().getParameter("id"));
		this.dao.deleteById(id);
		PageIOUtils.printToPage("success");
		return NONE;
	}
	
	public String goProcessTaskView() {
		List<Task> tasks = this.dao.findTasks(process.getId());
		this.setProcessTask(tasks);
		return SUCCESS;
	}

	public List<Task> getProcessTask() {
		return processTask;
	}

	public void setProcessTask(List<Task> processTask) {
		this.processTask = processTask;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}
}
