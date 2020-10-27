package cn.study.elec.dao.impl;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.ElecUserDao;
import cn.study.elec.domain.ElecUser;

@Repository(ElecUserDao.IMPL_NAME)
public class ElecUserDaoImpl extends CommonDaoImpl<ElecUser> implements ElecUserDao{
	
}
