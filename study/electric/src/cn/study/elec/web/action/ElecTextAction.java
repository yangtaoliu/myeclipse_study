package cn.study.elec.web.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.study.elec.domain.ElecText;
import cn.study.elec.service.ElecTextService;

/**
 * @Controller("elecTextAction")
 * 相当于spring容器中定义
 * <bean id="elecTextAction" class="com.itheima.elec.web.action.ElecTextAction" scope="prototype">
 */
@Controller("elecTextAction")
@Scope(value="prototype")//设置多例  默认单例
public class ElecTextAction extends BaseAction<ElecText>{
	ElecText elecText = this.getModel();
	@Resource(name=ElecTextService.SERVICE_NAME)
	private ElecTextService elecTextService;
	/**执行保存*/
	public String save(){
		//保存
		elecTextService.saveElecText(elecText);
		return "save";
	}
	
}
