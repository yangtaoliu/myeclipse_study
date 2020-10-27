package cn.study.elec.test;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import cn.study.elec.domain.ElecText;

public class TestHibernate extends SpringUtils{
	/**测试保存*/
	@Test
	public void save(){
/*		Configuration configuration = new Configuration();
		
		configuration.configure("hibernate/hibernate.cfg.xml");//加载类路径hibernate.cfg.xml和映射文件
		SessionFactory sf = configuration.buildSessionFactory();*/
		
		SessionFactory sf = (SessionFactory) context.getBean("sessionFactory");
		
		Session s = sf.openSession();
		Transaction tr = s.beginTransaction();
		
		//测试操作对象的过程，就是操作数据库表
		ElecText elecText = new ElecText();
		elecText.setTextName("测试Hibernate名称");
		elecText.setTextDate(new Date());
		elecText.setTextRemark("测试Hibernate备注");
		s.save(elecText);
		
		tr.commit();
		s.close();
	}

}
