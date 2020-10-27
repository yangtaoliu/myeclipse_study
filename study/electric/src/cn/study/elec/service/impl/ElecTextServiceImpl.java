package cn.study.elec.service.impl;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.study.elec.dao.ElecTextDao;
import cn.study.elec.domain.ElecText;
import cn.study.elec.service.ElecTextService;
/**
 * @Service
 * 相当于在spring容器中定义：
 * <bean id="elecTextService" class="cn.study.elec.service.impl.ElecTextServiceImpl">
 */
@Service(ElecTextService.SERVICE_NAME)
//@Scope(value="prototype")  设置多例  默认单例
@Transactional(readOnly=true)
public class ElecTextServiceImpl implements ElecTextService{
	@Resource(name=ElecTextDao.SERVICE_NAME)
	private ElecTextDao elecTextDao;
	
	public ElecTextDao getElecTextDao() {
		return elecTextDao;
	}

	public void setElecTextDao(ElecTextDao elecTextDao) {
		this.elecTextDao = elecTextDao;
	}
	/**保存测试表*/
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveElecText(ElecText elecText) {
		elecTextDao.save(elecText);
	}

}
