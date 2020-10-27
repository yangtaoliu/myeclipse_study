package cn.study.elec.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.study.elec.domain.ElecUser;

public class SystemUtils implements Filter{
	
	//登录之前，有些链接没有session，需查找到统一控制,相当于过滤器中不包含这些方法和链接
	List<String> list = new ArrayList<String>();
	
	/*web容器初始化*/
	public void init(FilterConfig arg0) throws ServletException {
		//没有session但需要放行
		list.add("/index.jsp");
		list.add("/image.jsp");
		list.add("/system/elecMenuAction_menuHome.do");
		//5秒倒计时链接
		list.add("/error.jsp");
		list.add("/system/elecMenuAction_logout.do");
	}


	/*跳转之前先完成过滤器的方法 */
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
        HttpServletRequest  request;
        HttpServletResponse response;
        //HttpServlet类 中有转换的方法
        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) res;
        } catch (ClassCastException e) {
            throw new ServletException("non-HTTP request or response");
        }		
        //获取访问的Servlet路径
        String path = request.getServletPath();//request.getContextPath()是项目路径，getServletPath是项目后面的路径
        //System.out.println("Servlet路径:" + path);	//  /index.jsp
        
        //在跳转index.jsp之前，先获取cookie,传值的方式给index.jsp
        this.initIndexPage(request,path);
        
        
        //添加粗颗粒度权限控制	只对请求拦截，不对转发拦截
        
        //没有session需要放行的链接
        if(list.contains(path)){
        	chain.doFilter(request, response);
        	return;
        }
        
        ElecUser elecUser = (ElecUser) request.getSession().getAttribute("globle_user");
        //存在用户
        if(elecUser!=null){
        	 //放行
            chain.doFilter(request, response);
            return;
        }
        /*
          	转发用的路径是服务端路径		"/": 相对于项目  	http://localhost:8080/day48_electric/
        */
        //request.getRequestDispatcher("/index.jsp").forward(request, response);转发,此次使用不合理
        
        /*
      		重定向是给浏览器的路径(客户端路径) "/": 相对于 主机	http://localhost:8080/
        */        
        //如果session中不存在用户，重定向到index.jsp
        //response.sendRedirect(request.getContextPath() + "/index.jsp");//重定向
        
        response.sendRedirect(request.getContextPath() + "/error.jsp");//重定向
        
        
        //System.out.println(request.getContextPath());	项目路径	/day48_electric
        //System.out.println(request.getServletContext().getContextPath());	/day48_electric
        
        //System.out.println(request.getServletContext().getRealPath("/"));
        //D:\a_install\apache-tomcat-7.0.96\webapps\day48_electric\
        
        //System.out.println("Servlet路径:" + request.getServletPath());	/index.jsp	getServletPath是项目后面的路径
        
	}

	/*销毁*/
	public void destroy() {
		
	}	
	
	private void initIndexPage(HttpServletRequest request, String path) {
		if(path!=null && "/index.jsp".equals(path)){
			//获取页面中存放的用户名和密码
			String name = "";
			String password = "";
			String checked = "";
			//从Cookie中获取登录名和密码
			Cookie[] cookies = request.getCookies();
			if(cookies!=null && cookies.length>0){
				for(Cookie cookie : cookies){
					if("name".equals(cookie.getName())){
						//用户名,中文解码，在LoginUtil中编码过
						name = cookie.getValue();
						try {
							name = URLDecoder.decode(name, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						checked = "checked";
					}
					if("password".equals(cookie.getName())){
						//密码
						password = cookie.getValue();
					}			
				}
			}	
			//存放到request中

			request.setAttribute("name", name);
			request.setAttribute("password", password);
			request.setAttribute("checked", checked);
		}
	}

	
}

