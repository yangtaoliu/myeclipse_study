package yycg.business.service;

import java.util.List;

import yycg.business.pojo.po.GysypmlControl;
import yycg.business.pojo.vo.GysypmlCustom;
import yycg.business.pojo.vo.GysypmlQueryVo;

public interface YpmlService {
	//供货商药品目录查询列表
	/**
	 * 
	 * <p>Title: findGysypmlList</p>
	 * <p>Description: </p>
	 * @param usergysId	供应商id
	 * @param gysypmlQueryVo 查询条件
	 * @return
	 * @throws Exception
	 */
	public List<GysypmlCustom> findGysypmlList(String usergysId,GysypmlQueryVo gysypmlQueryVo) throws Exception;
	//供货商药品目录查询总数
	public int findGysypmlCount(String usergysId,GysypmlQueryVo gysypmlQueryVo) throws Exception;

	public List<GysypmlCustom> findAddGysypmlList(String usergysId,GysypmlQueryVo gysypmlQueryVo) throws Exception;
	//供货商药品目录查询总数
	public int findAddGysypmlCount(String usergysId,GysypmlQueryVo gysypmlQueryVo) throws Exception;
	
	//供应商添加药品信息
	public void insertGysypml(String usergysId,String ypxxId) throws Exception;
	
	//删除供应商药品信息
	public void deleteGysypml(String usergysId,String ypxxId) throws Exception;
	
	public List<GysypmlCustom> findGysypmlControlList(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	//监管局控制供货商药品目录查询总数
	public int findGysypmlControlCount(GysypmlQueryVo gysypmlQueryVo) throws Exception;
	
	//监管局修改供应商状态
	public void updateGysypmlControl(String usergysId,String ypxxId,GysypmlControl gysypmlControl) throws Exception;
	
}
