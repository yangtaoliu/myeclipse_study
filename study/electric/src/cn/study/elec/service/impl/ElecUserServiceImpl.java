package cn.study.elec.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.study.elec.dao.ElecUserDao;
import cn.study.elec.dao.ElecUserFileDao;
import cn.study.elec.dao.IElecSystemDDLDao;
import cn.study.elec.domain.ElecRole;
import cn.study.elec.domain.ElecUser;
import cn.study.elec.domain.ElecUserFile;
import cn.study.elec.service.ElecUserService;
import cn.study.elec.util.FileUtils;
import cn.study.elec.util.MD5keyBean;

@Service(ElecUserService.SERVICE_NAME)
//@Scope(value="prototype")  设置多例  默认单例
@Transactional(readOnly=true)
public class ElecUserServiceImpl implements ElecUserService{
	/**用户表Dao*/
	@Resource(name=ElecUserDao.IMPL_NAME)
	private ElecUserDao elecUserDao;
	
	/**用户附件表Dao*/
	@Resource(name=ElecUserFileDao.IMPL_NAME)
	ElecUserFileDao elecUserFileDao;
	
	/**数据字典表Dao*/
	@Resource(name=IElecSystemDDLDao.IMPL_NAME)
	IElecSystemDDLDao elecSystemDDLDao;
	
	/**  
	* @Name: findUserListByCondition
	* @Description: 组织查询条件，查询用户列表
	* @Author: 
	* @Version: V1.00 （版本号）
	* @Create Date: 2019-12-25（创建日期）
	* @Parameters: ElecUser:VO对象
	* @Return: List<ElecUser>：用户集合
	*/
	@Override
	public List<ElecUser> findUserListByCondition(ElecUser elecUser) {
		//组织查询条件
		String condition = "";
		List<Object> paramsList = new ArrayList<Object>();
		//用户名称
		String userName = elecUser.getUserName();
		if(StringUtils.isNotBlank(userName)){
			condition += " and o.userName like ?";
			paramsList.add("%"+userName+"%");
		}
		//所属单位
		String jctID = elecUser.getJctID();
		if(StringUtils.isNotBlank(jctID)){
			condition += " and o.jctID = ?";
			paramsList.add(jctID);
		}
		//入职开始时间
		Date onDutyDateBegin = elecUser.getOnDutyDateBegin();
		if(onDutyDateBegin!=null){
			condition += " and o.onDutyDate >= ?";
			paramsList.add(onDutyDateBegin);
		}
		//入职结束时间
		Date onDutyDateEnd = elecUser.getOnDutyDateEnd();
		if(onDutyDateEnd!=null){
			condition += " and o.onDutyDate <= ?";
			paramsList.add(onDutyDateEnd);
		}
		Object [] params = paramsList.toArray();
		//排序（按照入职时间的升序排列）
		Map<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("o.onDutyDate", "asc");
		List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage(condition, params, orderby);
		/**
		 * 3：数据字典的转换
		 	* 使用数据类型和数据项的编号，查询数据字典，获取数据项的值
		 */
		this.convertSystemDDL(list);
		return list;
	}
	
	private void convertSystemDDL(List<ElecUser> list) {
		if(list!=null && list.size()>0){
			for(ElecUser user:list){
				//性别
				String sexID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("性别",user.getSexID());
				user.setSexID(sexID);
				//职位
				String postID = elecSystemDDLDao.findDdlNameByKeywordAndDdlCode("职位",user.getPostID());
				user.setPostID(postID);
			}
		}
	}
	/**
	 * 验证登录名是否存在
	 * 返回 1：为空
	 * 	  2：存在
	 * 	  3：不存在
	 */
	public String checkUser(String logonName) {
		String message = "";
		if(StringUtils.isNotBlank(logonName)){
			String condition = "and o.logonName = ?";
			Object[] params = {logonName};
			List<ElecUser> list = elecUserDao.findCollectionByConditionNoPage(condition, params, null);
			if(list!=null && list.size()>0){
				//存在
				message = "2";
			}else{
				//不存在
				message = "3";
			}			
		}else{
			message = "1";
		}
		return message;
	}
	/**
	 * 保存用户信息
	 * @param:elecUser VO对象
	 */
	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void saveUser(ElecUser elecUser) {
		this.saveUserFiles(elecUser);
		//密码用md5加密
		this.md5password(elecUser);
		//获取页面的userID
		String userID = elecUser.getUserID();
		if(StringUtils.isNotBlank(userID)){
			//组织PO对象，执行更新
			elecUserDao.update(elecUser);
		}else{
			//组织PO对象，保存用户（单条）
			elecUserDao.save(elecUser);
		}
	}
	/**
	 * md5密码加密
	 * @param elecUser
	 * 如果没有设置密码，需要设置初始密码为123
	 */
	private void md5password(ElecUser elecUser) {
		//获取加密前的密码
		String logonPwd = elecUser.getLogonPwd();
		String md5password = "";
		if(StringUtils.isBlank(logonPwd)){
			logonPwd = "123";
		}
		String oldPassword = elecUser.getPassword();
		//编辑的时候没有修改密码的时候不需要加密,加判断可以避免新增用户的时候，oldPassword为空调用equals方法出现空指针异常
		if(oldPassword!=null && oldPassword.equals(logonPwd)){
			md5password = logonPwd;
		}
		//新增的时候或者编辑时候修改了密码才需要加密
		else{
			//使用md5加密
			MD5keyBean md5keyBean = new MD5keyBean();
			md5password = md5keyBean.getkeyBeanofStr(logonPwd);			
		}
		elecUser.setLogonPwd(md5password);
	}

	//遍历多个附件，组织附件的PO对象，完成文件上传，保存用户的附件（多条），简历附件表和用户表的关联关系
	private void saveUserFiles(ElecUser elecUser) {
		//上传时间，写在循环外面，可以保证同时上传的文件一致
		Date progressTime = new Date();
		//获取上传的文件
		File[] uploads = elecUser.getUploads();
		//获取上传的文件名
		String[] fileNames = elecUser.getUploadsFileName();
		//获取上传的文件类型
		String[] contentTypes = elecUser.getUploadsContentType();
		//遍历
		if(uploads!=null && uploads.length>0){
			for(int i=0;i<uploads.length;i++){
				//组织附件的持久化对象(PO对象)
				ElecUserFile elecUserFile = new ElecUserFile();
				elecUserFile.setFileName(fileNames[i]);
				elecUserFile.setProgressTime(progressTime);
				//将文件上传，同时返回路径 path
				String fileURL = FileUtils.fileUploadReturnPath(uploads[i],fileNames[i],"用户管理");
				elecUserFile.setFileURL(fileURL);//上传路径（保存，应用与下载）
				elecUserFile.setElecUser(elecUser);//与用户建立关联关系(注意参数一定要为持久化对象)，否则外键为null
				elecUserFileDao.save(elecUserFile);
			}
		}
	}
	@Override
	/**
	 * 使用用户ID查询对象
	 */
	public ElecUser findUserByID(String userID) {
		return elecUserDao.findObjectByID(userID);
	}
	
	@Override
	/**
	 * 使用附件ID查询附件
	 */	
	public ElecUserFile findUserFileByID(String fileID) {
		return elecUserFileDao.findObjectByID(fileID);
	}
	
	/**
	 * 删除用户和附件
	 * @param userID 多个，用", "隔开
	 */
	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED,readOnly=false)
	public void deleteUserByID(String userID) {
		String [] userIds = userID.split(", ");
		
		if(userIds!=null && userIds.length>0){
			//获取每个用户ID
			for(String uid : userIds){
				//使用用户id查询用户对象获取当前用户具有的附件
				ElecUser elecUser = elecUserDao.findObjectByID(uid);
				Set<ElecUserFile> elecUserFiles = elecUser.getElecUserFiles();
				//遍历用户附件
				if(elecUserFiles!=null && elecUserFiles.size()>0){
					for(ElecUserFile elecUserFile : elecUserFiles){
						//删除用户对应的文件
						//获取路径
						String path = ServletActionContext.getServletContext().getRealPath("") + elecUserFile.getFileURL();
						File file = new File(path);
						if(file.exists()){
							//删除附件
							file.delete();
						}
						//删除每个用户的附件
						//删除该用户对应的用户附件表的数据（用级联删除即可，不用在这里删除）
						//elecUserFileDao.deleteObjectByIDs(elecUserFile.getFileID());
						//<set name="elecUserFiles" table="Elec_User_File" inverse="true" order-by="progressTime desc" cascade="delete">
					}
				}
				/**2020-01-03更新，同时删除用户角色关联的数据，begin*/
				//错误，因为在ElecUser.hbm.xml中设置了inverse="true"，用户不能维护关系，所以删除失败（角色是主控方）
				//elecUser.getElecRoles().clear();
				
				//正确的
				Set<ElecRole> elecRoles = elecUser.getElecRoles();
				if(elecRoles!=null && elecRoles.size()>0){
					for (ElecRole elecRole : elecRoles) {
						//方案一,错误，会导致角色关联的所有用户被删除(用户不会删除，但是关联关系会被删除)
						//elecRole.getElecUsers().clear();
						//方案二
						elecRole.getElecUsers().remove(elecUser);
					}
				}
				/**2020-01-03更新，同时删除用户角色关联的数据，end*/
			}
		}
		//删除附件表的数据
		//删除用户信息表
		elecUserDao.deleteObjectByIDs(userIds);
	}

	/**
	 * 使用登录名作为查询条件查询登录名对用的用户信息
	 */
	public ElecUser findUserByLogonName(String name) {
		ElecUser elecUser = null;
		if(StringUtils.isNotBlank(name)){
			String condition = " and o.logonName = ?";
			Object [] params = {name};
			List<ElecUser> elecUsers = elecUserDao.findCollectionByConditionNoPage(condition, params, null);
			if(elecUsers!=null && elecUsers.size()>0){
				elecUser = elecUsers.get(0);
			}
		}
		return elecUser;
	}

	
	
	
}
