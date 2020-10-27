 package yycg.base.dao.mapper;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import yycg.base.pojo.po.Sysuser;

/*
	使用@RunWith和@ContextConfiguration配合，可以自动初始化spring容易，直接把需要的东西注入即可
	
	
	继承TestCase就可以不用在方法上面写test就可以进行junit测试
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/applicationContext*.xml"})
public class SysuserMapperTest {
	
	@Autowired
	SysuserMapper sysuserMapper;
	
/*	private ApplicationContext applicationContext;
	
	@Before
	public void before(){
		applicationContext = new ClassPathXmlApplicationContext(new String[]{
			"spring/applicationContext.xml",
			"spring/applicationContext-base-dao.xml"
		});
	}*/
	
	@After
	public void after(){
		//System.out.println("---------结束" + this.getClass() + "测试---------");
	}	
	
	@Test
	public void testFindSysUserById() throws Exception {
		//SysuserMapper sysuserMapper = (SysuserMapper) applicationContext.getBean("sysuserMapper");
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey("100");
		System.out.println(sysuser);
	}

}
