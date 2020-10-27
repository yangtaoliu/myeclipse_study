package cn.study.elec.test;

import java.util.Date;

import org.junit.Test;

import cn.study.elec.domain.ElecText;
import cn.study.elec.service.ElecTextService;

public class TestService extends SpringUtils{
	/**测试保存*/
	@Test
	public void save(){
		ElecTextService elecTextService = (ElecTextService) context.getBean(ElecTextService.SERVICE_NAME);
		//操作对象
		ElecText elecText = new ElecText();
		elecText.setTextName("测试Service名称");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("测试Service备注");
		elecTextService.saveElecText(elecText);
	}

}
