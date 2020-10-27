package yycg.base.action;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yycg.base.pojo.vo.ActiveUser;
import yycg.base.process.context.Config;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.UserService;

/**
 * 
 * <p>Title: LoginAction</p>
 * <p>Description: 用户认证</p>
 * <p>Company: www.example.com</p> 
 * @author	insist
 * @date	2020年7月3日下午3:31:31
 * @version 1.0
 */
@Controller
public class LoginAction {
	
	@Autowired
	UserService userService;
	
	
	//用户登录页面
	@RequestMapping("/login")
	public String login() throws Exception{
		//获取数据传递到页面
		return "/base/login";
	}
	
	//用户登录提交
	@RequestMapping("/loginsubmit")
	public @ResponseBody SubmitResultInfo loginsubmit(HttpSession session, String userid,String pwd,String validateCode) throws Exception{
		//验证码
		String validateCodeSession = (String) session.getAttribute("validateCode");
		if(validateCodeSession!=null && !validateCodeSession.equals(validateCode)){
			// TODO 验证码验证   开发过程暂停使用
			//验证码错误
			//ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 113, null));
		}
		//用户认证
		ActiveUser activeUser = userService.checkUserInfo(userid, pwd);
		
		//用户信息放入session
		session.setAttribute(Config.ACTIVEUSER_KEY, activeUser);
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 107, new Object[]{activeUser.getUsername()}));
	}
	
	//退出
	@RequestMapping("/logout")
	public String logout(HttpSession session)throws Exception{
		//session过期
		session.invalidate();
		return "redirect:login.action";
	}	
}
