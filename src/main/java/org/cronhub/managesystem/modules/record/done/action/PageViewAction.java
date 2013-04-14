package org.cronhub.managesystem.modules.record.done.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.cronhub.managesystem.commons.action.IFindByPage;
import org.cronhub.managesystem.commons.action.PageActionUtils;
import org.cronhub.managesystem.commons.dao.bean.TaskRecordDone;
import org.cronhub.managesystem.commons.dao.config.FillConfig;
import org.cronhub.managesystem.commons.utils.FilterSqlGenerater;
import org.cronhub.managesystem.commons.utils.PageIOUtils;
import org.cronhub.managesystem.commons.utils.PageSqlGenerater;
import org.cronhub.managesystem.modules.record.done.dao.IDoneRecordDao;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PageViewAction extends ActionSupport {
	private PageSqlGenerater pageGen;

	public void setPageGen(PageSqlGenerater pageGen) {
		this.pageGen = pageGen;
	}

	private IDoneRecordDao doneRecordDao;

	public void setDoneRecordDao(IDoneRecordDao doneRecordDao) {
		this.doneRecordDao = doneRecordDao;
	}

	private static final String defaultOrderBy = "ORDER BY end_datetime DESC";

	public String findFirstPage() {
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
		final FillConfig fillConfig = FillConfig.getFillAllInstance();
		final StringBuilder orderSql = new StringBuilder(defaultOrderBy);
		if (ServletActionContext.getRequest().getParameterMap().containsKey(
				"sort_column")
				&& ServletActionContext.getRequest().getParameterMap()
						.containsKey("sort_order")) {
			orderSql.delete(0, orderSql.length());
			orderSql.append(" ORDER BY "
					+ ServletActionContext.getRequest().getParameter(
							"sort_column")+ " " + ServletActionContext.getRequest().getParameter("sort_order"));
		}
		final String tableName = ServletActionContext.getRequest()
				.getParameter("tableName");
		final String join_table = "("
				+ tableName
				+ " LEFT JOIN task ON "
				+ tableName
				+ ".task_id = task.id) LEFT JOIN daemon ON task.daemon_id = daemon.id";
		IFindByPage<TaskRecordDone> ifinder = new IFindByPage<TaskRecordDone>() {
			@Override
			public List<TaskRecordDone> findByPage(int currentPage,
					int maxPerPage) {
				List<TaskRecordDone> findList = doneRecordDao
						.findByPage(tableName, whereSql + orderSql.toString()
								+ pageGen.generateFirst(maxPerPage), fillConfig);
				return findList;
			}
		};
		PageActionUtils.findFirstPage(ifinder, this.pageGen
				.getTotalCountFromTable(join_table, whereSql));
		ServletActionContext.getRequest().setAttribute("tableName", tableName);
		return SUCCESS;
	}

	public String findNextPageNo() {
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
		final FillConfig fillConfig = FillConfig.getFillAllInstance();
		final StringBuilder orderSql = new StringBuilder(defaultOrderBy);
		if (ServletActionContext.getRequest().getParameterMap().containsKey(
				"sort_column")
				&& ServletActionContext.getRequest().getParameterMap()
						.containsKey("sort_order")) {
			orderSql.delete(0, orderSql.length());
			orderSql.append(" ORDER BY "
					+ ServletActionContext.getRequest().getParameter(
							"sort_column")
					+ " "
					+ ServletActionContext.getRequest().getParameter(
							"sort_order"));
		}
		final String tableName = ServletActionContext.getRequest()
				.getParameter("tableName");
		final String join_table = "("
				+ tableName
				+ " LEFT JOIN task ON "
				+ tableName
				+ ".task_id = task.id) LEFT JOIN daemon ON task.daemon_id = daemon.id";
		IFindByPage<TaskRecordDone> ifinder = new IFindByPage<TaskRecordDone>() {
			@Override
			public List<TaskRecordDone> findByPage(int currentPage,
					int maxPerPage) {
				List<TaskRecordDone> findList = doneRecordDao.findByPage(
						tableName, whereSql
								+ orderSql.toString()
								+ pageGen.generateNext(join_table, currentPage,
										maxPerPage, whereSql), fillConfig);
				return findList;
			}
		};
		PageActionUtils.findNextPageNo(ifinder, this.pageGen
				.getTotalCountFromTable(join_table, whereSql));
		ServletActionContext.getRequest().setAttribute("tableName", tableName);
		return SUCCESS;
	}

	public String findPrevPageNo() {
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
		final FillConfig fillConfig = FillConfig.getFillAllInstance();
		final StringBuilder orderSql = new StringBuilder(defaultOrderBy);
		if (ServletActionContext.getRequest().getParameterMap().containsKey("sort_column")
				&& ServletActionContext.getRequest().getParameterMap()
						.containsKey("sort_order")) {
			orderSql.delete(0, orderSql.length());
			orderSql.append(" ORDER BY "
					+ ServletActionContext.getRequest().getParameter(
							"sort_column")
					+ " "
					+ ServletActionContext.getRequest().getParameter(
							"sort_order"));
		}
		final String tableName = ServletActionContext.getRequest()
				.getParameter("tableName");
		final String join_table = "("
				+ tableName
				+ " LEFT JOIN task ON "
				+ tableName
				+ ".task_id = task.id) LEFT JOIN daemon ON task.daemon_id = daemon.id";
		IFindByPage<TaskRecordDone> ifinder = new IFindByPage<TaskRecordDone>() {
			@Override
			public List<TaskRecordDone> findByPage(int currentPage,
					int maxPerPage) {
				List<TaskRecordDone> findList = doneRecordDao
						.findByPage(tableName,
								whereSql
										+ orderSql.toString()
										+ pageGen.generatePrev(currentPage,
												maxPerPage), fillConfig);
				return findList;
			}
		};
		PageActionUtils.findPrevPageNo(ifinder, this.pageGen
				.getTotalCountFromTable(join_table, whereSql));
		ServletActionContext.getRequest().setAttribute("tableName", tableName);
		return SUCCESS;
	}

	public String findLastPage() {
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
		final FillConfig fillConfig = FillConfig.getFillAllInstance();
		final StringBuilder orderSql = new StringBuilder(defaultOrderBy);
		if (ServletActionContext.getRequest().getParameterMap().containsKey(
				"sort_column")
				&& ServletActionContext.getRequest().getParameterMap()
						.containsKey("sort_order")) {
			orderSql.delete(0, orderSql.length());
			orderSql.append(" ORDER BY "
					+ ServletActionContext.getRequest().getParameter(
							"sort_column")
					+ " "
					+ ServletActionContext.getRequest().getParameter(
							"sort_order"));
		}
		final String tableName = ServletActionContext.getRequest()
				.getParameter("tableName");
		final String join_table = "("
				+ tableName
				+ " LEFT JOIN task ON "
				+ tableName
				+ ".task_id = task.id) LEFT JOIN daemon ON task.daemon_id = daemon.id";
		IFindByPage<TaskRecordDone> ifinder = new IFindByPage<TaskRecordDone>() {
			@Override
			public List<TaskRecordDone> findByPage(int currentPage,
					int maxPerPage) {
				List<TaskRecordDone> findList = doneRecordDao.findByPage(
						tableName, whereSql
								+ orderSql.toString()
								+ pageGen.generateLast(join_table, maxPerPage,
										whereSql), fillConfig);
				return findList;
			}
		};
		PageActionUtils.findLastPage(ifinder, this.pageGen
				.getTotalCountFromTable(join_table, whereSql));
		ServletActionContext.getRequest().setAttribute("tableName", tableName);
		return SUCCESS;
	}

	public String getExecReturnString() {
		HttpServletRequest req = ServletActionContext.getRequest();
		Long id = Long.valueOf(req.getParameter("id"));
		String tableName = req.getParameter("tableName");
		FillConfig fillConfig = new FillConfig(false, false);
		TaskRecordDone record = this.doneRecordDao.findById(id, tableName,
				fillConfig);
		JSONObject ajaxJson = new JSONObject();
		ajaxJson.put("id", id);
		ajaxJson.put("return_str", record.getExec_return_str());
		PageIOUtils.printToPage(ajaxJson.toString());
		return NONE;
	}
}
