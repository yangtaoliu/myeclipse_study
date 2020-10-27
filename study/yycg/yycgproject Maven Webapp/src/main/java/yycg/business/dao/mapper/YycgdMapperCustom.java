package yycg.business.dao.mapper;

import java.util.List;

import yycg.business.pojo.vo.YycgdQueryVo;
import yycg.business.pojo.vo.YycgdmxCustom;

public interface YycgdMapperCustom {

	
	//采购单明细查询
	public List<YycgdmxCustom> findYycgdmxList(YycgdQueryVo yycgdQueryVo) throws Exception;
	
	public int findYycgdmxCount(YycgdQueryVo yycgdQueryVo) throws Exception;
	
	//采购药品添加查询
	public List<YycgdmxCustom> findAddYycgdmxList(YycgdQueryVo yycgdQueryVo) throws Exception;
	
	public int findAddYycgdmxCount(YycgdQueryVo yycgdQueryVo) throws Exception;	
}
