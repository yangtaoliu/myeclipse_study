package yycg.business.service;

import java.util.List;

import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.pojo.vo.YpxxQueryVo;

public interface YpxxService {
	public List<YpxxCustom> findYpxxList(YpxxQueryVo ypxxQueryVo) throws Exception;	

	//药品列表总数
	public int findYpxxCount(YpxxQueryVo ypxxQueryVo) throws Exception;
}
