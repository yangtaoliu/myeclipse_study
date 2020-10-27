package cn.study.elec.dao.impl;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.IElecCommonMsgContentDao;
import cn.study.elec.domain.ElecCommonMsgContent;


@Repository(IElecCommonMsgContentDao.IMPL_NAME)
public class ElecCommonMsgContentDaoImpl  extends CommonDaoImpl<ElecCommonMsgContent> implements IElecCommonMsgContentDao {

}
