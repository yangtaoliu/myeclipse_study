package cn.study.elec.web.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.study.elec.domain.ElecPopedom;
import cn.study.elec.domain.ElecRole;
import cn.study.elec.domain.ElecUser;
import cn.study.elec.service.ElecRoleService;


@SuppressWarnings("serial")
@Controller("elecRoleAction")
@Scope(value="prototype")
public class elecRoleAction extends BaseAction<ElecPopedom>{
	/**
	 * VO(value object) 值对象 
	 * BO(business object) 业务对象 
	 * PO(persistant object) 持久对象 
	 */
	//VO对象
	ElecPopedom elecPopedom = this.getModel();

	//注入角色service
	@Resource(name=ElecRoleService.SERVICE_NAME)
	private ElecRoleService elecRoleService;
	/**
	 * 角色管理首页
	 * @return
	 * 跳转到system/roleIndex.jsp
	 */
	public String home(){
		//查询所有角色
		List<ElecRole> roleList = elecRoleService.findAllRoleList();
		request.setAttribute("roleList", roleList);
		//查询所有的权限
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomList();
		request.setAttribute("popedomList", popedomList);
		return "home";
	}
	/**
	 * 跳转到编辑页面
	 * @return
	 * 跳转到system/roleEdit.jsp
	 */	
	public String edit(){
		String roleID = elecPopedom.getRoleID();
		List<ElecPopedom> popedomList = elecRoleService.findAllPopedomListByRoleID(roleID);
		request.setAttribute("popedomList", popedomList);	
		
		List<ElecUser> userList = elecRoleService.findAllUserByRoleID(roleID);
		request.setAttribute("userList", userList);	
		return "edit";
	}
	/**
	 * 保存角色和权限，角色和用户的关联关系
	 * @return
	 * 重定向到system/roleIndex.jsp
	 */		
	public String save(){
		elecRoleService.saveRole(elecPopedom);
		return "save";
	}
}
