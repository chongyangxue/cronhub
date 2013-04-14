package org.cronhub.managesystem.modules.task.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.cronhub.managesystem.commons.dao.bean.Task;
import org.cronhub.managesystem.commons.params.Params;
import org.cronhub.managesystem.commons.thrift.process.RemoteExecutCmdProcessor;
import org.cronhub.managesystem.commons.utils.PageIOUtils;
import org.cronhub.managesystem.modules.task.dao.ProcessDao;

import com.opensymphony.xwork2.ActionSupport;

public class ImmediateRemoteExecuteAction extends ActionSupport {
	private static final long serialVersionUID = 1780701872745870427L;
	private RemoteExecutCmdProcessor processor;
	private ProcessDao processDao;
	
	public void setProcessor(RemoteExecutCmdProcessor processor) {
		this.processor = processor;
	}

	public String remoteExecute(){
		HttpServletRequest req = ServletActionContext.getRequest();
		final Long id = Long.valueOf(req.getParameter("id"));
		boolean success = false;
		JSONObject ajaxJson = new JSONObject(); 
		try {
			success = this.processor.remoteExecuteOnSpot(id);
		} catch (Exception e) {
			ajaxJson.put("error",e.getMessage());
		}
		ajaxJson.put("status", success==true?"success":"fail");
		ajaxJson.put("id",id);
		PageIOUtils.printToPage(ajaxJson.toString());
		return NONE;
	}
	
	public String remoteExecuteProcess(){
		HttpServletRequest req = ServletActionContext.getRequest();
		final Long id = Long.valueOf(req.getParameter("id"));
		boolean success = false;
		JSONObject ajaxJson = new JSONObject(); 
		List<Task> tasks = processDao.findTasks(id);
		int taskCount = tasks.size();
		if(taskCount == 0) {
			ajaxJson.put("error","No task in this process!");
			ajaxJson.put("status", "fail");
			ajaxJson.put("id", id);
			PageIOUtils.printToPage(ajaxJson.toString());
			return NONE;
		}
		List<Task> taskQueue = new ArrayList<Task>();
		taskQueue.add(tasks.get(0));
		tasks.remove(0);
		
		//将List中的task按逻辑排序
		int queueCount = taskQueue.size();
		while(queueCount < taskCount) {
			for(Iterator<Task> iter = tasks.iterator(); iter.hasNext();) {
				Task task = iter.next();
				for(Task queue : taskQueue) {
					if(queue.getId() == task.getPre_task_id()){
						taskQueue.add(task);
						iter.remove();
						queueCount++;
						break;
					}
				}
			}
		}
		try {
			for(Task task : taskQueue) {
				success = processor.remoteExecute(task, Params.EXECTYPE_SPOT);
				if(!success)
					break;
			}
		}catch (Exception e) {
			ajaxJson.put("error",e.getMessage());
		}
		ajaxJson.put("status", success == true ? "success":"fail");
		ajaxJson.put("id", id);
		PageIOUtils.printToPage(ajaxJson.toString());
		return NONE;
	}

	public ProcessDao getProcessDao() {
		return processDao;
	}

	public void setProcessDao(ProcessDao processDao) {
		this.processDao = processDao;
	}
}
