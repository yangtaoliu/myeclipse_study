package cn.study.elec.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class LoginUtil {

	public static boolean checkNumber(HttpServletRequest request) {
		//页面中传过来的验证码
		String checkNumber = request.getParameter("checkNumber");
		if(StringUtils.isBlank(checkNumber)){
			return false;
		}
		//session中生成的验证码
		String sessionCheckNumber = (String) request.getSession().getAttribute("CHECK_NUMBER_KEY");
		if(StringUtils.isBlank(sessionCheckNumber)){
			return false;
		}		
		return checkNumber.equalsIgnoreCase(sessionCheckNumber);
	}

	public static void RemenberMe(String name, String password, HttpServletRequest request,
			HttpServletResponse response) {
		//创建2个cookie，存放用户名和密码
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		Cookie nameCookie = new Cookie("name", name);
		Cookie passwordCookie = new Cookie("password", password);
		//System.out.println("nameCookie的默认路径"+ nameCookie.getPath());	//null
		//设置路径
		nameCookie.setPath(request.getContextPath()+"/");
		//System.out.println("nameCookie设置后的路径"+ nameCookie.getPath());	///day48_electric/
		passwordCookie.setPath(request.getContextPath()+"/");			
		//判断页面复选框是否选中
		String rememberMe = request.getParameter("remeberMe");
		if(rememberMe!=null && "yes".equalsIgnoreCase(rememberMe)){
			//设置有效时间
			nameCookie.setMaxAge(7*24*60*60);//一周，单位：秒 ,-1为默认，浏览器关闭时候即删除cookie
			passwordCookie.setMaxAge(7*24*60*60);//一周，单位：秒
			
		}else{
			//清空cookie
			nameCookie.setMaxAge(0);//因为同一个路径 ,不能存在相同的cookie(键相同),通过覆盖的方式,设置有效时间为0. 删除cookie
			passwordCookie.setMaxAge(0);		
		}
		//将cookie存放到response中
		response.addCookie(nameCookie);
		response.addCookie(passwordCookie);

	}
	
}
