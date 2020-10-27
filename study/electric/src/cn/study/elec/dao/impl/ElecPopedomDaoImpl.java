package cn.study.elec.dao.impl;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.ElecPopedomDao;
import cn.study.elec.domain.ElecPopedom;

@Repository(ElecPopedomDao.IMPL_NAME)
public class ElecPopedomDaoImpl extends CommonDaoImpl<ElecPopedom> implements ElecPopedomDao{
	
}
