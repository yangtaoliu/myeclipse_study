<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/base/tag.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 引用jquery easy ui的js库及css -->
<LINK rel="stylesheet" type="text/css"
	href="${baseurl}js/easyui/styles/default.css">
<%@ include file="/WEB-INF/jsp/base/common_css.jsp"%>
<%@ include file="/WEB-INF/jsp/base/common_js.jsp"%>
<title>用户管理</title>

<script type="text/javascript">
  function systemConfigsave(){
	
	  jquerySubByFId('systemConfigForm',systemConfigsave_callback,null,"json");
	  
  }	
  
  //ajax调用的回调函数，ajax请求完成调用此函数，传入的参数是action返回的结果
  function systemConfigsave_callback(data){
	  message_alert(data);
  }  	
</script>

</head>
<body>

  <!-- html的静态布局 -->
  <form id="systemConfigForm" action="${baseurl}user/systemConfigSubmit.action" method="post">
	<!-- 查询条件 -->
	<TABLE class="table_search">
		<TBODY>
			<tr>系统参数配置</tr>
			<TR>
				<TD class="left" style="text-align: left; width: 200px;">配置项名称：</td>
				<TD class="left" style="text-align: left; width: 400px;">配置项值：</TD>
			</TR>
			<c:forEach items="${basicinfoList}" var="p">
				<input type="hidden" name="id" value="${p.id}"/>
				<TR>
					<TD><INPUT type="text" value="${p.name}" disabled  style="text-align: left; width: 200px;" /></td>
					<td><INPUT type="text" name="value" value="${p.value}" style="text-align: left; width: 400px;" /></TD>
				</TR>				
			</c:forEach>
			<tr>
			  <td colspan="12" style="text-align:center"><a id="btn" href="#" onclick="systemConfigsave()" class="easyui-linkbutton" iconCls='icon-search'>提交</a></td>
			</tr>
		</TBODY>
	</TABLE>
</form>
</body>
</html>