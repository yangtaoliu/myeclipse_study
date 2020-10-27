package cn.study.elec.service;

import cn.study.elec.domain.ElecCommonMsg;

public interface ElecCommonMsgService {
	public static final String SERVICE_NAME = "elecCommonMsgService";
	ElecCommonMsg findCommonMsg();
	void saveCommonMsg(ElecCommonMsg elecCommonMsg);	

}
