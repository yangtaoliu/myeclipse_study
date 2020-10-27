package cn.study.elec.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.ElecRolePopedomDao;
import cn.study.elec.domain.ElecRolePopedom;

@Repository(ElecRolePopedomDao.IMPL_NAME)
public class ElecRolePopedomDaoImpl extends CommonDaoImpl<ElecRolePopedom> implements ElecRolePopedomDao{

	@Override
	/**
	 * 
	* @see cn.study.elec.dao.ElecRolePopedomDao#findPopedomByRoleIDs(java.lang.String)  
	* @Function: ElecRolePopedomDaoImpl.java
	* @Description: 使用多个角色id转换的字符串，查询权限
	*
	* @param:condition 存放条件
	* @return：List<Object> 表示权限字符串的集合
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: admin
	* @date: 2020年1月4日 下午4:55:21 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2020年1月4日     admin           v1.0.0               修改原因
	* 使用hql或者sql语句
	*
	* select distinct o.mid from study_elec.elec_role_popedom o where 1=1 and o.roleID  in ('1','2'); 
	 */
	public List<Object> findPopedomByRoleIDs(String condition) {
		String hql = "select distinct o.mid from ElecRolePopedom o where 1=1 and o.roleID  in ("+condition+")";
		List<Object> list = this.getHibernateTemplate().find(hql);
		return list;
	}
	
}
