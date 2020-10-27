package cn.study.elec.service;

import cn.study.elec.domain.ElecText;

public interface ElecTextService {
	public static final String SERVICE_NAME = "elecTextService";
	void saveElecText(ElecText elecText);

}
