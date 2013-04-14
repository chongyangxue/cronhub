package org.cronhub.managesystem.modules.task.dao.impl;

import java.util.List;

import org.cronhub.managesystem.commons.dao.BaseRowMapper;
import org.cronhub.managesystem.commons.dao.bean.Process;
import org.cronhub.managesystem.commons.dao.bean.Task;
import org.cronhub.managesystem.commons.dao.config.FillConfig;
import org.cronhub.managesystem.modules.daemon.dao.IDaemonDao;
import org.cronhub.managesystem.modules.task.dao.ProcessDao;
import org.springframework.jdbc.core.JdbcTemplate;

public class ProcessDaoImpl implements ProcessDao {
	private JdbcTemplate jdbcTemplate;
	private IDaemonDao daemonDao;

	@Override
	public void insert(Process process) {
		final String sql = "INSERT INTO process(user_id, process_name, cron_exp," +
				"description, update_time) VALUES(?,?,?,?,?)";
		this.jdbcTemplate.update(sql, new Object[] {
				process.getUser_id(),
				process.getProcess_name(),
				process.getCron_exp(),
				process.getDescription(),
				process.getUpdate_time()
			});
	}
	
	@Override
	public void saveProcessTask(Long task_id,Long process_id, String pre_task_id) {
		final String sql = "INSERT INTO process_task(task_id, process_id, pre_task_id) VALUES(?,?,?)";
		this.jdbcTemplate.update(sql, new Object[] {task_id, process_id, pre_task_id});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Process> findAll() {
		String sql = "SELECT process.id as id, user_id, process_name, cron_exp, description from process";
		List<Process> processes = this.jdbcTemplate.query(sql, new BaseRowMapper(Process.class));
		return processes;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public  List<Process> findByPage(String orderLimit, FillConfig fillConfig) {
		String sql = "SELECT * from process " + orderLimit;
		List<Process> processes = this.jdbcTemplate.query(sql, new BaseRowMapper(Process.class));
		return processes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Task> findTasks(Long process_id) {
		String sql = "SELECT task.id as id, daemon_id, user_id, cron_exp, shell_cmd, must_replace_cmd,"
				+ "run_mode, run_start_reportaddress, run_end_reportaddress, is_process_node, "
				+ "is_process_chain, process_tasks, task.comment as comment, operate_uid,"
				+ "task.update_time, is_redo, end_redo_times, daemon.machine_ip as machine_ip, "
				+ "process_task.pre_task_id as pre_task_id "
				+ "FROM task LEFT JOIN daemon on task.daemon_id = daemon.id "
				+ "LEFT JOIN process_task on task.id = process_task.task_id "
				+ "WHERE process_task.process_id = " + process_id;
		List<Task> tasks = this.jdbcTemplate.query(sql, new BaseRowMapper(Task.class));
		for(Task task : tasks){
			task.setDaemon(this.daemonDao.findById(task.getDaemon_id()));
		}
		return tasks;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Task getPreTask(Long process_id, Long task_id) {
		String sql = "SELECT task.id as id,daemon_id,cron_exp,shell_cmd, must_replace_cmd,"
				+ "run_mode, run_start_reportaddress, run_end_reportaddress, is_process_node, "
				+ "is_process_chain, process_tasks, task.comment as comment, operate_uid,"
				+ "task.update_time, is_redo, end_redo_times, daemon.machine_ip as machine_ip "
				+ "FROM task LEFT JOIN daemon on task.daemon_id = daemon.id "
				+ "join process_task on task.id = process_task.pre_task_id "
				+ "WHERE process_task.process_id = " + process_id
				+ " AND process_task.task_id = " + task_id;
		List<Task> tasks = this.jdbcTemplate.query(sql, new BaseRowMapper(Task.class));
		if(tasks != null && tasks.size() > 0)
			return tasks.get(0);
		return null;
	}
	
	public IDaemonDao getDaemonDao() {
		return daemonDao;
	}

	public void setDaemonDao(IDaemonDao daemonDao) {
		this.daemonDao = daemonDao;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void deleteById(Long id) {
		String sql = "DELETE FROM process WHERE id = " + id;
		this.jdbcTemplate.update(sql);
	}
	
	@Override
	public boolean deleteProcessTaskById(Long task_id) {
		String checkSql = "SELECT COUNT(*) FROM process_task WHERE pre_task_id = " + task_id;
		int childCount = this.jdbcTemplate.queryForInt(checkSql);
		if(childCount > 0) {
			new Exception("This task has child task, can not delete");
			return false;
		}
		String sql = "DELETE FROM process_task WHERE task_id = " + task_id;
		this.jdbcTemplate.update(sql);
		return true;
	}
}
