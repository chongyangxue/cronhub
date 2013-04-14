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

<style type="text/css">
img[src="<%=basePath%>res/icons/16x16/magnifier.png"]{
	cursor:pointer;
}
</style>
</head>

<body>
	<div class="tab">
		<a class="tab-selected"><img src="<%=basePath%>res/icons/16x16/application_side_list.png" />登录日志</a>
	</div>
	<table class="grid" width="60%">
		<thead>
			<tr>
				<th width="50%"><span>用户ID</span></th>
				<th width="50%"><span>登录时间</span></th> 
			</tr>
		</thead>
		<tbody>
			<s:iterator value="#request.logList" id="list" status="statu">
				<tr>
					<td align="center"><span><s:property value="#list.user_id" /></span></td>
					<td align="center"><span><s:date name="#list.login_time" format="yyyy-MM-dd HH:mm:ss"/></span></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</body>
</html>
