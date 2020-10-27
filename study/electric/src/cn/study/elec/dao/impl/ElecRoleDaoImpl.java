package cn.study.elec.dao.impl;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.ElecRoleDao;
import cn.study.elec.domain.ElecRole;

@Repository(ElecRoleDao.IMPL_NAME)
public class ElecRoleDaoImpl extends CommonDaoImpl<ElecRole> implements ElecRoleDao{
	
}
