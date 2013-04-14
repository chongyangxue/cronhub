<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<title>CronHub调度系统</title>

<script type="text/javascript" src="<%=basePath%>res/js/jquery/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/navi/navi.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/zdialog/zDrag.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/zdialog/zDialog.js"></script>
<script type="text/javascript" src="<%=basePath%>res/js/utils/navigator_test.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath%>res/skin/all.css" />

<script>
$(function() {
	if('${sessionScope.userType}' == "admin")
		naviUrl='<%=basePath%>data/navi/navi_admin.xml';
	else 
		naviUrl='<%=basePath%>data/navi/navi.xml';
	// navi
	var navi = new NaviTree({
		container_horiz: $('#td-navi'),
		container_vertical: $('#left-navi'),
		loadUrl: naviUrl,
		dataType: 'xml',
		timeout: 10000,
		indent: 6,
		click: function(node) {
			node.toggle();
			if(node.url) {
				main.location.href = node.url;
			}
		}
	});
	navi.init();
//	$.easing.def = "easeInOutBounce"; 
	//$.easing.def = "jswing"; 
	$("#top-right").animate({ marginRight: '4em', opacity: "show"}, {duration: 2500});
	//$("#top-right").animate({ scrollLeft: $(this).scrollLeft()+100}, {duration: 2500});	
});
var daemon_id=0;
var machine_ip="";
var machine_port=0;
</script>

<style>
body {
	padding: 0;
	overflow: hidden;
}
#td-top {
	height: 50px;
	background: #f5f8f9;
	color: #e17009;
}
#td-navi {
	height: 30px;
	border: 1px solid #c5dbec;
	background: #dfeffc;
}
#td-left {
	width: 170px;
	vertical-align: top;
}
#td-toggle {
	width: 3px;
	padding: 0 1px;
	vertical-align: middle;
	border-left: 1px solid #c5dbec;
	border-right: 1px solid #c5dbec;
}
#top-logo {
	height: 50px;
	float: left;
	width: 279px; /*修改为logo图片width*/
	background: res/images/logo/logo.jpg; /*在这里添加logo图片路径*/
}
#top-right {
	color: #000000;
	height: 50px;
	line-height: 25px;
	text-align: center;
	white-space: nowrap;
	position: absolute;
	top: 0;
	right: 0;
}
#top-right span {
	color: #e17009;
	font-weight: bold;
}
#top-right a,
#top-right a:hover,
#top-right a:active,
#top-right a:visited {
	color: #0066cc;
	font-weight: bold;
}
#top-right a:hover {
	text-decoration: underline;
}
#left-navi {
	overflow: auto;
	height: 100%;
}
</style>
</head>

<body scroll="no">
	<table width="100%" height="100%">
	<tr>
		<td id="td-top">
			<div id="top-logo"><img src="res/images/logo/logo.jpg"/></div>
	 		<div id="top-right">
	 		<div>&nbsp;</div>
				<div>当前用户：<span><s:property value="#session.user_id"/></span>
				&nbsp;&nbsp;角色：<span><s:if test='#session.userType == "admin"'>管理员</s:if>
						   <s:else>普通用户</s:else>
				</span>
				&nbsp;&nbsp;<a href="logoutAction.action">注销</a>&nbsp;&nbsp; </div>
			</div>
		</td>
	</tr>
	<tr>
		<td id="td-navi">
			
		</td>
	</tr>
	<tr>
		<td>
			<table width="100%" height="100%">
			<tr>
				<td id="td-left">
					<div id="left-navi"/>
					</div>
				</td>
				<td id="td-toggle">
				</td>
				<td>
					<iframe id="main" name="main" width="100%" height="100%" scrolling="yes" frameborder="0"></iframe>
				</td>
			</tr>
			</table>
		</td>
	</tr>
	</table>
</body>
</html>
