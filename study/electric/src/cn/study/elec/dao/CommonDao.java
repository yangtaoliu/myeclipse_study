package cn.study.elec.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface CommonDao<T> {
	void save(T entity);
	void update(T entity);
	T findObjectByID(Serializable id);
	void deleteObjectByIDs(Serializable... ids);
	void deleteObjectByCollection(List<T> list);
	List<T> findCollectionByConditionNoPage(String condition,
			final Object[] params, Map<String, String> orderby);	
	
}
