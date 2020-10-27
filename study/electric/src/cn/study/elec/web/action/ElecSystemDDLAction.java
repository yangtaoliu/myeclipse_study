package cn.study.elec.web.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.study.elec.domain.ElecSystemDDL;
import cn.study.elec.service.IElecSystemDDLService;
import cn.study.elec.util.AnnotationLimit;



@SuppressWarnings("serial")
@Controller("elecSystemDDLAction")
@Scope(value="prototype")//设置多例  默认单例
public class ElecSystemDDLAction extends BaseAction<ElecSystemDDL>{
	
	ElecSystemDDL elecSystemDDL = this.getModel();
	
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	private IElecSystemDDLService elecSystemDDLService;
	/**
	 * 
	 * @return
	 * 跳转到 system/dictionaryIndex.jsp
	 */
	@AnnotationLimit(mid="aq",pid="am")
	public String home(){
		//查询数据库中已有的数据类型，返回list集合，遍历到页面的下拉菜单中
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByDistinct();
		request.setAttribute("list", list);
		return "home";
	}
	/**
	 * 
	 * @return
	 * 跳转到system/dictionaryEdit.jsp
	 */
	public String edit(){
		//获取数据类型
		String keyword = elecSystemDDL.getKeyword();
		//以类型作为条件，查询数据字典，返回List<ElecSystemDDL>
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByKeyword(keyword);
		request.setAttribute("list", list);
		return "edit";
	}
	/**
	 * 保存数据字典
	 * @return
	 */
	public String save(){
		elecSystemDDLService.saveSystemDDL(elecSystemDDL);
		return "save";
	}
	
}
