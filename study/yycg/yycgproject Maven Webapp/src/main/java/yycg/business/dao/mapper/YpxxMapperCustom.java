package yycg.business.dao.mapper;

import java.util.List;

import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;

public interface YpxxMapperCustom {
	//查询药品列表
	public List<YpxxCustom> findYpxxList(YpxxQueryVo ypxxQueryVo) throws Exception;	

	//查询药品总数
	public int findYpxxCount(YpxxQueryVo ypxxQueryVo) throws Exception;	
	
	//查询BM的最大值，用于增长
	public int findMaxBm() throws Exception;	
}
