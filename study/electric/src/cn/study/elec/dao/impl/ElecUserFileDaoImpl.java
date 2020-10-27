package cn.study.elec.dao.impl;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.ElecUserFileDao;
import cn.study.elec.domain.ElecUserFile;

@Repository(ElecUserFileDao.IMPL_NAME)
public class ElecUserFileDaoImpl extends CommonDaoImpl<ElecUserFile> implements ElecUserFileDao{
	
}
