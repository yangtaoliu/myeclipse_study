package yycg.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import yycg.business.dao.mapper.YpxxMapperCustom;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;
import yycg.business.service.YpxxService;

public class YpxxServiceImpl implements YpxxService{
	@Autowired
	YpxxMapperCustom ypxxMapperCustomer;

	
	@Override
	public List<YpxxCustom> findYpxxList(YpxxQueryVo ypxxQueryVo) throws Exception {
		return ypxxMapperCustomer.findYpxxList(ypxxQueryVo);
	}


	@Override
	public int findYpxxCount(YpxxQueryVo ypxxQueryVo) throws Exception {
		return ypxxMapperCustomer.findYpxxCount(ypxxQueryVo);
	}
	
	
}
