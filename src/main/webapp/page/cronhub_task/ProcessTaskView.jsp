<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "  http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>list</title>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />

<script type="text/javascript" src="<%=basePath%>res/js/jquery/jquery-1.6.2.min.js"></script>

<script type="text/javascript" src="<%=basePath%>res/js/list/list.js"></script>

<script type="text/javascript" src="<%=basePath%>res/js/highlight/highlight.js"></script>

<script type="text/javascript" src="<%=basePath%>res/js/contextmenu/jquery.contextmenu.js"></script>

<script type="text/javascript" src="<%=basePath%>res/js/zdialog/zDrag.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/zdialog/zDialog.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/utils/json2.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/pager/pager.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/utils/string_utils.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>res/skin/all.css" />

<script>
var dialog = null;

$(function() {
	// 查询对话框
	dialog = new Dialog({
		Animator: false,
		Width: 420,
		Height: 200,
		Title: '查询对话框',
		InvokeElementId: 'dialog-form',
		OKEvent: function() {
			dialog.close();
			$('#form1').submit();
		}
	});
	initPagerBtnAndChangeFilter("<%=basePath%>task_first_page_view.action","<%=basePath%>task_prev_page_view.action","<%=basePath%>task_next_page_view.action","<%=basePath%>task_last_page_view.action");
	deleteOne();
	$("#btnImmediateExec").click(btnImmediateExecClick);
	hideLongText();
	$("#btnCmdUnfold").bind("click",{"name":"unfoldBtn"},cmdfoldreverse);
	$("#btnCmdfold").bind("click",	{"name":"foldBtn"},cmdfoldreverse);
	highlight_ok('crontab调度任务持续进行中...');
	setInterval("highlight_ok('crontab调度任务持续进行中...')",11000);
});
function validationIp(current_ip){
	var isIp = /^[\d|\.]+$/.test(current_ip);
	if(!isIp){
		return false;
	}
	return true;
}
function deleteOne(){
	$(".grid tr img[name='del']").click(function(){
		var del_id = $(this).attr("title");
		Dialog.confirm("将关联删除该task所发出的所有相关调度记录,是否继续?",function(){
			$.ajax({
				url: "<%=basePath%>task/deleteTask.action",
				data: {"id":del_id},
				cache: false,
				async: true,
				type: 'POST',
				dataType: 'text',
				timeout: 10000,
				error: function() {
					Dialog.alert('删除失败，请检查关联的任务');
				},
				success: function(data) {
					if(data == "success"){
					Dialog.alert("删除成功");
					$(".grid tr#"+del_id).hide();
					}else{
						Dialog.alert("删除id:"+del_id+"时出现错误.");
					}
				}
			});	
		});
	});
}

function btnImmediateExecClick(){
	var checkCount=0;
	$('.grid tbody input[type=checkbox]').each(function(i, o) {
		if($(o).attr('checked')) {
			checkCount++;
		}
	});
	if(checkCount<=0){
		Dialog.alert("请选中至少一条记录进行操作");
		return;
	}
	Dialog.confirm("确认要当场执行吗?执行结果会记入到调度结果记录中",function(){
	var loadingImg = "<%=basePath%>res/images/gif/loading.gif";
	var successImg = "<%=basePath%>res/icons/16x16/accept.png";
	var failImg ="<%=basePath%>res/icons/16x16/exclamation.png"
	$('.grid tbody input[type=checkbox]').each(function(i, o) {
		if($(o).attr('checked')){
			var id = $(o).val();
			$("tr[id='"+id+"'] img[title='"+id+"'][name='status']").attr("src",loadingImg);
			$.ajax({
				url: "<%=basePath%>task/remoteExec.action",
				data: {"id":id},
				cache: false,
				async: true,
				type: 'POST',
				dataType: 'json',
				timeout: 8640000,
				error: function() {
					Dialog.alert('对不起，服务器响应超时，请联系管理员');
				},
				success: function(result) {
					if(result.hasOwnProperty("error")){
						Dialog.alert(result["error"]);
					}
					var status = result["status"];
					var ret_id = result["id"];
					$("tr[id='"+ret_id+"'] span img[title='"+ret_id+"'][name='status']").attr("src",status=="success"?successImg:failImg);
				}
				});
			}
		});		
	});
}

function hideLongText(){
	$(".grid tbody td.cmdClass").each(function(){
		var innerStr = $(this).text();
		var innerStr = $.trim(innerStr);	
		if(innerStr.length>11){
			var ellipsis = $("<span class='toolbar' title='"+innerStr+"' name='unfoldBtn'><a>...</a></span>").click(function(){
				var wholeStr = $(this).attr('title');
				var currentFold = $(this).parent().contents().clone(true);
				var parent = $(this).parent();
				var foldBtn = $("<span class='toolbar' name='foldBtn'><a>&lt;&lt;</a>").click(function(){
					parent.html("").append(currentFold);
				});
				$(this).parent().html(wholeStr).append(foldBtn);
			});
			var shortStr = innerStr.substr(0,11);
			$(this).html(shortStr).append(ellipsis);
		}
	});
}
</script>
<style type="text/css">
img[src="<%=basePath%>res/icons/16x16/magnifier.png"]{
	cursor:pointer;
}
</style>
</head>

<body>
	<div class="tab">
		<a class="tab-selected"><img src="<%=basePath%>res/icons/16x16/application_side_list.png" />流程任务列表</a>
	</div>
	<div class="toolbar">
		<a id="btnImmediateExec"><img src="<%=basePath%>res/icons/16x16/application_osx_terminal.png" />当场执行选中的任务</a>
	</div>
	<table class="grid">
		<thead>
			<tr>
				<th><span><input type="checkbox" value="0"/></span></th>
				<th><span>task id</span></th>
				<th><span>前提任务id</span></th>
				<th><span>流程id</span></th>
				<th><span>daemon名字</span></th>
				<th><span>机器ip</span></th>
				<th><span>端口号</span></th>
				<th><span>任务命令</span></th>
				<th><span>简介</span></th>
				<th><span>替换参数</span></th>
				<th><span>重执行</span></th>
				<th><span>通信</span></th>
				<th><span>更新时间</span></th>
				<th width="15"><span>删除</span></th>				
			</tr>
		</thead>
		<tbody>
			<s:iterator value="#request.processTask" id="taskbean" status="statu">
			<tr id="<s:property value='#taskbean.id' />">
				<td align="center"><span><input type="checkbox" value="<s:property value='#taskbean.id' />" /></span></td>
				<td align="center"><span><s:property value="#taskbean.id" /></span></td>
				<td align="center"><span><s:property value="#taskbean.pre_task_id" /></span></td>
				<td align="center"><span><s:property value="#request.process.id" /></span></td>
				<td align="center" class="cmdClass"><s:property value="#taskbean.daemon.daemon_version_name" /></td>
				<td align="center"><span><s:property value="#taskbean.daemon.machine_ip" /></span></td>
				<td align="center"><span><s:property value="#taskbean.daemon.machine_port" /></span></td>
				<td align="center" class="cmdClass"><s:property value="#taskbean.shell_cmd" /></td>
				<td align="center" class="cmdClass"><s:property value='#taskbean.comment' /></td>
				<td align="center"><span>${taskbean.must_replace_cmd == true ? '<font style="color:green">是</font>':'<font style="color:red">否</font>'}</span></td>
				<td align="center"><span>${taskbean.is_redo == true ? '<font style="color:green">是</font>':'<font style="color:red">否</font>'}</span></td>
				<td align="center"><span><s:if test="#taskbean.daemon.conn_status==true"><font style="color:green">通信正常</font></s:if><s:else><font style="color:red">通信失败</font></s:else></span></td>
				<td align="center"><span><s:date name="#taskbean.update_time" format="yyyy-MM-dd HH:mm:ss"/></span></td>
				<td align="center"><span><img title="<s:property value='#taskbean.id' />" name="del" style="cursor:pointer" src="<%=basePath%>res/icons/16x16/cancel.png"/></span></td>
			</tr>
			<div id="comment_<s:property value='#taskbean.id' />" style="display:none"><s:property value='#taskbean.comment' /></div>
			</s:iterator>
		</tbody>
	</table>
</body>
</html>
