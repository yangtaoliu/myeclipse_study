package yycg.business.dao.mapper;

import java.util.List;

import yycg.business.pojo.vo.GysypmlCustom;
import yycg.business.pojo.vo.GysypmlQueryVo;

public interface GysypmlMapperCustom {
	//供货商药品目录查询列表
	public List<GysypmlCustom> findGysypmlList(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	//供货商药品目录查询总数
	public int findGysypmlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception;


	//供货商药品目录添加查询
	public List<GysypmlCustom> findAddGysypmlList(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	//供货商药品目录添加总数
	public int findAddGysypmlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception;

	//监管局控制药品目录查询列表
	public List<GysypmlCustom> findGysypmlControlList(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	//监管局控制药品目录查询总数
	public int findGysypmlControlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	
	
}