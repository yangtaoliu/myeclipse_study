package cn.study.elec.dao;

import java.util.List;

import cn.study.elec.domain.ElecSystemDDL;

public interface IElecSystemDDLDao extends CommonDao<ElecSystemDDL> {
	
	public static final String IMPL_NAME = "elecSystemDDLDaoImpl";

	List<ElecSystemDDL> findSystemDDLListByDistinct();

	String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode);

	
	
}
