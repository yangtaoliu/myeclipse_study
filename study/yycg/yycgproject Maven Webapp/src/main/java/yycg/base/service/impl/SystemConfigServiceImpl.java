package yycg.base.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import yycg.base.dao.mapper.BasicinfoMapper;
import yycg.base.dao.mapper.DictinfoMapper;
import yycg.base.pojo.po.Basicinfo;
import yycg.base.pojo.po.BasicinfoExample;
import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.po.DictinfoExample;
import yycg.base.service.SystemConfigService;

public class SystemConfigServiceImpl implements SystemConfigService{

	@Autowired
	DictinfoMapper dictinfoMapper;
	
	@Autowired
	BasicinfoMapper basicinfoMapper;
	
	//根据字典typecode获取字典明细
	@Override
	public List<Dictinfo> findDictinfoByType(String typecode) throws Exception {
		DictinfoExample example = new DictinfoExample();
		DictinfoExample.Criteria criteria =  example.createCriteria();
		criteria.andTypecodeEqualTo(typecode);
		
		return dictinfoMapper.selectByExample(example);
	}

	//根据typecode和dictcode获取单个字段明细
	@Override
	public Dictinfo findDictinfoByDictcode(String typecode, String dictcode) throws Exception {
		DictinfoExample example = new DictinfoExample();
		DictinfoExample.Criteria criteria =  example.createCriteria();
		criteria.andTypecodeEqualTo(typecode);
		criteria.andDictcodeEqualTo(dictcode);
		
		List<Dictinfo> list = dictinfoMapper.selectByExample(example);
		if(list!=null && list.size()==1){
			return list.get(0);
		}
		
		return null;
	}
	
	//根据系统参数id获取系统参数表信息
	@Override
	public Basicinfo findBasicinfoById(String id) throws Exception {
		
		return basicinfoMapper.selectByPrimaryKey(id);
	}
	
	@Override
	public List<Basicinfo> findAllBasicinfo() throws Exception {
		BasicinfoExample basicinfoExample = new BasicinfoExample();
		List<Basicinfo>  list = basicinfoMapper.selectByExample(basicinfoExample);
		return list;
	}
	
	@Override
	public void updateSystemConfig(List<Basicinfo> list) {
		if(list!=null && list.size()>0){
			for(Basicinfo basicinfo:list){
				Basicinfo basicinfoUpdate = basicinfoMapper.selectByPrimaryKey(basicinfo.getId());
				basicinfoUpdate.setValue(basicinfo.getValue());
				basicinfoMapper.updateByPrimaryKey(basicinfoUpdate);
			}
		}
	}
}
