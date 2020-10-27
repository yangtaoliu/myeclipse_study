package yycg.business.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import yycg.base.pojo.po.Dictinfo;
import yycg.base.process.context.Config;
import yycg.base.process.result.ResultUtil;
import yycg.base.service.SystemConfigService;
import yycg.business.dao.mapper.GysypmlControlMapper;
import yycg.business.dao.mapper.GysypmlMapper;
import yycg.business.dao.mapper.GysypmlMapperCustom;
import yycg.business.dao.mapper.YpxxMapper;
import yycg.business.pojo.po.Gysypml;
import yycg.business.pojo.po.GysypmlControl;
import yycg.business.pojo.po.GysypmlControlExample;
import yycg.business.pojo.po.GysypmlExample;
import yycg.business.pojo.po.Ypxx;
import yycg.business.pojo.vo.GysypmlCustom;
import yycg.business.pojo.vo.GysypmlQueryVo;
import yycg.business.service.YpmlService;
import yycg.util.UUIDBuild;

public class YpmlServiceImpl implements YpmlService{

	@Autowired
	GysypmlMapperCustom gysypmlMapperCustom;
	
	@Autowired
	GysypmlMapper gysypmlMapper;
	
	@Autowired
	GysypmlControlMapper gysypmlControlMapper;
	
	@Autowired
	YpxxMapper ypxxMapper;
	
	@Autowired
	SystemConfigService systemConfigService;
	
	@Override
	public List<GysypmlCustom> findGysypmlList(String usergysId, GysypmlQueryVo gysypmlQueryVo) throws Exception {
		
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		GysypmlCustom gysypmlCustom = gysypmlQueryVo.getGysypmlCustom();
		if(gysypmlCustom == null){
			gysypmlCustom = new GysypmlCustom();
		}
		gysypmlCustom.setUsergysid(usergysId);
		//设置供货商id
		gysypmlQueryVo.setGysypmlCustom(gysypmlCustom);
		
		return gysypmlMapperCustom.findGysypmlList(gysypmlQueryVo);
	}

	@Override
	public int findGysypmlCount(String usergysId, GysypmlQueryVo gysypmlQueryVo) throws Exception {
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		GysypmlCustom gysypmlCustom = gysypmlQueryVo.getGysypmlCustom();
		if(gysypmlCustom == null){
			gysypmlCustom = new GysypmlCustom();
		}
		gysypmlCustom.setUsergysid(usergysId);
		//设置供货商id
		gysypmlQueryVo.setGysypmlCustom(gysypmlCustom);
		
		return gysypmlMapperCustom.findGysypmlCount(gysypmlQueryVo);
	}

	@Override
	public List<GysypmlCustom> findAddGysypmlList(String usergysId, GysypmlQueryVo gysypmlQueryVo) throws Exception {
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		GysypmlCustom gysypmlCustom = gysypmlQueryVo.getGysypmlCustom();
		if(gysypmlCustom == null){
			gysypmlCustom = new GysypmlCustom();
		}
		gysypmlCustom.setUsergysid(usergysId);
		//设置供货商id
		gysypmlQueryVo.setGysypmlCustom(gysypmlCustom);
		
		return gysypmlMapperCustom.findAddGysypmlList(gysypmlQueryVo);
	}

	@Override
	public int findAddGysypmlCount(String usergysId, GysypmlQueryVo gysypmlQueryVo) throws Exception {
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		GysypmlCustom gysypmlCustom = gysypmlQueryVo.getGysypmlCustom();
		if(gysypmlCustom == null){
			gysypmlCustom = new GysypmlCustom();
		}
		gysypmlCustom.setUsergysid(usergysId);
		//设置供货商id
		gysypmlQueryVo.setGysypmlCustom(gysypmlCustom);
		
		return gysypmlMapperCustom.findAddGysypmlCount(gysypmlQueryVo);
	}

	@Override
	public void insertGysypml(String usergysId, String ypxxId) throws Exception {
		//校验不允许已经存在
		Gysypml gysypml = this.findGysypmlByPrimarykeys(usergysId,ypxxId);
		if(gysypml != null){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 401, null));
		}
		
		Ypxx ypxx = ypxxMapper.selectByPrimaryKey(ypxxId);
		if("2".equals(ypxx.getJyzt())){
			// 药品状态为暂停不允许添加
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 403,
					new Object[] { ypxx.getBm(), ypxx.getMc()

					}));
		}
		
		Gysypml record = new Gysypml();
		record.setId(UUIDBuild.getUUID());
		record.setUsergysid(usergysId);
		record.setYpxxid(ypxxId);
		gysypmlMapper.insert(record);
		
		// 同时向供货商药品目录控制表gysypml_control插入一条记录
		// 如果控制表已存在供货商药品不用插入
		// 根据供货商id和药品id查询
		GysypmlControl gysypmlControl = this.findGysypmlControlByPrimarykeys(usergysId,ypxxId);
		if(gysypmlControl==null){
			
			//从系统参数配置表获取控制状态的默认值
			String control = systemConfigService.findBasicinfoById("00101").getValue();
			
			//执行插入
			GysypmlControl gysypmlControl_insert = new GysypmlControl();
			gysypmlControl_insert.setId(UUIDBuild.getUUID());
			gysypmlControl_insert.setUsergysid(usergysId);
			gysypmlControl_insert.setYpxxid(ypxxId);
			gysypmlControl_insert.setControl(control);//控制状态，采用默认值，默认值从系统参数配置表中获取
			
			gysypmlControlMapper.insert(gysypmlControl_insert);
		}		
	}

	private GysypmlControl findGysypmlControlByPrimarykeys(String usergysId, String ypxxId) {
		GysypmlControlExample example = new GysypmlControlExample();
		GysypmlControlExample.Criteria criteria = example.createCriteria();
		criteria.andUsergysidEqualTo(usergysId);
		criteria.andYpxxidEqualTo(ypxxId);
		List<GysypmlControl> list = gysypmlControlMapper.selectByExample(example);
		if(list!=null && list.size()==1){
			return list.get(0);
		}else{
			return null;
		}
	}

	private Gysypml findGysypmlByPrimarykeys(String usergysId, String ypxxId) {
		GysypmlExample example = new GysypmlExample();
		GysypmlExample.Criteria criteria = example.createCriteria();
		criteria.andUsergysidEqualTo(usergysId);
		criteria.andYpxxidEqualTo(ypxxId);
		List<Gysypml> list = gysypmlMapper.selectByExample(example);
		if(list!=null && list.size()==1){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public void deleteGysypml(String usergysId, String ypxxId) throws Exception {
		//校验存在才能删除
		Gysypml gysypml = this.findGysypmlByPrimarykeys(usergysId,ypxxId);
		
		if(gysypml == null){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 402, null));
		}
		
		gysypmlMapper.deleteByPrimaryKey(gysypml.getId());
		
	}

	@Override
	public List<GysypmlCustom> findGysypmlControlList(GysypmlQueryVo gysypmlQueryVo)
			throws Exception {
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		return gysypmlMapperCustom.findGysypmlControlList(gysypmlQueryVo);
	}

	@Override
	public int findGysypmlControlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception {
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		return gysypmlMapperCustom.findGysypmlControlCount(gysypmlQueryVo);
	}

	@Override
	public void updateGysypmlControl(String usergysId, String ypxxId, GysypmlControl gysypmlControl) throws Exception {
		
		GysypmlControl dbGysypmlControl = this.findGysypmlControlByPrimarykeys(usergysId, ypxxId);
		if(dbGysypmlControl == null){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 407, null));
		}
		String control = gysypmlControl.getControl();
		
		if(!"".equals(control)){
			Dictinfo dictinfo =  systemConfigService.findDictinfoByDictcode("008", control);
			
			if(dictinfo == null){
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 408, null));
			}		
		}
		
		String advice = gysypmlControl.getAdvice();
		
		if(control!=null && (!"".equals(control)))
		{
			dbGysypmlControl.setControl(control);
		}

		dbGysypmlControl.setAdvice(advice);
	
		
		gysypmlControlMapper.updateByPrimaryKey(dbGysypmlControl);
		
	}

}
