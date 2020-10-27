package cn.study.elec.test;

import java.util.Date;

import org.junit.Test;

import cn.study.elec.dao.ElecTextDao;
import cn.study.elec.domain.ElecText;

public class TestDao extends SpringUtils{
	/**测试保存*/
	@Test
	public void save(){
        //调用接口
		ElecTextDao elecTextDao = (ElecTextDao) context.getBean(ElecTextDao.SERVICE_NAME);
		//操作对象
		ElecText elecText = new ElecText();
		elecText.setTextName("测试Dao名称");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("测试Dao备注");
		elecTextDao.save(elecText);
	}

}
