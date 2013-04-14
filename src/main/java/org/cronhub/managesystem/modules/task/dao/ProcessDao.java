package org.cronhub.managesystem.modules.task.dao;

import java.util.List;

import org.cronhub.managesystem.commons.dao.bean.Process;
import org.cronhub.managesystem.commons.dao.bean.Task;
import org.cronhub.managesystem.commons.dao.config.AssociateDeleteConfig;
import org.cronhub.managesystem.commons.dao.config.FillConfig;

public interface ProcessDao {
	/**
	 * 查询所有的流程
	 * @return
	 */
	List<Process> findAll();
	
	/**
	 * 查询流程内的所有任务
	 * @param process_id	流程ID
	 * @return
	 */
	List<Task> findTasks(Long process_id);

	/**
	 * 查询流程任务的前提任务
	 * @param process_id	流程ID
	 * @param task_id		任务ID
	 * @return
	 */
	Task getPreTask(Long process_id, Long task_id);

	/**
	 * 添加新的流程
	 * @param process
	 */
	void insert(Process process);

	/**
	 * 分页查询流程
	 * @param orderLimit
	 * @param fillConfig
	 * @return
	 */
	List<Process> findByPage(String orderLimit, FillConfig fillConfig);

	/**
	 * 根据ID删除流程
	 * @param id	流程ID
	 */
	void deleteById(Long id);

	/**
	 * 插入一条新的流程任务
	 * @param task_id		任务ID
	 * @param process_id	流程ID
	 * @param pre_task_id	要添加任务的前提任务ID
	 */
	void saveProcessTask(Long task_id, Long process_id, String pre_task_id);

	/**
	 * 删除一条流程任务
	 * @param task_id	任务ID
	 * @return
	 */
	boolean deleteProcessTaskById(Long task_id);
}
