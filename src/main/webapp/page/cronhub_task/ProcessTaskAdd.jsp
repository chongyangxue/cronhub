<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "  http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>form</title>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />

<script type="text/javascript" src="<%=basePath%>res/js/jquery/jquery-1.6.2.min.js"></script>

<script type="text/javascript" src="<%=basePath%>res/js/validate/jquery.metadata.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/validate/messages_cn.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/validate/extension.js"></script><!-- 这个是我用来自定义一些验证函数的,比如到后台验证crontab表达式的有效性等 -->
<script type="text/javascript" src="<%=basePath%>res/js/zdialog/zDialog.js"></script> 
<link rel="stylesheet" type="text/css" href="<%=basePath%>res/skin/all.css" />

<script>
$(function(){
	// 表单校验属性设置
	$.metadata.setType("attr", "validate");
	
	// 表单校验
	var validator = $("#formTaskAdd").validate({
	    success: function(label) {
		   label.html("&nbsp;").addClass("valid");
	    }
	});
	
	$("#ddl_cronhub_daemon").change(function(){
		$("#machineIdTR,#machinePortTR,#daemonIdTR,#div_daemonid,#div_machineip").hide();
		if($(this).val() == "daemon_id"){
			$("#div_daemonid").show();
		}else{
			$("#div_machineip").show();
		}
	});
	initDaemonIpList();
	initByParentPage();

	//一期暂不支持被动模式
	$("input[@type=radio][name='task.run_mode']").change(function(){
		if("false"!=$(this).val())
			{
				Dialog.alert('暂不支持"主动模式"功能,即daemon无需中央服务器通知的模式.');
				$("input[@type=radio][name='task.run_mode'][value='false']").attr("checked",'checked'); 
			}
		});
});

function initByParentPage(){
	if(parent.process_id==0){return;}
	$("#process_id").val(parent.process_id).attr("readonly","readonly");
	parent.process_id=0;
}

function initDaemonIpList(){
	var daemon_ip = $("#daemon_id");
	$.ajax({
		url: "<%=basePath%>daemon/getDaemonList.action",
		async: true,
		type: 'get',
		dataType: 'json',
		success: function(data) {
			var json = data.daemons;
			for (var index = 0; index < json.length; index ++) {
				daemon_ip.append("<option value=" + json[index].id + ">" + json[index].machine_ip + "</option>");  
            }
		}
	}); 
	
	var preTaskId = $("#preTaskId");
	$.ajax({
		url: "<%=basePath%>process/getPreTaskId.action",
		data: {"process.id":parent.process_id},
		async: true,
		type: 'get',
		dataType: 'json',
		success: function(data) {
			var json = data.task_id_List;
			for (var ind = 0; ind < json.length; ind ++) {
				preTaskId.append("<option value=" + json[ind] + ">" + json[ind] + "</option>");  
            }
		}
	});
}
</script>


</head>

<body>
<div id="errorMsg" > 信息内容:</div>
	<div class="tab">
		<a class="tab-selected"><img src="<%=basePath%>res/icons/16x16/application_side_list.png" />新增流程任务</a>
	</div>
	<form id="formTaskAdd" name="formCronhubTaskAdd" action="<%=basePath%>process/process_task_add.action" method="post">
		<table class="formtable">
		<tr>
			<td width="200">流程id：</td>
			<td>
				<input type="text" id="process_id" name="process.id" class="input-text" />
				<b>*</b>
			</td>
		</tr>
		<tr>
			<td width="200">daemon执行器IP：</td>
			<td>
				<div id="div_daemonid">
				<select id="daemon_id" name="task.daemon_id" class="input-select"  validate="{required: true}">
				</select>
				<b>*</b>
				</div>
			</td>
		</tr>
		<tr>
			<td width="200">前提任务ID：</td>
			<td>
				<div id="div_preTaskId">
				<select id="preTaskId" name="pre_task_id" class="input-select">
					<option value="0"></option>
				</select>
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<label>执行脚本命令：</label>
			</td>
			<td>
				<div>
				<input type="text" id="shellCmd" name="task.shell_cmd" class="input-text" validate="{required: true}"/>
				<b>*</b>
				<span>请采用unix/linux的shell支持的命令</span>
				</div>
			</td>
		</tr>
		<tr>
			<td>
			<label>是否支持参数替换(命令中参数还原为现场时间)：</label>
			</td>
			<td>
			<div>
			<input type="radio" name="task.must_replace_cmd" value="true" checked="checked" validate="{required: true}"></radio> 支持 &nbsp;&nbsp;&nbsp;<input type="radio"  name="task.must_replace_cmd" value="false"></radio> 不支持
			<b>*</b>
			<span>[说明]支持:命令中的`中间所夹的内容全部被替换为shell执行后的值,而存入历史数据库记录中的参数为所夹内容执行的返回值.  不支持:命令中的`所夹内容不会被替换,而存入数据库中的仍为``参数,在隔天运行且要得到当时现场的值时有风险</span>
			</div>
			</td>
		</tr>
		<tr>
			<td>
				<label>运行模式：</label></td><td>
				<div>
				<input type="radio" name="task.run_mode" value="true"  validate="{required: true}" ></radio> 主动 &nbsp;&nbsp;&nbsp;<input type="radio" name="task.run_mode" value="false" checked="checked" validate="{required: true}" ></radio> 被动
				<b>*</b>
				<span>主动模式:crontab刷至执行daemon程序,由daemon执行程序自主执行,无需中央服务器通知调度.被动模式:由中央服务器定时调度</span>
				</div>
			</td>
			</tr>
			<tr>
			<td>
				<label>失败时自动重新执行</label></td><td>
				<div>
				<select id="ddl_is_redo" class="input-shortselect" name="task.is_redo" onChange="$(this).val() == 'true' ? $('#div_redoEndTimes').fadeIn('slow') : $('#div_redoEndTimes').hide();$('#txtRedoEndTimes').val('0')"><option value="false" checked="checked">否</option><option value="true">是</option></select>
				<span id="div_redoEndTimes" style="display:none">
				<input type="text" id="txtRedoEndTimes"  name="task.end_redo_times" value="0" onClick="if($(this).val()=='请填入截止执行次数...'){$(this).val('');}"  class="input-text" validate="{required: true,digits:true}" />
				</span>
				<b>*</b>
				<span>失败时自动重新执行的截止次数</span>
				</span>
				</div>
			</td>
		</tr>
			<tr>
			<td>
				<label>描述：</label>
			</td>
			<td>
				<div>
				<textarea id="txtComment" name="task.comment" class="input-textarea" validate="{required: true}"></textarea>
				<b>*</b>
				</div>
			</td>
		</tr>
		</table>
	    <div class="toolbar">
		    <button type="submit"><img src="<%=basePath%>res/icons/16x16/tick.png">确定</button>
			<button type="button" onclick="validator.resetForm()"><img src="<%=basePath%>res/icons/16x16/arrow_redo.png">清空</button>
	    </div>
	</form>
</body>
</html>
</html>
