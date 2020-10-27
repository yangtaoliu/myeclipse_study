package cn.study.elec.test;

import org.junit.Test;

import cn.study.elec.service.ElecTextService;
import cn.study.elec.web.action.ElecTextAction;


public class TestAction extends SpringUtils{
	@Test
	public void testPrivilegeAction(){
		ElecTextAction elecTextAction1 = (ElecTextAction) context.getBean("elecTextAction");
		ElecTextAction elecTextAction2 = (ElecTextAction) context.getBean("elecTextAction");
		System.out.println("--Action--" + (elecTextAction1==elecTextAction2));
				
		ElecTextService elecTextService1 = (ElecTextService) context.getBean(ElecTextService.SERVICE_NAME);
		ElecTextService elecTextService2 = (ElecTextService) context.getBean(ElecTextService.SERVICE_NAME);
		System.out.println("--Service--" + (elecTextService1==elecTextService2));
	}
}
