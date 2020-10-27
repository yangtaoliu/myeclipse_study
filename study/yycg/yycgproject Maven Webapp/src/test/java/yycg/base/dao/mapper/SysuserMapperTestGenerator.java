package yycg.base.dao.mapper;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import yycg.base.pojo.po.Sysuser;
import yycg.base.pojo.po.SysuserExample;
import yycg.util.UUIDBuild;

public class SysuserMapperTestGenerator {
	
	private ApplicationContext applicationContext;
	
	private SysuserMapper sysuserMapper;
	
	@Before
	public void before(){
		applicationContext = new ClassPathXmlApplicationContext(new String[]{
			"spring/applicationContext.xml",
			"spring/applicationContext-base-dao.xml"
		});
		sysuserMapper = (SysuserMapper) applicationContext.getBean("sysuserMapper");
	}	
	
	//根据主键删除
	@Test
	public void testDeleteByPrimaryKey() {
		sysuserMapper.deleteByPrimaryKey("8a72fb6787bd45c1aa0a18c96c29cd11");
	}

	@Test
	public void testInsert() {
		Sysuser sysuser = new Sysuser();
		sysuser.setId(UUIDBuild.getUUID());
		sysuser.setPwd("123");
		sysuser.setUserid("111");
		sysuser.setUsername("大狗");
		sysuser.setGroupid("3");
		sysuser.setAddr("测试数据");
		
		sysuserMapper.insertSelective(sysuser);
	}
	
	//根据主键查询
	@Test
	public void testSelectByExample() {
		SysuserExample example = new SysuserExample();
		SysuserExample.Criteria criteria = example.createCriteria();
		
		//自定义查询条件
		criteria.andUsernameEqualTo("aaa").andGroupidEqualTo("3");
		
		List<Sysuser> list = sysuserMapper.selectByExample(example);
		
		System.out.println(list.size());
	}

	@Test
	public void testSelectByPrimaryKey() {
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey("8888");
		
		System.out.println(sysuser.getUsername());
	}

	//根据主键更新，只更新不为空的属性，一般直接定义新的对象，设置需要更新的属性然后更新
	@Test
	public void testUpdateByPrimaryKeySelective() {
		Sysuser sysuser = new Sysuser();
		sysuser.setId("4ab56a3ed3da421eb0eb0ad82f80ec4c");
		sysuser.setPwd("99999");
		sysuser.setUserid("999");
		sysuser.setUsername("大狗很大");
		sysuser.setGroupid("3");
		sysuser.setAddr("测试数据");
		sysuserMapper.updateByPrimaryKeySelective(sysuser);
	}

	//根据主键更新，不管属性是否为空都更新，通常先查询对象，再设置值，再更新
	@Test
	public void testUpdateByPrimaryKey() {
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey("4ab56a3ed3da421eb0eb0ad82f80ec4c");
		sysuser.setUsername("大狗很大很大");
		sysuser.setAddr("测试数据测试数据");
		sysuserMapper.updateByPrimaryKey(sysuser);
	}

}
