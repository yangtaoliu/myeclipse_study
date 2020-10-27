package cn.study.elec.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.apache.struts2.ServletActionContext;

public class ConditionalTagUtil extends SimpleTagSupport{
	private String pattern;
	//用来接收页面指定的参数
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/*
       <s:set value="#session.globle_popedom" var="popedom" scope="request"></s:set>
       <!-- 使用权限控制 -->
       <s:if test="#request.popedom.contains('ec')">
       		<input type="button" name="saveitem" value="保存" style="font-size:12px; color:black; height=20;width=50" onClick="returnMethod()">
       </s:if>
	 */
	@Override
	public void doTag() throws JspException, IOException {
		//从session中获取当前用户权限的字符串
		String popedom = (String) ServletActionContext.getRequest().getSession().getAttribute("globle_popedom");
		//权限匹配成功
		if(popedom.contains(pattern)){
			//显示标签体的内容
			this.getJspBody().invoke(null);
		}
	}
}
