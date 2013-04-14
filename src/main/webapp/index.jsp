<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<title>分布式cron调度系统</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #016aa9;
	overflow:hidden;
}
.STYLE1 {
	color: #000000;
	font-size: 12px;
}
-->
</style></head>

<body>
<s:set name="f" value="flag"/> 
<s:if test='#f=="not_exist"'>
	<script type="text/javascript">alert("用户未注册！");</script>
</s:if>
<s:elseif test='#f=="wrong_pwd"'>
	<script type="text/javascript">alert("用户名或密码错误！");</script>
</s:elseif>


<form id="form1" action="loginAction.action" method="post">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
  <tr>
    <td><table width="962" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td height="235" background="<%=basePath%>res/images/login_03.gif">&nbsp;</td>
      </tr>
      <tr>
        <td height="53"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td width="394" height="53" background="<%=basePath%>res/images/login_05.gif">&nbsp;</td>
            <td width="206" background="<%=basePath%>res/images/login_06.gif"><table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td width="18%" height="25"><div align="right"><span class="STYLE1">用户名</span></div></td>
                <td width="57%" height="25"><div align="center">
                  <input type="text" name="user_id" style="width:105px; height:17px; background-color:#292929; border:solid 1px #7dbad7; font-size:12px; color:#6cd0ff">
                </div></td>
                <td width="27%" height="25">&nbsp;</td>
              </tr>
              <tr>
                <td height="25"><div align="right"><span class="STYLE1">密码</span></div></td>
                <td height="25"><div align="center">
                  <input type="password" name="password" style="width:105px; height:17px; background-color:#292929; border:solid 1px #7dbad7; font-size:12px; color:#6cd0ff">
                </div></td>
                <td height="25"><div align="left"><input type="image" src="<%=basePath%>res/images/dl.gif"  onClick="form1.submit()" width="49" height="18" border="0"></div></td>
              </tr>
            </table></td>
            <td width="362" background="<%=basePath%>res/images/login_07.gif">&nbsp;</td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td height="213" background="<%=basePath%>res/images/login_08.gif">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
</body>
</html>
