package yycg.base.pojo.vo;

import yycg.base.pojo.po.Sysuser;

/**
 * 
 * <p>Title: SysuserCustom</p>
 * <p>Description: 扩展类，用于提交信息、查询条件</p>
 * <p>Company: www.example.com</p> 
 * @author	insist
 * @date	2020年6月24日下午3:44:41
 * @version 1.0
 */
public class SysuserCustom extends Sysuser{
	//单位名称
	private String sysmc;
	
	//用户类型对应的名称
	private String groupname;
	
	//用户状态对应的名称
	private String userstateName;
	
	public String getSysmc() {
		return sysmc;
	}

	public void setSysmc(String sysmc) {
		this.sysmc = sysmc;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getUserstateName() {
		return userstateName;
	}

	public void setUserstateName(String userstateName) {
		this.userstateName = userstateName;
	}

}
