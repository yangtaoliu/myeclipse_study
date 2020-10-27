package yycg.base.process.exception;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
//import org.springframework.web.servlet.HandlerInterceptor; //拦截器  进入action方法前中后期都有单独的方法
import org.springframework.web.servlet.ModelAndView;

import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultInfo;

public class ExceptionResolverCustom implements HandlerExceptionResolver{

	//json转换器，将异常信息转成json
	private HttpMessageConverter<ExceptionResultInfo> jsonMessageConverter;
	
	//前端控制器调用此方法执行异常处理
	//handler 执行的action类就包括了一个方法（对应url的方法）
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		//输入异常信息
		ex.printStackTrace();
		
		//转成springmvc底层对象（就是对action方法的封装对象，只有一个方法，因为springmvc是面向方法开发的，每个方法的request和response等都不同）
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		//取出方法
		Method method = handlerMethod.getMethod();
		//boolean isAnnotationPresent = method.isAnnotationPresent(ResponseBody.class);
		//判断方法上面是否有ResponseBody注解，有的话那么方法返回的是json
		ResponseBody responseBody = AnnotationUtils.findAnnotation(method, ResponseBody.class);
		if(responseBody!=null){
			//将异常信息转json输出
			return resolveJsonException(request,response,handler,ex);
		}
		//到这里说明返回的是jsp页面
		//解析异常
		ExceptionResultInfo exceptionResultInfo = resolveExceptionCustomer(ex);
		
		int messageCode = exceptionResultInfo.getResultInfo().getMessageCode();
		
		String view = "/base/error";
		
		if(106==messageCode){	//此操作需要登录后进行
			//跳转到登录
			view = "/base/login";
		}
		//将异常信息在error页面显示
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exceptionResultInfo", exceptionResultInfo.getResultInfo());
		//设置逻辑视图名
		modelAndView.setViewName(view);
		
		return modelAndView;
	}

	private ModelAndView resolveJsonException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		ExceptionResultInfo exceptionResultInfo = resolveExceptionCustomer(ex);
		HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);
		try {
			//将exceptionResultInfo对象转成json输出
			jsonMessageConverter.write(exceptionResultInfo, MediaType.APPLICATION_JSON, outputMessage);
		} catch (HttpMessageNotWritableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ModelAndView();
	}

	private ExceptionResultInfo resolveExceptionCustomer(Exception ex) {
		
		ResultInfo info = new ResultInfo();
		
		if(ex instanceof ExceptionResultInfo){
			info = ((ExceptionResultInfo)ex).getResultInfo();
		}else{
			info.setType(ResultInfo.TYPE_RESULT_FAIL);
			info.setMessage("未知错误！");
		}
		return new ExceptionResultInfo(info);
	}
	
	//将异常信息转json输出
	
	
	public HttpMessageConverter<ExceptionResultInfo> getJsonMessageConverter() {
		return jsonMessageConverter;
	}

	public void setJsonMessageConverter(HttpMessageConverter<ExceptionResultInfo> jsonMessageConverter) {
		this.jsonMessageConverter = jsonMessageConverter;
	}

	
}
