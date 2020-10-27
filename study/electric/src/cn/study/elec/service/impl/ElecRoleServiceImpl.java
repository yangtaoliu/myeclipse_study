package cn.study.elec.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.study.elec.dao.ElecPopedomDao;
import cn.study.elec.dao.ElecRoleDao;
import cn.study.elec.dao.ElecRolePopedomDao;
import cn.study.elec.dao.ElecUserDao;
import cn.study.elec.domain.ElecPopedom;
import cn.study.elec.domain.ElecRole;
import cn.study.elec.domain.ElecRolePopedom;
import cn.study.elec.domain.ElecUser;
import cn.study.elec.service.ElecRoleService;
/**
 * @Service
 * 相当于在spring容器中定义：
 * <bean id="elecRoleService" class="cn.study.elec.service.impl.ElecRoleServiceImpl">
 */
@Service(ElecRoleService.SERVICE_NAME)
//@Scope(value="prototype")  设置多例  默认单例
@Transactional(readOnly=true)
public class ElecRoleServiceImpl implements ElecRoleService{
	/** 用户表Dao*/
	@Resource(name=ElecUserDao.IMPL_NAME)
	ElecUserDao elecUserDao;
	
	/** 角色表Dao*/
	@Resource(name=ElecRoleDao.IMPL_NAME)
	ElecRoleDao elecRoleDao;
	
	/** 权限表Dao*/
	@Resource(name=ElecPopedomDao.IMPL_NAME)
	ElecPopedomDao elecPopedomDao;
	
	/** 角色权限表Dao*/
	@Resource(name=ElecRolePopedomDao.IMPL_NAME)
	ElecRolePopedomDao elecRolePopedomDao;
	
	@Override
	/**
	 * 查询系统中所有的角色
	 */
	public List<ElecRole> findAllRoleList() {
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.roleID", "asc");
		List<ElecRole> list = elecRoleDao.findCollectionByConditionNoPage("", null, orderby);
		return list;
	}

	/**
	 * 查询系统中所有的权限(满足父中包含子的集合)
	 */
	public List<ElecPopedom> findAllPopedomList() {
		//查询pid=0的权限	SELECT * FROM elec_popedom o where 1=1 and o.pid='0';
		//组织查询条件
		String condition = " and o.pid = ?";
		Object[] params  = {"0"};
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.mid", "asc");
		List<ElecPopedom> list = elecPopedomDao.findCollectionByConditionNoPage(condition, params, orderby);
		if(list!=null && list.size()>0){
			for(ElecPopedom elecPopedom:list){
				//获取权限id,这个值是子元素对应的pid
				String mid1 = elecPopedom.getMid();
				String condition1 = " and o.pid = ?";
				Object[] params1  = {mid1};
				Map<String, String> orderby1 = new LinkedHashMap<String, String>();
				orderby.put("o.mid", "asc");
				List<ElecPopedom> list1 = elecPopedomDao.findCollectionByConditionNoPage(condition1, params1, orderby1);				
				elecPopedom.setList(list1);
			}
		}
		return list;
	}

	@Override
	public List<ElecPopedom> findAllPopedomListByRoleID(String roleID) {
		List<ElecPopedom> list =  this.findAllPopedomList();
		//使用角色ID，组织查询条件，查询角色权限关联表，返回List<ElecRolePopedom>
		String condition = " and o.roleID = ?";
		Object[] params = {roleID};
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.mid", "asc");
		//查询当前角色具有的权限
		List<ElecRolePopedom> popedomList = elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, orderby);
		//匹配，成功设置flag=1，不成功为2，使用包含技术  List和String有一个函数contains()
		
		//定义一个权限字符串
		StringBuffer popedomBuffer = new StringBuffer();
		//遍历popedomList，将每个mid的值获取，组织一个字符串
		if(popedomList!=null && popedomList.size()>0){
			for(ElecRolePopedom elecRolePopedom :popedomList){
				//获取mid
				String mid = elecRolePopedom.getMid();
				//组织成一个权限字符串(aa@bb@cc)
				popedomBuffer.append(mid).append("@");
			}
			//去掉最后一个@
			popedomBuffer.deleteCharAt(popedomBuffer.length()-1);
		}
		//存放权限，该权限就是当前角色所具有的权限
		String popedom = popedomBuffer.toString();
		//定义一个方法，迭代，完成匹配
		this.findPopedomResult(popedom,list);
		return list;
	}
	//迭代，完成匹配
	private void findPopedomResult(String popedom, List<ElecPopedom> list) {
		//遍历所有的权限，权限中只定义tr的List
		if(list!=null && list.size()>0){
			for (ElecPopedom elecPopedom : list) {
				//获取每个权限的mid
				String mid = elecPopedom.getMid();
				//匹配成功
				if(popedom.contains(mid)){
					elecPopedom.setFlag("1");
				}else{
					elecPopedom.setFlag("2");
				}
				//迭代子里面的权限
				List<ElecPopedom> childList = elecPopedom.getList();
				findPopedomResult(popedom, childList);
			}
		}
	}

	@Override
	public List<ElecUser> findAllUserByRoleID(String roleID) {
		//查询所有的用户
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage("", null, orderby);
		//使用角色id，查询用户角色表，返回ElecRole对象，通过set集合获取用户当前具有的角色
		ElecRole elecRole = elecRoleDao.findObjectByID(roleID);
		Set<ElecUser> elecUsers = elecRole.getElecUsers();
		
		/*方案1*/
		/*
		//遍历elecUsers，组织List<String>,存放ID
		List<String> idList = new ArrayList<String>();
		if(elecUsers!=null && elecUsers.size()>0){
			for (ElecUser elecUser : elecUsers) {
				//用户id
				String userID = elecUser.getUserID();
				idList.add(userID); 
			}
		}
		//遍历所有的用户集合
		if(list!=null && list.size()>0){
			for (ElecUser elecUser : list) {
				if(idList.contains(elecUser.getUserID())){
					//匹配设置标志位1
					elecUser.setFlag("1");
				}else{
					//不匹配设置标志位2
					elecUser.setFlag("2");
				}
			}
		}*/
		/*方案2*/
		if(list!=null && list.size()>0){
			for (ElecUser elecUser : list) {
				//遍历elecUsers
				if(elecUsers!=null && elecUsers.size()>0){
					boolean flag = false;
					for (ElecUser elecUser2 : elecUsers) {
						if(elecUser.getUserID().equals(elecUser2.getUserID())){
							flag = true;
							break;
						}
					}
					if(flag){
						elecUser.setFlag("1");
					}else{
						elecUser.setFlag("2");
					}
				}else{
					elecUser.setFlag("2");
				}
			}
		}		
		return list;
	}

	/**
	 * 保存用户角色关联表
	 * 保存角色权限关联表
	 */
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveRole(ElecPopedom elecPopedom) {
		//获取角色ID
		String roleID = elecPopedom.getRoleID();
		//获取权限主键，格式 pid_mid,如0_aa
		String[] selectopers = elecPopedom.getSelectoper();
		
		String[] selectusers = elecPopedom.getSelectuser();
		
		//一：操作角色权限关联表
		this.saveRolePopedom(roleID,selectopers);
		
		//二：操作用户角色关联表
		this.saveUserRole(roleID,selectusers);

		
	}

	private void saveUserRole(String roleID, String[] selectusers) {
		//1：使用角色ID，查询角色对象ElecRole，获取到该角色对应的用户的Set集合
		ElecRole elecRole = elecRoleDao.findObjectByID(roleID);
		/*方案一*/
		/*Set<ElecUser> elecUsers = elecRole.getElecUsers();		
		//2：解除Set集合的关联关系（删除中间表）
		elecUsers.clear();
		//3：遍历用户ID的数组，重新建立Set集合的关联关系,快照
		if(selectusers!=null && selectusers.length>0){
			for (String userID : selectusers) {
				ElecUser elecUser = new ElecUser();
				elecUser.setUserID(userID);//建立关联关系
				elecUsers.add(elecUser);
			}
		}*/
		/*方案二*/
		Set<ElecUser> elecUsers = new HashSet<ElecUser>();
		if(selectusers!=null && selectusers.length>0){
			for (String userID : selectusers) {
				ElecUser elecUser = new ElecUser();
				elecUser.setUserID(userID);//建立关联关系
				elecUsers.add(elecUser);
			}
		}		
		elecRole.setElecUsers(elecUsers);//list集合会添加，而set集合是直接替换
	}

	private void saveRolePopedom(String roleID, String[] selectopers) {
		//1：使用角色ID，组织查询条件，查询角色权限关联表，返回List<ElecRolePopedom>
		String condition = " and o.roleID = ?";
		Object [] params = {roleID};
		List<ElecRolePopedom> list = elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, null);
		//2：删除之前的数据List
		elecRolePopedomDao.deleteObjectByCollection(list);
		//3：遍历权限主键ID的数组，组织PO对象，执行保存	
		//没用hibernamte创建的表自己管理，自己查，自己删
		if(selectopers!=null && selectopers.length>0){
			//ids的格式：pid_mid 如  0_aa
			for (String ids : selectopers) {
				String [] arrays = ids.split("_");
				//组织PO对象，执行保存
				ElecRolePopedom elecRolePopedom = new ElecRolePopedom();
				elecRolePopedom.setRoleID(roleID);
				elecRolePopedom.setPid(arrays[0]);
				elecRolePopedom.setMid(arrays[1]);
				elecRolePopedomDao.save(elecRolePopedom);
			}
		}
	}

	/**
	 * 
	* @see cn.study.elec.service.ElecRoleService#findPopedomByRoleIDs(java.util.Hashtable)  
	* @Function: ElecRoleServiceImpl.java
	* @Description: 使用角色ID查询角色对应权限并集
	*
	* @param:描述1描述
	* @return：String 存放权限的mid 字符串格式 aa@ab@ac
	* @throws：异常描述
	*
	* @version: v1.0.0
	* @author: admin
	* @date: 2020年1月4日 下午4:20:51 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2020年1月4日     admin           v1.0.0               修改原因
	* 使用hql或者sql语句
	* select distinct o.mid from study_elec.elec_role_popedom o where 1=1 and o.roleID = 1 or o.roleID = 2;
	*
	* select distinct o.mid from study_elec.elec_role_popedom o where 1=1 and o.roleID  in ('1','2');
	* 
	*/
	public String findPopedomByRoleIDs(Hashtable<String, String> ht) {
		//组织查询条件
		StringBuffer bufferCondition = new StringBuffer("");
		
		//遍历hashtable
		if(ht!=null && ht.size()>0){
			for(Iterator<Entry<String, String>> ite = ht.entrySet().iterator();ite.hasNext();){
				Entry<String, String> entry = ite.next();
				bufferCondition.append("'").append(entry.getKey()).append("'").append(",");
			}
			//删除最后一个逗号
			bufferCondition.deleteCharAt(bufferCondition.length()-1);
		}
		String condition = bufferCondition.toString();
		//组织查询
		List<Object> list = elecRolePopedomDao.findPopedomByRoleIDs(condition);
		//组织权限封装的字符串：（字符串格式：aa@ab@ac）
		StringBuffer sb = new StringBuffer();
		if(list!=null && list.size()>0){
			for(int i=0; i<list.size(); i++){
				sb.append(list.get(i).toString()).append("@");
			}
			//删除最后一个@
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}

	/**
	 * 
	* @see cn.study.elec.service.ElecRoleService#findPopedomListByString(java.lang.String)  
	* @Function: ElecRoleServiceImpl.java
	* @Description: 使用权限的字符串查询当前权限（当前用户）所对应的权限集合
	*
	* @param:String :表示权限的字符串，格式 aa@ab@ac
	* @return：List<ElecPopedom>:权限的集合
	* @throws：异常描述
	* SELECT * FROM elec_popedom o WHERE 1=1 AND  o.ismenu = TRUE AND o.MID IN ('aa','ab','ac','ad','ae')
	* @version: v1.0.0
	* @author: admin
	* @date: 2020年1月5日 下午9:10:46 
	*
	* Modification History:
	* Date         Author          Version            Description
	*---------------------------------------------------------*
	* 2020年1月5日     admin           v1.0.0               修改原因
	 */
	public List<ElecPopedom> findPopedomListByString(String popedom) {
		//注意hal语句字段名和javabean中的字段名保持一致
		String condition = " and o.isMenu = ? and o.mid in('"+popedom.replace("@", "','")+"')";
		Object[] params = {true};
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.mid", "asc");//注意条件中的字段一定要和javabean保持一直
		List<ElecPopedom> list = null;
		try {
			list = elecPopedomDao.findCollectionByConditionNoPage(condition, params, orderby);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 使用角色id,mid,pid查询角色权限关联表，判断当前操作是否可以访问action上的方法
	 * @return true 	可以访问  
	 * 		   false 	不能访问
	 */
	public boolean findRolePopedomByID(String roleID, String mid, String pid) {
		StringBuffer conditionBuffer = new StringBuffer();
		List<Object> paramsList = new ArrayList<Object>();
		if(StringUtils.isNotBlank(roleID)){
			conditionBuffer.append(" and o.roleID = ?");
			paramsList.add(roleID);
		}
		if(StringUtils.isNotBlank(mid)){
			conditionBuffer.append(" and o.mid = ?");
			paramsList.add(mid);			
		}
		if(StringUtils.isNotBlank(pid)){
			conditionBuffer.append(" and o.pid = ?");
			paramsList.add(pid);			
		}	
		
		String condition = conditionBuffer.toString();
		Object []params = paramsList.toArray();
		
		List<ElecRolePopedom> elecRolePopedomList = elecRolePopedomDao.findCollectionByConditionNoPage(condition, params, null);
		if(elecRolePopedomList!=null && elecRolePopedomList.size()>0){
			return true;
		}else{
			return false;
		}
	}
	
}
