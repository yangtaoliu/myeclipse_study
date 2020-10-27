package cn.study.elec.service;

import java.util.List;

import cn.study.elec.domain.ElecSystemDDL;

public interface IElecSystemDDLService {
	public static final String SERVICE_NAME = "elecSystemDDLService";

	public List<ElecSystemDDL> findSystemDDLListByDistinct();

	public List<ElecSystemDDL> findSystemDDLListByKeyword(String keyword);

	public void saveSystemDDL(ElecSystemDDL elecSystemDDL);

	public String findDdlNameByKeywordAndDdlCode(String keyword, String ddlCode);
	
}
