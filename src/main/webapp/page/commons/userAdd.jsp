<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
	var validator = $("#formUserAdd").validate({
	    success: function(label) {
		   label.html("&nbsp;").addClass("valid");
	    }
	});
});

</script>
</head>

<body>
<s:set name="code" value="errCode"/>
<s:if test='#code=="duplicate"'>
	<script type="text/javascript">alert("用户ID已注册！");</script>
</s:if>
	<div class="tab">
		<a class="tab-selected"><img src="<%=basePath%>res/icons/16x16/application_side_list.png" />注册新用户</a>
	</div>
	<form id="formUserAdd" name="userAdd" action="addAuthUser.action" method="post">
		<table class="formtable">
		
		<tr>
			<td width="200">用户ID：</td>
			<td>
				<div>
				<input type="text" name=authUser.user_id class="input-text" />
				<b>*</b>
				<span>请准确填写公司账号ID</span>
				</div>
			</td>
		</tr>
		<tr>
			<td width="200"><label>用户姓名：</label></td>
			<td>
				<div>
				<input type="text" name="authUser.user_name" class="input-text" />
				<b>*</b>
				</div>
			</td>
		</tr>
		<tr>
			<td width="200"><label>用户类型：</label></td>
			<td>
				<div>
				<select name="authUser.user_type" class="input-select">
					<option value="1">管理员</option>
					<option value="0">普通用户</option>
				</select>
				</div>
			</td>
		</tr>
		<tr>
			<td width="200"><label>用户部门：</label></td>
			<td>
				<div>
				<input type="text"  name="authUser.department" class="input-text" />
				</div>
			</td>
		</tr>
		<tr>
			<td width="200"><label>联系电话：</label></td>
			<td>
				<div>
				<input type="text" name="authUser.phone" class="input-text" />
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
