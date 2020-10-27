package cn.study.elec.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.study.elec.dao.IElecSystemDDLDao;
import cn.study.elec.domain.ElecSystemDDL;
import cn.study.elec.service.IElecSystemDDLService;

@Service(IElecSystemDDLService.SERVICE_NAME)
//@Scope(value="prototype")  设置多例  默认单例
@Transactional(readOnly=true)
public class ElecSystemDDLServiceImpl implements IElecSystemDDLService{
	
	/**数据字典的Dao*/
	@Resource(name=IElecSystemDDLDao.IMPL_NAME)
	private IElecSystemDDLDao elecSystemDDLDao;

	/**
	 * 查询数据字典，去掉重复值
	 */
	@Override
	public List<ElecSystemDDL> findSystemDDLListByDistinct() {
		return elecSystemDDLDao.findSystemDDLListByDistinct();
	}

	@Override
	public List<ElecSystemDDL> findSystemDDLListByKeyword(String keyword) {
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(keyword)){
			condition +=" and o.keyword=?";
			paramsList.add(keyword);
		}
		Object[] params = paramsList.toArray();
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.ddlCode", "asc");
		List<ElecSystemDDL> list = elecSystemDDLDao.findCollectionByConditionNoPage(condition, params, orderby);
		System.out.println();
		return list;
	}
	/**
	 * 保存数据字典
	 */
	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)	
	public void saveSystemDDL(ElecSystemDDL elecSystemDDL) {
		//1：获取页面传递的参数
		//数据类型
		String keyword = elecSystemDDL.getKeywordname();
		//业务标识
		String typeflag = elecSystemDDL.getTypeflag();
		//数据项的值（数组）
		String itemnames [] = elecSystemDDL.getItemname();
		//2：获取判断业务逻辑的标识（new和add）
		// 如果typeflag==new：新增一种新的数据类型
		if(typeflag!=null && typeflag.equals("new")){
			//* 遍历页面传递过来的数据项的名称，组织PO对象，执行保存
			this.saveDDL(keyword,itemnames);
		}
		// 如果typeflag==add：在已有的数据类型基础上进行编辑和修改
		else{
			//* 使用数据类型，查询该数据类型对应的list，删除list
			List<ElecSystemDDL> list = this.findSystemDDLListByKeyword(keyword);
			elecSystemDDLDao.deleteObjectByCollection(list);
		    //* 遍历页面传递过来的数据项的名称，组织PO对象，执行保存
			this.saveDDL(keyword, itemnames);
		}		
	}	
	
	//* 遍历页面传递过来的数据项的名称，组织PO对象，执行保存
	private void saveDDL(String keyword, String[] itemnames) {
		if(itemnames!=null && itemnames.length>0){
			for(int i=0;i<itemnames.length;i++){
				//组织PO对象，执行保存
				ElecSystemDDL systemDDL = new ElecSystemDDL();
				systemDDL.setKeyword(keyword);
				systemDDL.setDdlCode(i+1);
				systemDDL.setDdlName(itemnames[i]);
				elecSystemDDLDao.save(systemDDL);
			}
		}
	}

	/**
	 * 使用数据类型和数据项的编号获取数据项的值
	 * @param 	keyword 数据类型
	 * 			ddlCode 数据项的编号
	 * @return	数据项的值
	 */
	public String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode) {
		return elecSystemDDLDao.findDdlNameByKeywordAndDdlCode(keyword, ddlCode);
	}	
	
}
