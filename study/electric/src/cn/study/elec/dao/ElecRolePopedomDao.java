package cn.study.elec.dao;

import java.util.List;

import cn.study.elec.domain.ElecRolePopedom;

public interface ElecRolePopedomDao extends CommonDao<ElecRolePopedom>{
	public static final String IMPL_NAME = "elecRolePopedomDao";

	public List<Object> findPopedomByRoleIDs(String condition);
}
