package cn.study.elec.service;

import java.util.List;

import cn.study.elec.domain.ElecUser;
import cn.study.elec.domain.ElecUserFile;

public interface ElecUserService {
	public static final String SERVICE_NAME = "elecUserService";
	//public static final String SERVICE_NAME = "cn.study.elec.service.impl.ElecUserServiceImpl";
	

	public List<ElecUser> findUserListByCondition(ElecUser elecUser);

	public String checkUser(String logonName);

	public void saveUser(ElecUser elecUser);

	public ElecUser findUserByID(String userID);

	public ElecUserFile findUserFileByID(String fileID);

	public void deleteUserByID(String userID);

	public ElecUser findUserByLogonName(String name);
}	
