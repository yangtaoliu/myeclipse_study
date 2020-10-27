package cn.study.elec.web.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.study.elec.domain.ElecCommonMsg;
import cn.study.elec.service.ElecCommonMsgService;
import cn.study.elec.util.ValueUtils;


@SuppressWarnings("serial")
@Controller("elecCommonMsgAction")
@Scope(value="prototype")//设置多例  默认单例
public class ElecCommonMsgAction extends BaseAction<ElecCommonMsg>{
	
	ElecCommonMsg elecCommonMsg = this.getModel();
	
	@Resource(name=ElecCommonMsgService.SERVICE_NAME)
	private ElecCommonMsgService elecCommonMsgService;
	/**运行监控的首页
	 *	system/actingIndex.jsp 
	 */
	public String home(){
		//查询数据
		ElecCommonMsg commomMsg = elecCommonMsgService.findCommonMsg();
/*		if(commomMsg == null){
			commomMsg = new ElecCommonMsg();
		}*/
		//压入栈顶，回显用
		ValueUtils.putValueStack(commomMsg);
		
		return "home";
	}
	
	/**
	 *保存监控对象 
	 */
	public String save(){
		//模拟保存150次，方便计算百分比
/*		for(int i=1;i<=150;i++){
			elecCommonMsgService.saveCommonMsg(elecCommonMsg);
			request.getSession().setAttribute("percent", (double)i/150*100);//存放计算的百分比
		}
		//线程结束时，清空当前session
		request.getSession().removeAttribute("percent");*/		
		
		elecCommonMsgService.saveCommonMsg(elecCommonMsg);
		
		return "save";
	}	
	
	public String actingView(){
		//查询运行监控的数据
		ElecCommonMsg commomMsg = elecCommonMsgService.findCommonMsg();
		//压入栈顶，回显用
		ValueUtils.putValueStack(commomMsg);		
		return "actingView";
	}
	
	/**
	 * 百分比进度条,使用ajax计算执行的百分比情况，将结果显示到页面上
	 * @return 不需要跳转页面
	 * @throws IOException 
	 */
	public String progressBar() throws IOException{
		//从session中获取操作方法中计算的百分比
		Double percent = (Double) request.getSession().getAttribute("percent");
		String res = "";
		//此时说明操作的业务方法仍然继续在执行
		if(percent!=null){
			//计算的小数，四舍五入取整
			int percentInt = (int) Math.rint(percent); 
			res = "<percent>" + percentInt + "</percent>";
		}
		//此时说明操作的业务方法已经执行完毕，session中的值已经被清空
		else{
			//存放百分比
			res = "<percent>" + 100 + "</percent>";
		}
		//定义ajax的返回结果是XML的形式
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");//默认的是html		
		response.setHeader("Cache-Control", "no-cache");
		//存放结果数据，例如：<response><percent>88</percent></response>
		out.println("<response>");
		out.println(res);
		out.println("</response>");
		out.close();
		return null;
	}	
}
