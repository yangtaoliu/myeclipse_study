package yycg.base.service;

import java.util.List;

import yycg.base.pojo.po.Basicinfo;
import yycg.base.pojo.po.Dictinfo;

public interface SystemConfigService {
	//根据字典typecode获取字典明细
	public List<Dictinfo> findDictinfoByType(String typecode) throws Exception;
	
	//根据typecode和dictcode获取单个字段明细
	public Dictinfo findDictinfoByDictcode(String typecode,String dictcode) throws Exception;
	
	//根据系统参数id获取系统参数表信息
	public Basicinfo findBasicinfoById(String id)throws Exception;	
	
	public List<Basicinfo> findAllBasicinfo()throws Exception;

	//保存系统配置信息
	public void updateSystemConfig(List<Basicinfo> list);	
}
