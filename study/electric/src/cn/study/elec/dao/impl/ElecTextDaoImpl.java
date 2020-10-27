package cn.study.elec.dao.impl;

import org.springframework.stereotype.Repository;

import cn.study.elec.dao.ElecTextDao;
import cn.study.elec.domain.ElecText;
/**
 * @Repository(IElecTextDao.SERVICE_NAME)
 * 相当于在spring容器中定义：
 * <bean id="elecTextDao" class="cn.study.elec.dao.impl.ElecTextDaoImpl">
 *
 */
@Repository(ElecTextDao.SERVICE_NAME)
public class ElecTextDaoImpl extends CommonDaoImpl<ElecText> implements ElecTextDao{

}
