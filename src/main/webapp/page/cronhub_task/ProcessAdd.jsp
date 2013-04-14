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
	var validator = $("#formProcessAdd").validate({
	    success: function(label) {
		   label.html("&nbsp;").addClass("valid");
	    }
	});
});

</script>
</head>

<body>
	<div class="tab">
		<a class="tab-selected"><img src="<%=basePath%>res/icons/16x16/application_side_list.png" />新增调度流程</a>
	</div>
	<form id="formProcessAdd" name="formCronhubProcessAdd" action="<%=basePath%>process/process_add.action" method="post">
		<table class="formtable">
		
		<tr>
			<td width="200">流程名称：</td>
			<td>
				<div>
				<input type="text" id="txt_daemonid" name="process.process_name" class="input-text" />
				<b>*</b>
				</div>
			</td>
		</tr>
		<tr>
			<td width="200"><label>crontab表达式(cron_exp)：</label></td>
			<td>
				<div>
				<input type="text" id="process_cron_exp" name="process.cron_exp" class="input-text" validate="{required: true, isValidCronexp: true}" />
				<b>*</b>
				<span>请采用unix/linux的crontab的时间格式表达式</span>
				</div>
			</td>
		</tr>
			<tr>
			<td>
				<label>描述：</label>
			</td>
			<td>
				<div>
				<textarea id="process_description" name="process.description" class="input-textarea"></textarea>
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
