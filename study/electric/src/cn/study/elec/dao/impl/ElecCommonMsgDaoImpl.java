package cn.study.elec.dao.impl;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.ElecCommonMsgDao;
import cn.study.elec.domain.ElecCommonMsg;

@Repository(ElecCommonMsgDao.IMPL_NAME)
public class ElecCommonMsgDaoImpl extends CommonDaoImpl<ElecCommonMsg> implements ElecCommonMsgDao{
	
}
