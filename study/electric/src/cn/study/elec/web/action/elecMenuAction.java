package cn.study.elec.web.action;

import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.study.elec.domain.ElecCommonMsg;
import cn.study.elec.domain.ElecPopedom;
import cn.study.elec.domain.ElecRole;
import cn.study.elec.domain.ElecUser;
import cn.study.elec.service.ElecCommonMsgService;
import cn.study.elec.service.ElecRoleService;
import cn.study.elec.service.ElecUserService;
import cn.study.elec.util.LoginUtil;
import cn.study.elec.util.MD5keyBean;
import cn.study.elec.util.ValueUtils;
import cn.study.elec.web.form.MenuForm;


@Controller("elecMenuAction")
@Scope(value="prototype")
public class elecMenuAction extends BaseAction<MenuForm>{
	MenuForm menuForm = this.getModel();

	//注入运行监控service
	@Resource(name=ElecCommonMsgService.SERVICE_NAME)
	private ElecCommonMsgService elecCommonMsgService;
	
	//注入用户service
	@Resource(name=ElecUserService.SERVICE_NAME)
	private ElecUserService elecUserService;

	//注入角色service
	@Resource(name=ElecRoleService.SERVICE_NAME)
	private ElecRoleService elecRoleService;
	
	public String menuHome(){
		//获取用户名和密码
		String name = menuForm.getName();
		String password = menuForm.getPassword();
		/**
		 * 验证验证码是否正确
		 * true 验证成功
		 * false 验证失败
		 */
		boolean flag = LoginUtil.checkNumber(request);
		if(!flag){
			this.addActionError("验证码错误!");
			
			String rememberMe = request.getParameter("remeberMe");
			if(rememberMe!=null && "yes".equalsIgnoreCase(rememberMe)){
				ServletActionContext.getRequest().setAttribute("checked", "checked");
			}			
			
			
			return "loginError";//跳转到登录页面			
		}
		ElecUser elecUser = elecUserService.findUserByLogonName(name);
		if(elecUser==null){
			this.addActionError("用户名输入有误!");
			return "loginError";//跳转到登录页面
		}
		if(StringUtils.isBlank(password)){
			this.addActionError("密码不能为空！");
			return "loginError";//跳转到登录页面
		}else{
			MD5keyBean bean = new MD5keyBean();
			String md5password = bean.getkeyBeanofStr(password);
			if(!md5password.equals(elecUser.getLogonPwd())){
				this.addActionError("密码错误！");
				return "loginError";
			}
		}
		LoginUtil.RemenberMe(name,password,request,response);
		
		//判断用户是否分配了角色，如果分配了角色，将角色的信息存放起来
		/**
		 * key: 角色ID
		 * value: 角色名称
		 */
		Hashtable<String, String> ht = new Hashtable<String, String>();//Hashtable是线程安全的
		Set<ElecRole> elecRoles = elecUser.getElecRoles();
		//当前用户没有分配角色
		if(elecRoles==null || elecRoles.size()==0){
			this.addActionError("当前用户没有分配角色，请与管理员联系！");
			return "loginError";			
		}
		//分配角色的话保存起来
		else{
			for(ElecRole elecRole : elecRoles){
				ht.put(elecRole.getRoleID(), elecRole.getRoleName());
			}
		}
		//判断用户角色是否分配了权限，如果分配了，保存权限pid 如aa@ab@ac
		String popedom = elecRoleService.findPopedomByRoleIDs(ht);
		if(StringUtils.isBlank(popedom)){
			this.addActionError("当前用户具有的角色没有分配权限，请与管理员联系！");
			return "loginError";
		}
		
		//用户名密码都正确将ElecUser对象放入session
		request.getSession().setAttribute("globle_user", elecUser);
		request.getSession().setAttribute("globle_role", ht);
		request.getSession().setAttribute("globle_popedom", popedom);
		return "menuHome";
	}
	/**标题*/
	public String title(){
		return "title";
	}
	/**菜单*/
	public String left(){
		return "left";
	}
	/**框架大小改变*/
	public String change(){
		return "change";
	}
	/**功能区域显示页面
	 * 跳转到menu/loading.jsp
	 */
	public String loading(){
		//查询设备运行情况，放置到浮动框中
		//查询数据
		ElecCommonMsg commomMsg = elecCommonMsgService.findCommonMsg();
		//压入栈顶，回显用
		ValueUtils.putValueStack(commomMsg);		
		return "loading";
	}	
	/**重新登录
	 *	重定向到登录页面，转发不行，转发地址不改变 
	 */
	public String logout(){
		//清空所有session
		request.getSession().invalidate();
		return "logout";
	}
	/**站点运行情况*/
	public String alermStation(){
		//查询数据
		ElecCommonMsg commomMsg = elecCommonMsgService.findCommonMsg();
		//压入栈顶，回显用
		ValueUtils.putValueStack(commomMsg);		
		return "alermStation";
	}
	/**设备运行情况*/
	public String alermDevice(){
		//查询数据
		ElecCommonMsg commomMsg = elecCommonMsgService.findCommonMsg();
		//压入栈顶，回显用
		ValueUtils.putValueStack(commomMsg);		
		return "alermDevice";
	}
	/**
	 * 
	* @Function: elecMenuAction.java
	* @Description: 使用ajax动态加载左侧的树形菜单
	*
	* @param:无
	* @return：String 使用struts2提供的插件包
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: admin
	* @date: 2020年1月5日 下午9:04:06 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2020年1月5日     admin           v1.0.0               修改原因
	 */
	public String showMenu(){
		//获取角色的集合
		Hashtable<String, String> ht = (Hashtable<String, String>) request.getSession().getAttribute("globle_role");
		//当前用户
		ElecUser elecUser = (ElecUser) request.getSession().getAttribute("globle_user");
		
		//1:从Session获取当前用户具有的权限的字符串	如aa@ab@ac
		String popedom = (String) request.getSession().getAttribute("globle_popedom");
		//2:查询当前用户所对应的功能权限List<ElecPopedom>，将List集合转化成json
		List<ElecPopedom> list = elecRoleService.findPopedomListByString(popedom);
		
		//不包含管理员权限不能查看用户，只能编辑自己用户的信息
		if(!ht.containsKey("1")){
			if(list!=null && list.size()>0){
				for(ElecPopedom elecPopedom:list){
					String mid = elecPopedom.getMid();
					String pid = elecPopedom.getPid();
					if("an".equals(mid) && "am".equals(pid)){
						elecPopedom.setUrl("../system/elecUserAction_edit.do?userID="+elecUser.getUserID()+"&roleflag=1");
					}
				}
			}
		}
		
		//3:将List转化成json，只需要将list集合放置到栈顶
		ValueUtils.putValueStack(list);
		return "showMenu";
	}
}
