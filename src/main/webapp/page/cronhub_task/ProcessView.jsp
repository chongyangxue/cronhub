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
	initPagerBtnAndChangeFilter("<%=basePath%>task_first_page_view.action",
			"<%=basePath%>task_prev_page_view.action",
			"<%=basePath%>task_next_page_view.action",
			"<%=basePath%>task_last_page_view.action");
	deleteOne();
	$("#btnImmediateExec").click(btnImmediateExecClick);
/* 	hideLongText();
	$("#btnCmdUnfold").bind("click",{"name":"unfoldBtn"},cmdfoldreverse);
	$("#btnCmdfold").bind("click",	{"name":"foldBtn"},cmdfoldreverse);
	highlight_ok('crontab调度任务持续进行中...');
	setInterval("highlight_ok('crontab调度任务持续进行中...')",11000); */
});

function deleteOne(){
	$(".grid tr img[name='del']").click(function(){
		var del_id = $(this).attr("title");
		Dialog.confirm("将关联删除该process所发出的所有相关调度记录,是否继续?",function(){
			$.ajax({
				url: "<%=basePath%>process/deleteProcess.action",
				data: {"id":del_id},
				cache: false,
				async: true,
				type: 'POST',
				dataType: 'text',
				timeout: 10000,
				error: function() {
					Dialog.alert('对不起，服务器响应超时，请联系管理员');
				},
				success: function(data) {
					if(data == "success"){
					Dialog.alert("删除成功");
					$(".grid tr#"+del_id).hide();
					}else{
						Dialog.alert("删除id:" + del_id + "时出现错误.");
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
				url: "<%=basePath%>process/remoteExec.action",
				data: {"id":id},
				cache: false,
				async: true,
				type: 'POST',
				dataType: 'json',
				timeout: 8640000,
				error: function() {
					Dialog.alert('执行过程出错，请检查流程中的任务状态！');
				},
				success: function(result) {
					if(result.hasOwnProperty("error")){
						Dialog.alert(result["error"]);
					}
					var status = result["status"];
					var ret_id = result["id"];
					if(status == "success") {
						Dialog.alert('流程执行成功！');
					}
					//$("tr[id='"+ret_id+"'] span img[title='"+ret_id+"'][name='status']").attr("src",status=="success"?successImg:failImg);
				}
				});
			}
		});		
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
		<a class="tab-selected"><img src="<%=basePath%>res/icons/16x16/application_side_list.png" />Process流程列表</a>
	</div>
	<div class="toolbar">
		
		<a id="btnImmediateExec"><img src="<%=basePath%>res/icons/16x16/application_osx_terminal.png" />当场执行</a>
		<%--
	 	<a id="btnCmdUnfold"><img src="<%=basePath%>res/icons/16x16/table_sort.png" />展开</a>
		<a id="btnCmdfold"><img src="<%=basePath%>res/icons/16x16/table_sort.png" />折叠</a> 
		--%>
	</div>
	<table class="grid">
		<thead>
			<tr>
				<th width="5%"><span><input type="checkbox" value="0"/></span></th>
				<th width="5%"><span>process id</span></th>
				<th width="10%"><span>流程名称</span></th>
				<th width="10%"><span>创建人</span></th>
				<th width="10%"><span>cron_exp</span></th>
				<th width="8%"><span>管理流程任务</span></th>
				<th width="7%"><span>添加流程任务</span></th>
				<th width="15%"><span>简介</span></th>
				<th width="15%"><span>更新时间</span></th>
				<th width="10%"><span>删除</span></th>				
			</tr>
		</thead>
		<tbody>
			<s:iterator value="#request.beanlist" id="process" status="statu">
			<tr id="<s:property value='#process.id' />">
				<td align="center"><span><input type="checkbox" value="<s:property value='#process.id' />" /></span></td>
				<td align="center"><span><s:property value="#process.id" /></span></td>
				<td align="center"><span><s:property value="#process.process_name" /></span></td>
				<td align="center"><span><s:property value="#process.user_id" /></span></td>
				<td align="center"><span><s:property value="#process.cron_exp" /></span></td>
				<td align="center">
					<span><img title="管理流程任务" onClick="window.location.href='<%=basePath%>process/process_task_view.action?process.id=<s:property value="#process.id"/>'"
						src="<%=basePath%>res/icons/16x16/application_side_list.png" style="cursor:pointer"/>
					</span>
				</td>
				<td align="center">
					<span><img title="添加新任务" 	onClick="parent.process_id=<s:property value='#process.id'/>;window.location='<%=basePath%>page/cronhub_task/ProcessTaskAdd.jsp';" 
						src="<%=basePath%>res/icons/16x16/table_add.png" style="cursor:pointer"/>
					</span>
				</td>
				<td align="center" class="cmdClass"><s:property value='#process.description' /></td>
				<td align="center"><span><s:date name="#process.update_time" format="yyyy-MM-dd HH:mm:ss"/></span></td>
				<td align="center"><span><img title="<s:property value='#process.id' />" 
					name="del" style="cursor:pointer" src="<%=basePath%>res/icons/16x16/cancel.png"/></span>
				</td>
				
			</tr>
			<div id="comment_<s:property value='#taskbean.id' />" style="display:none"><s:property value='#taskbean.comment' /></div>
			</s:iterator>
		</tbody>
	</table>
<div id="divPager" class="toolbar">
		<a id="btnFirstPage" ><img src="<%=basePath%>res/icons/16x16/arrow_redo.png" />首页</a>
		<a id="btnPrevPage"><img src="<%=basePath%>res/icons/16x16/arrow_left.png" />上一页</a>
		<a id="btnNextPage"><img src="<%=basePath%>res/icons/16x16/arrow_right.png" />下一页</a>
		<a id="btnLastPage"><img src="<%=basePath%>res/icons/16x16/arrow_undo.png" />末页</a>
		<div id="hiddenPageNo" style='display:none'><s:property value='#request["current_page_no"]'/></div>
		<div id="hiddenMaxPerPage" style='display:none'><s:property value='#request["max_per_page"]'/></div>
		<div id="hidden_filter" style="display:none"><s:property value="#request.filter"/></div>
		共<font style="color:#8B0A50"><s:property value='#request["total_count"]'/>条</font>记录 共计<font style="color:#00688B">
			<span id="spanTotalPageNo"><s:property value='#request["total_page_count"]'/>
			</span>页</font> 当前<font style="color:#00688B">第<span id="spanCurrentPageNo">0</span>页</font>
		</select>
		每页显示
		<select id="ddlMaxPerPage">
			<option value="20">20条</option>
			<option value="50">50条</option>
			<option value="100">100条</option>
		</select>
	</div>

</body>
</html>
