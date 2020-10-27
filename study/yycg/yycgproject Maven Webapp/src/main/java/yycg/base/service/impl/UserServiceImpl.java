package yycg.base.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import yycg.base.dao.mapper.SysuserMapper;
import yycg.base.dao.mapper.SysuserMapperCustom;
import yycg.base.dao.mapper.UsergysMapper;
import yycg.base.dao.mapper.UserjdMapper;
import yycg.base.dao.mapper.UseryyMapper;
import yycg.base.pojo.po.Sysuser;
import yycg.base.pojo.po.SysuserExample;
import yycg.base.pojo.po.Usergys;
import yycg.base.pojo.po.UsergysExample;
import yycg.base.pojo.po.Userjd;
import yycg.base.pojo.po.UserjdExample;
import yycg.base.pojo.po.Useryy;
import yycg.base.pojo.po.UseryyExample;
import yycg.base.pojo.vo.ActiveUser;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.base.process.context.Config;
import yycg.base.process.result.ResultUtil;
import yycg.base.service.UserService;
import yycg.util.MD5;
import yycg.util.UUIDBuild;

//service类较少，直接写在spring配置文件 applicationContext-base-service.xml 中了
public class UserServiceImpl implements UserService{
	
	//@Autowired根据类型注入，spring框架中可以使用，但是如果不是spring框架，要使用jdk中的@Resource注入
	@Autowired
	private SysuserMapper sysuserMapper;
	
	@Autowired
	UserjdMapper userjdMapper;
	
	@Autowired
	UseryyMapper useryyMapper;
	
	@Autowired
	UsergysMapper usergysMapper;
	
	@Autowired
	private SysuserMapperCustom sysuserMapperCustom;

	@Override
	public List<SysuserCustom> findSysuserList(SysuserQueryVo sysuserQueryVo) throws Exception {
		
		return sysuserMapperCustom.findSysuserList(sysuserQueryVo);
	}

	@Override
	public int findSysuserCount(SysuserQueryVo sysuserQueryVo) throws Exception {
		return sysuserMapperCustom.findSysuserCount(sysuserQueryVo);
	}
	
	@Override
	//根据账号查询用户方法
	public Sysuser findSysuserByUserid(String userId) throws Exception{
		SysuserExample example = new SysuserExample();
		SysuserExample.Criteria criteria = example.createCriteria();
		//设置查询条件，根据账号查询
		criteria.andUseridEqualTo(userId);
		List<Sysuser> list = sysuserMapper.selectByExample(example);
		if(list!=null && list.size()==1){
			return list.get(0);
		}else{
			return null;
		}
	}
	
	//根据单位名称查询单位信息
	@Override
	public Userjd findUserjdByMc(String mc) throws Exception{
		UserjdExample example = new UserjdExample();
		UserjdExample.Criteria criteria = example.createCriteria();
		//设置查询条件，根据账号查询
		criteria.andMcEqualTo(mc);
		List<Userjd> list = userjdMapper.selectByExample(example);
		if(list!=null && list.size()==1){
			return list.get(0);
		}else{
			return null;
		}		
	}
	
	//根据单位名称查询单位信息
	@Override
	public Useryy findUseryyByMc(String mc) throws Exception{
		UseryyExample example = new UseryyExample();
		UseryyExample.Criteria criteria = example.createCriteria();
		//设置查询条件，根据账号查询
		criteria.andMcEqualTo(mc);
		List<Useryy> list = useryyMapper.selectByExample(example);
		if(list!=null && list.size()==1){
			return list.get(0);
		}else{
			return null;
		}		
	}
	
	//根据单位名称查询单位信息
	@Override
	public Usergys findUsergysByMc(String mc) throws Exception{
		UsergysExample example = new UsergysExample();
		UsergysExample.Criteria criteria = example.createCriteria();
		//设置查询条件，根据账号查询
		criteria.andMcEqualTo(mc);
		List<Usergys> list = usergysMapper.selectByExample(example);
		if(list!=null && list.size()==1){
			return list.get(0);
		}else{
			return null;
		}		
	}	
	
	@Override
	public void insertSysuser(SysuserCustom sysuserCustom) throws Exception{
		//参数校验       
		//1 通用合法性校验（如非空校验，长度校验等） 使用工具类
		//2数据业务合法性校验（如账号不能重复，单位必须在单位表中含有等）后台比对
		Sysuser sysuser = this.findSysuserByUserid(sysuserCustom.getUserid());
		if(sysuser!=null){
			//账号重复
			//抛出异常，可预知的异常
			//throw new Exception("账号重复！");
			
			//ResultInfo resultInfo = new ResultInfo();
			//resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
			//resultInfo.setMessage("账号重复！");
			
			//从资源文件中获取错误信息
			//String message = ResourcesUtil.getValue("resources.messages", "213");
			//resultInfo.setMessage(message);
			
			//使用resultUtil构造resultInfo
			
			//throw new ExceptionResultInfo(resultInfo);
			
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 213,null));
		}
		
		//根据用户类型，单位必须存在
		String groupid = sysuserCustom.getGroupid();
		String sysmc = sysuserCustom.getSysmc();
		String sysid = this.findSysidBySysmcAndGroupid(sysmc, groupid);
/*		String sysid = null;
		//监督单位
		if(groupid.equals("1")||groupid.equals("2")){
			 Userjd userjd = this.findUserjdByMc(sysuserCustom.getSysmc());
			 if(userjd==null){
				 //throw new Exception("单位名称输入错误！");
				//ResultInfo resultInfo = new ResultInfo();
				//resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
				//resultInfo.setMessage("单位名称输入错误！");
				//throw new ExceptionResultInfo(resultInfo);
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 209,null));
			 }
			 sysid = userjd.getId();
		}
		//卫生室（医院）
		else if(groupid.equals("3")){
			 Useryy useryy = this.findUseryyByMc(sysuserCustom.getSysmc());
			 if(useryy==null){
				 //throw new Exception("单位名称输入错误！");
				//ResultInfo resultInfo = new ResultInfo();
				//resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
				//resultInfo.setMessage("单位名称输入错误！");
				//throw new ExceptionResultInfo(resultInfo); 
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 210,null));				 
			 }
			 sysid = useryy.getId();
		}	
		//供应商
		else if(groupid.equals("4")){
			 Usergys usergys = this.findUsergysByMc(sysuserCustom.getSysmc());
			 if(usergys==null){
				 //throw new Exception("单位名称输入错误！");
				 //ResultInfo resultInfo = new ResultInfo();
				 //resultInfo.setType(ResultInfo.TYPE_RESULT_FAIL);
				 //resultInfo.setMessage("单位名称输入错误！");
				 //throw new ExceptionResultInfo(resultInfo); 
				 ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 211,null));
			 }
			 //单位id
			 sysid = usergys.getId();
		}*/
		//设置主键
		sysuserCustom.setId(UUIDBuild.getUUID());
		
		//设置单位id
		sysuserCustom.setSysid(sysid);
		 
		//密码加密
		sysuserCustom.setPwd(new MD5().getMD5ofStr(sysuserCustom.getPwd()));
		
		sysuserMapper.insert(sysuserCustom);
	}

	@Override
	public void deleteSysuser(String id) throws Exception {
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		if(sysuser == null){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 212, null));
		}
		
		sysuserMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void updateSysuser(String id, SysuserCustom sysuserCustom) throws Exception {
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		//用户不存在
		if(sysuser == null){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 215, null));
		}
		
		//修改用户账号不允许占用别人的账号
		//如果判断账号修改了
		//页面提交的账号可能是用户修改的账号
		String userid_page = sysuserCustom.getUserid();	
		
		//用户原始账号
		String userid = sysuser.getUserid();
		
		if("".equals(userid_page)||userid_page==null){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 219, null));
		}
		
		if(!userid_page.equals(userid)){
			//通过页面提交的账号查询数据库，如果查到说明暂用别人的账号
			Sysuser sysuser_1 = this.findSysuserByUserid(userid_page);
			if(sysuser_1!=null){
				//说明暂用别人的账号
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 213, null));
			}
		}
		
		//根据页面提交的单位名称查询单位id
		String groupid = sysuserCustom.getGroupid();//用户类型
		String sysmc = sysuserCustom.getSysmc();//页面输入的单位名称
		//单位id
		String sysid = this.findSysidBySysmcAndGroupid(sysmc, groupid);

		
		//密码修改
		//如果从页面提交的密码值为空说明用户不修改密码，否则 就需要对密码进行加密存储
		String pwd_page = sysuserCustom.getPwd().trim();
		String pwd_md5 = null;
		if(pwd_page!=null && !pwd_page.equals("")){
			//说明用户修改密码了
			pwd_md5 = new MD5().getMD5ofStr(pwd_page);
		}	
		
		
		//设置更新用户信息
		
		
		//调用mapper更新用户
		//使用updateByPrimaryKey更新，要先查询用户,否则会清空没有设置值的字段		
		
		Sysuser sysuserUpdate = sysuserMapper.selectByPrimaryKey(id);
		
		sysuserUpdate.setUserid(sysuserCustom.getUserid());
		sysuserUpdate.setUsername(sysuserCustom.getUsername());
		if(pwd_md5!=null){
			sysuserUpdate.setPwd(pwd_md5);
		}		
		sysuserUpdate.setGroupid(sysuserCustom.getGroupid());
		sysuserUpdate.setSysid(sysid);//单位id
		sysuserUpdate.setUserstate(sysuserCustom.getUserstate());
		
		sysuserUpdate.setContact(sysuserCustom.getContact());
		sysuserUpdate.setEmail(sysuserCustom.getEmail());
		sysuserUpdate.setAddr(sysuserCustom.getAddr());
		sysuserUpdate.setPhone(sysuserCustom.getPhone());
		
		sysuserMapper.updateByPrimaryKey(sysuserUpdate);		
		
	}

	@Override
	public SysuserCustom findSysuserById(String id) throws Exception {
		Sysuser sysuser = sysuserMapper.selectByPrimaryKey(id);
		if(sysuser == null){
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 215, null));
		}	
		
		String groupid = sysuser.getGroupid();
		
		String sysid = sysuser.getSysid();
		
		String sysmc = this.findSysmcBySysidAndGroupid(sysid, groupid);
		
		SysuserCustom custom = new SysuserCustom();
		
		//属性拷贝
		BeanUtils.copyProperties(sysuser, custom);		
		//设置单位名称
		custom.setSysmc(sysmc);
		
		return custom;
	}

	@Override
	public ActiveUser checkUserInfo(String userid, String pwd) throws Exception {
		//校验用户是否存在
		Sysuser sysuser = this.findSysuserByUserid(userid);
		if(sysuser == null || (sysuser.getPwd() == null)){
			//用户不存在
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 101, null));
		}
		//校验密码是否正确
		String pwdDb = sysuser.getPwd();	//数据库MD5加密后的密码
		
		String pwbMd5 = new MD5().getMD5ofStr(pwd);
		
		if(!pwdDb.equals(pwbMd5)){
			//密码错误
			ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 114, null));
		}
		
		ActiveUser activeUser = new ActiveUser();
		activeUser.setUserid(userid);
		activeUser.setUsername(sysuser.getUsername());
		activeUser.setGroupid(sysuser.getGroupid());
		activeUser.setSysid(sysuser.getSysid());
		
		String sysid = sysuser.getSysid();
		String groupid = sysuser.getGroupid();
		
		
		String sysmc = this.findSysmcBySysidAndGroupid(sysid, groupid);
		
		activeUser.setSysmc(sysmc);
		
		return activeUser;
	}
	
	
	private String findSysmcBySysidAndGroupid(String sysid,String groupid) throws Exception{
		String sysmc = null;
		//监督单位
		if(groupid.equals("1")||groupid.equals("2")){
			 Userjd userjd = userjdMapper.selectByPrimaryKey(sysid);
			 if(userjd==null){
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 217,null));
			 }
			 sysmc = userjd.getMc();
		}
		//卫生室（医院）
		else if(groupid.equals("3")){
			 Useryy useryy = useryyMapper.selectByPrimaryKey(sysid);
			 if(useryy==null){
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 217,null));				 
			 }
			 sysmc = useryy.getMc();
		}	
		//供应商
		else if(groupid.equals("4")){
			 Usergys usergys = usergysMapper.selectByPrimaryKey(sysid);
			 if(usergys==null){
				 ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 217,null));
			 }
			 //单位id
			 sysmc = usergys.getMc();
		}
		return sysmc;
		
	}
	private String findSysidBySysmcAndGroupid(String sysmc,String groupid) throws Exception{
		
		String sysid = null;//单位id
		
		//监督单位
		if(groupid.equals("1")||groupid.equals("2")){
			 Userjd userjd = this.findUserjdByMc(sysmc);
			 if(userjd==null){
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 209,null));
			 }
			 sysid = userjd.getId();
		}
		//卫生室（医院）
		else if(groupid.equals("3")){
			 Useryy useryy = this.findUseryyByMc(sysmc);
			 if(useryy==null){
				ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 210,null));				 
			 }
			 sysid = useryy.getId();
		}	
		//供应商
		else if(groupid.equals("4")){
			 Usergys usergys = this.findUsergysByMc(sysmc);
			 if(usergys==null){
				 ResultUtil.throwExcepion(ResultUtil.createFail(Config.MESSAGE, 211,null));
			 }
			 //单位id
			 sysid = usergys.getId();
		}		
		
		return sysid;
		
	}
	
}
