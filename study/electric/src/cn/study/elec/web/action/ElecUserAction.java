package cn.study.elec.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.study.elec.domain.ElecSystemDDL;
import cn.study.elec.domain.ElecUser;
import cn.study.elec.domain.ElecUserFile;
import cn.study.elec.service.ElecUserService;
import cn.study.elec.service.IElecSystemDDLService;
import cn.study.elec.util.AnnotationLimit;
import cn.study.elec.util.ValueUtils;



@SuppressWarnings("serial")
@Controller("elecUserAction")
@Scope(value="prototype")//设置多例  默认单例
public class ElecUserAction extends BaseAction<ElecUser>{
	ElecUser elecUser = this.getModel();
	/**
	 *	注入用户service
	 */
	@Resource(name=ElecUserService.SERVICE_NAME)
	ElecUserService elecUserService;
	/**
	 *	注入数据字典service
	 */
	@Resource(name=IElecSystemDDLService.SERVICE_NAME)
	private IElecSystemDDLService elecSystemDDLService;	
	
	/**
	 * 
	 * @return
	 * 跳转到 system/userIndex.jsp
	 */
	@AnnotationLimit(mid="an",pid="am")
	public String home(){
		//1：加载数据类型是所属单位的数据字典的集合，遍历在页面的下拉菜单中
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		//2：组织页面中传递的查询条件，查询用户表，返回List<ElecUser>
		List<ElecUser> userList = elecUserService.findUserListByCondition(elecUser);
		request.setAttribute("userList", userList);	
		return "home";
	}
	/**
	 * 跳转到用户管理新增页面
	 * @return
	 * 跳转到	userAdd.jsp
	 */
	public  String add(){
		//加载数据字典，用来遍历性别，职位，所属单位，是否在职
		this.initSystemDDL();
		return "add";
	}
	private void initSystemDDL() {
		List<ElecSystemDDL> sexList = elecSystemDDLService.findSystemDDLListByKeyword("性别");
		request.setAttribute("sexList", sexList);
		List<ElecSystemDDL> postList = elecSystemDDLService.findSystemDDLListByKeyword("职位");
		request.setAttribute("postList", postList);
		List<ElecSystemDDL> jctList = elecSystemDDLService.findSystemDDLListByKeyword("所属单位");
		request.setAttribute("jctList", jctList);
		List<ElecSystemDDL> isDutyList = elecSystemDDLService.findSystemDDLListByKeyword("是否在职");
		request.setAttribute("isDutyList", isDutyList);
	}
	/**
	 * 使用jquery的ajax完成二级联动，使用所属单位，关联单位名称
	 * @return
	 */
	public String findJctUnit(){
		String jctID = elecUser.getJctID();
		List<ElecSystemDDL> list = elecSystemDDLService.findSystemDDLListByKeyword(jctID);
		ValueUtils.putValueStack(list);//放入栈顶后，使用struts的json插件包将会自动转换，list转为json数组，object对象转成josn对象
		return "findJctUnit";
	}
	/**
	 * 使用jquery的ajax完成登录名的后台校验，判断登录名是否存在
	 * @return
	 */	
	public String checkUser(){
		//获取登录名
		String logonName = elecUser.getLogonName();
		//判断是否出现重复，返回messgae 1:为空  2存在  3不存在
		String message = elecUserService.checkUser(logonName);
		elecUser.setMessage(message);
		//ValueUtils.putValueStack(message);//栈顶对象是String类型的属性
		ValueUtils.putValueStack(elecUser);//栈顶对象是elecUser对象
		return "checkUser";
	}
	/**
	 * 保存用户
	 * @return：跳转到close.jsp
	 */
	public String save(){

		elecUserService.saveUser(elecUser);
		//获取标识
		String roleflag = elecUser.getRoleflag();
		//如果不是管理员，此时保存，重定向到编辑页面
		if(roleflag!=null && "1".equals(roleflag)){
			return "redirectEdit";
		}
		return "close";
	}
	/**
	 * 跳转到编辑页面
	 * @return：跳转到userEdit.jsp
	 */	
	public String edit(){
		String userID = elecUser.getUserID();
		//将对象放入栈顶，回显用
		ElecUser user = elecUserService.findUserByID(userID);
		//将vo对象的属性值，放置到po对象的属性值
		user.setViewflag(elecUser.getViewflag());
		// rolefalg==null 用来判断系统管理员操作编辑，此时保存：close.jsp 
		// rolefalg==1 如果不是系统管理员，此时保存：重定向到编辑页面
		user.setRoleflag(elecUser.getRoleflag());
		ValueUtils.putValueStack(user);
		//加载数据字典，用来遍历性别，职位，所属单位，是否在职
		this.initSystemDDL();
		//获取所属单位的编号
		String ddlCode = user.getJctID();
		//使用所属单位和数据项的编号获取数据项的值
		String ddlName = elecSystemDDLService.findDdlNameByKeywordAndDdlCode("所属单位",ddlCode);
		//使用查询的数据项的值作为数据类型查询，查询该数据类型的对应的集合，返回一个List<ElecSystemDDL>
		List<ElecSystemDDL> jctUnitList = elecSystemDDLService.findSystemDDLListByKeyword(ddlName);
		request.setAttribute("jctUnitList", jctUnitList);
		return "edit";
	}
	/**
	 * 文件下载(普通方式)
	 * @return
	 */
/*	public String download(){
		try {
			//获取要下载文件的id
			String fileID = elecUser.getFileID();
			//查询用户文件表，获取文件路径
			ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
			String path = ServletActionContext.getServletContext().getRealPath("") + elecUserFile.getFileURL();
			//告知客户端下载方式
			//可以在tomcat服务器目录下conf下的web.xml查看对应的mime-type
			//或者使用方法getServletContext().getMimeType(".txt")自动查找对应的mime-type类型
			String mimeType = ServletActionContext.getServletContext().getMimeType(elecUserFile.getFileName().substring(elecUserFile.getFileName().lastIndexOf(".")));
			//System.out.println(mimeType);
			response.setContentType(mimeType);
			//推荐的文件名使用URLEncoder编码一下，可以避免中文问题出现的乱码，但是火狐浏览器不支持
			String fileName = elecUserFile.getFileName();
			fileName = new String(fileName.getBytes("gbk"), "iso8859-1");
			//response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(elecUserFile.getFileName(), "UTF-8"));	火狐不支持		
			//这种方式适用于所有浏览器
			response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			InputStream in = new FileInputStream(new File(path));
			OutputStream out = response.getOutputStream();
			//IOUtils.copy(in, out);  //工具中有直接复制
			int len = -1;
			byte[] b = new byte[1024];
			while((len=in.read(b))!=-1){
				out.write(b, 0, len);
			}
			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}*/
	/**
	 * 文件下载(struts方式)
	 * @return
	 */	
	public String download(){
		try {
			//获取要下载文件的id
			String fileID = elecUser.getFileID();
			//查询用户文件表，获取文件路径
			ElecUserFile elecUserFile = elecUserService.findUserFileByID(fileID);
			String path = ServletActionContext.getServletContext().getRealPath("") + elecUserFile.getFileURL();
			//文件名称
			String fileName = elecUserFile.getFileName();
			fileName = new String(fileName.getBytes("gbk"), "iso8859-1");
			request.setAttribute("filename", fileName);//struts配置文件中保持一致
			InputStream in = new FileInputStream(new File(path));
			
			//与栈顶的inputStream关联
			elecUser.setInputStream(in);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "download";
	}
	/**
	 * 删除用户信息，包括附件
	 * @return
	 * 重定向到  	system/userIndex.jsp
	 */
	public String delete(){
		elecUserService.deleteUserByID(elecUser.getUserID());
		return "delete";
	}
}
