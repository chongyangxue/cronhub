package org.cronhub.managesystem.modules.task.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.struts2.json.annotations.JSON;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.cronhub.managesystem.commons.dao.bean.Process;
import org.cronhub.managesystem.commons.dao.bean.Task;
import org.cronhub.managesystem.modules.task.dao.ITaskDao;
import org.cronhub.managesystem.modules.task.dao.ProcessDao;
import org.springframework.transaction.annotation.Transactional;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ProcessAddAction extends ActionSupport {
	private ProcessDao processDao;
	private Process process;
	private ITaskDao taskDao;
	private Task task;
	private String pre_task_id;
	private List<Long> task_id_List;
	
	public String addProcess(){
		process.setUpdate_time(new Date());
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		process.setUser_id(user_id);
		this.processDao.insert(process);
		return SUCCESS;
	}
	
	@Transactional
	public String addProcessTask(){
		task.setUpdate_time(new Date());
		task.setIs_process_node(true);
		String user_id = (String)ActionContext.getContext().getSession().get("user_id");
		task.setUser_id(user_id);
		Long taskId = this.taskDao.insert(task);
		this.processDao.saveProcessTask(taskId, process.getId(), pre_task_id);
		return SUCCESS;
	}

	public String getPre_task_id() {
		return pre_task_id;
	}

	public String getPreTaskId(){
		List<Task> preTasks = processDao.findTasks(process.getId());
		List<Long> taskIdList = new ArrayList<Long>();
		for(Task task : preTasks) {
			taskIdList.add(task.getId());
		}
		this.setTask_id_List(taskIdList);
		return SUCCESS;
	}

	@JSON(serialize = false)
	public Process getProcess() {
		return process;
	}
	
	public Task getTask() {
		return this.task;
	}

	public List<Long> getTask_id_List() {
		return task_id_List;
	}
	
	public void setPre_task_id(String pre_task_id) {
		this.pre_task_id = pre_task_id;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public void setProcessDao(ProcessDao dao) {
		this.processDao = dao;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public void setTask_id_List(List<Long> task_id_List) {
		this.task_id_List = task_id_List;
	}

	public void setTaskDao(ITaskDao taskDao) {
		this.taskDao = taskDao;
	}

}
