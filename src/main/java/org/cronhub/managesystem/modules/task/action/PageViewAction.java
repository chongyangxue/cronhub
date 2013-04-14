package org.cronhub.managesystem.modules.task.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.cronhub.managesystem.commons.action.IFindByPage;
import org.cronhub.managesystem.commons.action.PageActionUtils;
import org.cronhub.managesystem.commons.dao.bean.Task;
import org.cronhub.managesystem.commons.dao.config.AssociateDeleteConfig;
import org.cronhub.managesystem.commons.dao.config.FillConfig;
import org.cronhub.managesystem.commons.utils.FilterSqlGenerater;
import org.cronhub.managesystem.commons.utils.PageIOUtils;
import org.cronhub.managesystem.commons.utils.PageSqlGenerater;
import org.cronhub.managesystem.modules.task.dao.ITaskDao;
import org.cronhub.managesystem.modules.task.dao.ProcessDao;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PageViewAction extends ActionSupport {
	private PageSqlGenerater pageGen;
	private ITaskDao dao;
	private ProcessDao processDao;
	private static final String join_table = " task LEFT JOIN daemon on task.daemon_id = daemon.id ";
	private static final String order_update_time_sql= " ORDER BY update_time DESC";
	
	public String findFirstPage(){
		String genWhereSql = FilterSqlGenerater.genWhereSql();
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = genWhereSql;
		}else {
			if(genWhereSql.equals("")) {
				whereSql = "WHERE task.user_id = '" + user_id + "'";
			}else {
				whereSql = genWhereSql + " AND task.user_id = '" + user_id + "'";
			}
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Task> ifinder = new IFindByPage<Task>(){
			@Override
			public List<Task> findByPage(int currentPage, int maxPerPage) {
				List<Task> findList =  dao.findByPage(whereSql + order_update_time_sql 
						+ pageGen.generateFirst(maxPerPage), fillConfig);
				//Collections.sort(findList, machineIpSort);
				return findList;
			}
		};
		PageActionUtils.findFirstPage(ifinder, this.pageGen.getTotalCountFromTable(join_table, whereSql));
		return SUCCESS;
	}
	
	public String findNextPageNo(){
		String genWhereSql = FilterSqlGenerater.genWhereSql();
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = genWhereSql;
		}else {
			if(genWhereSql.equals("")) {
				whereSql = "WHERE task.user_id = '" + user_id + "'";
			}else {
				whereSql = genWhereSql + " AND task.user_id = '" + user_id + "'";
			}
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Task> ifinder = new IFindByPage<Task>(){
			@Override
			public List<Task> findByPage(int currentPage, int maxPerPage) {
				List<Task> findList =  dao.findByPage(whereSql + order_update_time_sql 
						+ pageGen.generateNext(join_table, currentPage, maxPerPage,whereSql),fillConfig);
				//Collections.sort(findList, machineIpSort);
				return findList;
			}
		};
		PageActionUtils.findNextPageNo(ifinder, this.pageGen.getTotalCountFromTable(join_table,whereSql));
		return SUCCESS;
	}
	
	public String findPrevPageNo(){
		String genWhereSql = FilterSqlGenerater.genWhereSql();
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = genWhereSql;
		}else {
			if(genWhereSql.equals("")) {
				whereSql = "WHERE task.user_id = '" + user_id + "'";
			}else {
				whereSql = genWhereSql + " AND task.user_id = '" + user_id + "'";
			}
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Task> ifinder = new IFindByPage<Task>(){
			@Override
			public List<Task> findByPage(int currentPage, int maxPerPage) {
				List<Task> findList =  dao.findByPage(whereSql+order_update_time_sql 
						+ pageGen.generatePrev(currentPage, maxPerPage),fillConfig);
				//Collections.sort(findList, machineIpSort);
				return findList;
			}
		};
		PageActionUtils.findPrevPageNo(ifinder, this.pageGen.getTotalCountFromTable(join_table,whereSql));
		return SUCCESS;
	}

	public String findLastPage(){
		String genWhereSql = FilterSqlGenerater.genWhereSql();
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		String user_type = (String)ActionContext.getContext().getSession().get("userType");
		final String whereSql;
		if(user_type.equals("admin")) {
			whereSql = genWhereSql;
		}else {
			if(genWhereSql.equals("")) {
				whereSql = "WHERE task.user_id = '" + user_id + "'";
			}else {
				whereSql = genWhereSql + " AND task.user_id = '" + user_id + "'";
			}
		}
		final FillConfig fillConfig = FillConfig.getFillDaemonInstance();
		IFindByPage<Task> ifinder = new IFindByPage<Task>(){
			@Override
			public List<Task> findByPage(int currentPage, int maxPerPage) {
				List<Task> findList =  dao.findByPage(whereSql + 
						order_update_time_sql+pageGen.generateLast(join_table, maxPerPage,whereSql),fillConfig);
				return findList;
			}
		};
		PageActionUtils.findLastPage(ifinder, this.pageGen.getTotalCountFromTable(join_table,whereSql));
		return SUCCESS;
	}

	public String deleteOneById(){
		Long id = Long.valueOf(ServletActionContext.getRequest().getParameter("id"));
		AssociateDeleteConfig config = new AssociateDeleteConfig(true,true,true);
		Task task = this.dao.findById(id, FillConfig.getFillTaskInstance());
		boolean canBeDeleted = true;
		if(task.getIs_process_node()) {
			canBeDeleted = this.processDao.deleteProcessTaskById(id);
		}
		if(canBeDeleted){
			this.dao.deleteById(id, config);
		 	PageIOUtils.printToPage("success");
		} else {
			PageIOUtils.printToPage("error");
		}
		return NONE;
	}
	
	public ProcessDao getProcessDao() {
		return processDao;
	}
	
	public void setProcessDao(ProcessDao processDao) {
		this.processDao = processDao;
	}
	
	public void setPageGen(PageSqlGenerater pageGen) {
		this.pageGen = pageGen;
	}
	
	public void setDao(ITaskDao dao) {
		this.dao = dao;
	}
}
