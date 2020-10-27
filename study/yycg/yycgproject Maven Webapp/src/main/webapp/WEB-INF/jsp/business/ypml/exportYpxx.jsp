<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/jsp/base/tag.jsp"%>
<html> 
<head>
<title>药品目录导出</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<%@ include file="/WEB-INF/jsp/base/common_css.jsp"%>
<%@ include file="/WEB-INF/jsp/base/common_js.jsp"%>

<script type="text/javascript">

	
	//datagrid列定义
	var columns_v = [ [ {
		field : 'bm',//对应json中的key
		title : '流水号',
		width : 120
	}, {
		field : 'mc',//对应json中的key
		title : '通用名 ',
		width : 120
	}, 
	/* 
		id,bm,mc,jx,gg,zhxs,scqymc,spmc,zbjg,jyzt ,jyztmc

    */
	
	{
		field : 'jx',//对应json中的key
		title : '剂型',
		width : 120
	},{
		field : 'gg',//对应json中的key
		title : '规格',
		width : 120
	},{
		field : 'zhxs',//对应json中的key
		title : '转换系数',
		width : 120
	},{
		field : 'scqymc',//对应json中的key
		title : '生产企业',
		width : 220
	},{
		field : 'spmc',//对应json中的key
		title : '商品名称',
		width : 180
	},{
		field : 'zbjg',//对应json中的key
		title : '中标价',
		width : 120
	},{
		field : 'jyztmc',//对应json中的key
		title : '交易状态',
		width : 120
	}/* ,{
		field : 'opt1',//对应json中的key
		title : '删除',
		width : 120,
		formatter : function(value, row, index) {
			//alert(index);
			return "<a href=javascript:deleteuser('"+ row.id +"')>删除</a>";
		}
	}, {
		field : 'opt2',//对应json中的key
		title : '修改',
		width : 120,
		formatter : function(value, row, index) {
			return "<a href=javascript:edituser('"+row.id+"')>修改</a>";
		}
	} */ 
	] ];
		
	//加载datagrid
	//预加载方法，页面加载后执行次方法
	$(function() {
		$('#ypxxlist').datagrid({
			title : '药品信息查询',//数据列表标题
			nowrap : true,//单元格中的数据不换行，如果为true表示不换行，不换行情况下数据加载性能高，如果为false就是换行，换行数据加载性能不高
			striped : true,//条纹显示效果
			url : '${baseurl}ypml/queryYpxxResult.action',//加载数据的连接，连接请求过来是json数据
			idField : 'id',//此字段很重要，数据结果集的唯一约束(重要)，如果写错影响 获取当前选中行的方法执行
			loadMsg : '',
			columns : columns_v,
			pagination : true,//是否显示分页
			rownumbers : true,//是否显示行号
			pageList:[15,30,50]
		});
	});
	
	//查询方法
	function queryYpxx(){
		//datagrid的方法load方法要求传入json数据，最终将 json转成key/value数据传入action
		//将form表单数据提取出来，组成一个json
		var formdata = $("#ypxxlistFrom").serializeJson();
		$('#ypxxlist').datagrid('load',formdata);
	}
	
	
	
		//药品信息导出
	function ypxxexport(){
		//调用ajax Form提交
		jquerySubByFId('ypxxlistFrom',ypxxExprot_callback,null,"json");
		
	}
	function ypxxExprot_callback(data){
		
		//在这里提示信息中有文件下载链接
		message_alert(data);
		
	}

</script>

</head>
<body>
<!-- 导出条件 -->

<form id="ypxxlistFrom" name="ypxxlistFrom" action="${baseurl}ypml/exportYpxxSubmit.action" method="post">
<TABLE  class="table_search">
				<TBODY>
					<TR>
						<TD class="left">流水号：</TD>
						<td ><INPUT type="text" name="ypxxCustom.bm" /></td>
						<TD class="left">通用名：</td>
						<td><INPUT type="text"  name="ypxxCustom.mc" /></TD>
						
						<TD class="left">药品类别：</TD>
						<td >
							<select id="ypxxCustom.lb" name="ypxxCustom.lb" style="width:150px">
								<option value="">全部</option>
								<c:forEach items="${yplblist}" var="value">
									<option value="${value.id}">${value.info}</option>
								</c:forEach>
							</select>
						</td>
						<TD class="left">交易状态：</TD>
						<td >
							<select id="ypxxCustom.jyzt" name="ypxxCustom.jyzt" style="width:150px">
								<option value="">全部</option>
								<c:forEach items="${jyztlist}" var="value">
									<option value="${value.dictcode}">${value.info}</option>
								</c:forEach>
							</select>
						</td>
						<td><a id="btn" href="#" onclick="queryYpxx()"
							class="easyui-linkbutton" iconCls='icon-search'>查询</a></td>
					</TR>
					<tr>
					  <td colspan="12" style="text-align:center"><a id="btn" href="#" onclick="ypxxexport()" class="easyui-linkbutton" iconCls='icon-search'>导出</a></td>
					</tr>
				</TBODY>
			</TABLE>
			<!-- 查询列表 -->
			<TABLE border=0 cellSpacing=0 cellPadding=0 width="99%" align=center>
				<TBODY>
					<TR>
						<TD>
							<table id="ypxxlist"></table>
						</TD>
					</TR>
				</TBODY>
			</TABLE>			
</form>


</body>
</html>