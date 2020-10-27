<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-cn">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<LINK href="${pageContext.request.contextPath}/css/buttonstyle.css" type="text/css" rel="stylesheet">
<LINK href="${pageContext.request.contextPath}/css/MainPage.css" type="text/css" rel="stylesheet">
<script type='text/javascript' src='${pageContext.request.contextPath}/script/pub.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/script/validate.js'></script>
<script language="javascript" src="${pageContext.request.contextPath }/script/jquery-1.4.2.js"></script>
<SCRIPT type="text/javascript">
function ini(){
   //document.all.name.focus();
   $("input[name='name']").focus();
}

/*
   	DOM对象
*/


/* function check(){
    var theForm = document.forms[0];
	if(Trim(theForm.name.value)==""){
		alert("请输入用户名");
		theForm.name.focus();
		return false;
	}else if(Trim(theForm.checkNumber.value)==""){
		alert("请输入验证码");
		theForm.checkNumber.focus();
		return false;	
	}
	theForm.action = "${pageContext.request.contextPath }/system/elecMenuAction_menuHome.do";
	theForm.submit();//表单中有路径直接submit即可,无路径要添加路径
	
	return true; 
} */



 /**jquery对象*/
 function check(){
    if($.trim($("input[name='name']").val())=="")
	{
		alert("请输入用户名");
		$("input[name='name']").focus();
		return false;
	}else if($.trim($("#checkNumber").val())=="")
	{
		alert("请输入验证码");
		$("#checkNumber").focus();
		return false;
	}
 	$("form:first").attr("action","${pageContext.request.contextPath }/system/elecMenuAction_menuHome.do");
    $("form:first").submit();	
 }

/*dom对象*/
/* function checkNumberImage(){
	var imageNumber = document.getElementById("imageNumber");
	imageNumber.src = "${pageContext.request.contextPath}/image.jsp?timestamp="+new Date().getTime();
} */

/*jquery对象*/
function checkNumberImage(){
	//$("#imageNumber").attr("src","${pageContext.request.contextPath}/image.jsp?timestamp="+new Date().getTime());
	$("img[name='imageNumber']").attr("src","${pageContext.request.contextPath}/image.jsp?timestamp="+new Date().getTime());
}

function checkFunction(){
	return check();
}
</SCRIPT>
<STYLE type=text/css>
BODY { margin: 0px; }
FORM {
	MARGIN: 0px; BACKGROUND-COLOR: #ffffff
} 
</STYLE>
<title>国家电力监测中心</title>
</head>
<%--
	缺点：将java代码放置到jsp上，要求jsp先要执行编译java代码，然后再执行。效率会降低
	
	
	<%=password %> 下面写法的取值方式
	
	String name = "";
	String password = "";
	String checked = "";
	//从Cookie中获取登录名和密码
	Cookie[] cookies = request.getCookies();
	if(cookies!=null && cookies.length>0){
		for(Cookie cookie : cookies){
			if("name".equals(cookie.getName())){
				//用户名
				name = cookie.getValue();
				checked = "checked";
			}
			if("password".equals(cookie.getName())){
				//密码
				password = cookie.getValue();
			}			
		}
	}
 --%>
<body onload="ini()">
<!-- action="${pageContext.request.contextPath}/system/elecMenuAction_menuHome.do" -->
<form method="post" target="_top">
<table border="0" width="100%" id="table1" height="532" cellspacing="0" cellpadding="0" >
	<tr>
		<td>　</td>
	</tr>
	<tr>
		<td height="467">
		<table border="0" width="1024" id="table2" height="415" cellspacing="0" cellpadding="0" >
		<br><br><br><br><br>
			<tr>
				<td width=12%></td>
				<td align=center background="${pageContext.request.contextPath}/images/index.jpg">
				<table border="0" width="98%" id="table3" height="412" cellspacing="0" cellpadding="0">
					<tr height=122>
						<td colspan=2></td>
					</tr>
					<tr>
						<td height="313" width="73%"></td>
						<td height="99" width="27%">
							<table border="0" width="70%" id="table4">
								<s:actionerror/>
								<tr>
									<td width="100"><img border="0" src="${pageContext.request.contextPath}/images/yonghu.jpg" width="75" height="20"></td>
									<td><input type="text" name="name" style="width: 125 px" size="20" value="${requestScope.name}"  maxlength="25"></td>
	
								</tr>
								<tr>
									<td width="100"><img border="0" src="${pageContext.request.contextPath}/images/mima.jpg" width="75" height="20"></td>
									<td><input type="password" name="password" style="width: 125 px" size="20" value="${requestScope.password}"  maxlength="25"></td>
								</tr>
								
								<tr>
									<td width="100"><img border="0" src="${pageContext.request.contextPath}/images/check.jpg" width="75" height="20"></td>
									<td>
										<table>
											<tr>
												<td>
													<input type="text" name="checkNumber" id="checkNumber" value=""  maxlength="4" size="7">
												</td>
												<td>
													<img src="${pageContext.request.contextPath}/image.jsp" name="imageNumber" id="imageNumber" style="cursor:hand" title="点击可更换图片" height="20" onclick="checkNumberImage()"/>
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td width="100"><img border="0" src="${pageContext.request.contextPath}/images/remeber.jpg" width="75" height="20"></td>
									<td>
										<s:if test="#request.checked.equals('checked')">
											<input type="checkbox" name="remeberMe" id="remeberMe" value="yes" checked="checked"/>
										</s:if>
										<s:else>
											<input type="checkbox" name="remeberMe" id="remeberMe" value="yes" />
										</s:else>
									</td>
								</tr>
								
								<tr>
									<td width="100"></td>
									<td width="100"><input type="button" class=btn_mouseout onmouseover="this.className='btn_mouseover'" onmouseout="this.className='btn_mouseout'" value="登   录" name="huifubtn" onclick="checkFunction()"></td>

								</tr>
							</table>
						</td>
					</tr>
					
					</table>
					<s:debug></s:debug>
				</td>
				<td width=13%></td>
			</tr>
			<tr>
		      <td align="center" colspan=3>&nbsp;</td>
	        </tr>
		</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
