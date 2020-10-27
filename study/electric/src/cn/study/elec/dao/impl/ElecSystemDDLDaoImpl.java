package cn.study.elec.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.study.elec.dao.IElecSystemDDLDao;
import cn.study.elec.domain.ElecSystemDDL;


@Repository(IElecSystemDDLDao.IMPL_NAME)
public class ElecSystemDDLDaoImpl  extends CommonDaoImpl<ElecSystemDDL> implements IElecSystemDDLDao {
	// hql语句中如果查询的是一个字段，返回的是object  如果是多个，返回的是数组
	@Override
	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		List<ElecSystemDDL> systemList = new ArrayList<ElecSystemDDL>();
/*		String hql = "select distinct o.keyword from ElecSystemDDL o";
		List<Object> list = this.getHibernateTemplate().find(hql);
		//组织页面返回的的结果
		if(list!=null && list.size()>0){
			for (Object o : list) {
				ElecSystemDDL elecSystemDDL = new ElecSystemDDL();
				//数据类型
				elecSystemDDL.setKeyword(o.toString());
				systemList.add(elecSystemDDL);
			}
		}*/
		/**使用hql语句直接将投影查询的字段放置到对象中*/
		String hql = "select distinct new cn.study.elec.domain.ElecSystemDDL(o.keyword) from ElecSystemDDL o";
		systemList = this.getHibernateTemplate().find(hql);
		return systemList;
	}
	
	/**  
	* @Name: findDdlNameByKeywordAndDdlCode
	* @Description: 使用数据类型和数据项的编号，获取数据项的值
	* @Author: 
	* @Version: V1.00 （版本号）
	* @Create Date: 2019-12-25（创建日期）
	* @Parameters: String keywrod,数据类型
	* 			   String ddlCode：数据项的编号
	* @Return: String：数据项的值
	*/
	@Override
	public String findDdlNameByKeywordAndDdlCode(final String keyword, final String ddlCode) {
		final String hql = "select o.ddlName from ElecSystemDDL o where o.keyword=? and o.ddlCode=?";
		List<Object> list = this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setParameter(0, keyword);
				query.setParameter(1, Integer.parseInt(ddlCode));//数据字典中ddlCode为Integer
				return query.list();
			}
			
		});
		//返回数据项的值
		String ddlName = "";
		if(list!=null && list.size()>0){
			Object o = list.get(0);
			ddlName = o.toString();
		}
		return ddlName;
	}
	
}
