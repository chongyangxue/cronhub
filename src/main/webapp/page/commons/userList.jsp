<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
<script type="text/javascript" src="<%=basePath%>res/js/pager/pager.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/utils/json2.js"></script>
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
	deleteOne();
});

function deleteOne(){
	$(".grid tr img[name='del']").click(function(){
		var del_id = $(this).attr("title");
		Dialog.confirm("确认删除此用户?",function(){
			$.ajax({
				url: "<%=basePath%>deleteUser.action",
				data: {"authUser.user_id":del_id},
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
						Dialog.alert("删除id:"+del_id+"时出现错误.");
					}
				}
			});	
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
		<a class="tab-selected"><img src="<%=basePath%>res/icons/16x16/application_side_list.png" />用户管理</a>
	</div>
	<table class="grid">
		<thead>
			<tr>
				<th width="10%"><span>用户ID</span></th>
				<th width="10%"><span>用户姓名</span></th> 
				<th width="10%"><span>用户类型</span></th>
				<th width="10%"><span>用户部门</span></th>
				<th width="10%"><span>联系电话</span></th>
				<th width="5%"><span>删除</span></th>				
			</tr>
		</thead>
		<tbody>
			<s:iterator value="#request.userList" id="users" status="statu">
				<tr id="<s:property value='#users.user_id' />">
					<td align="center"><span><s:property value="#users.user_id" /></span></td>
					<td align="center"><span><s:property value="#users.user_name" /></span></td>
					<td align="center"><span><s:if test='#users.user_type=="1"'>管理员</s:if><s:else>普通用户</s:else></span></td>
					<td align="center"><span><s:property value="#users.department" /></span></td>
					<td align="center"><span><s:property value="#users.phone" /></span></td>
					<td align="center"><span><img title="<s:property value='#users.user_id' />" name="del" style="cursor:pointer" src="<%=basePath%>res/icons/16x16/cancel.png"/></span></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
</html>
