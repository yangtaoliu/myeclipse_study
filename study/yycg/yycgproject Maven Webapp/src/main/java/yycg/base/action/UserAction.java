package yycg.base.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yycg.base.pojo.po.Basicinfo;
import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.vo.PageQuery;
import yycg.base.pojo.vo.SysuserCustom;
import yycg.base.pojo.vo.SysuserQueryVo;
import yycg.base.process.context.Config;
import yycg.base.process.result.DataGridResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.SystemConfigService;
import yycg.base.service.UserService;

/**
 * 
 * <p>Title: UserAction</p>
 * <p>Description: 系统用户管理</p>
 * <p>Company: www.example.com</p> 
 * @author	insist
 * @date	2020年6月24日下午4:02:30
 * @version 1.0
 */
@Controller
@RequestMapping("/user")
public class UserAction {
	
	@Autowired
	UserService userService;
	
	@Autowired
	SystemConfigService systemConfigService;
	
	//系统参数配置
	@RequestMapping("/systemConfig")
	public String systemConfig(Model model) throws Exception{
		//准备数据
		List<Basicinfo> basicinfoList = systemConfigService.findAllBasicinfo();
		
		model.addAttribute("basicinfoList", basicinfoList);
		
		return "/base/user/systemConfig";
	}
	//系统参数配置提交
	@RequestMapping("/systemConfigSubmit")
	public @ResponseBody SubmitResultInfo systemConfigSubmit(Model model,String[] id,String[] value) throws Exception{
		
		List<Basicinfo> list = new ArrayList<Basicinfo>();
		Basicinfo basicinfo = null;
		for(int i=0; i<id.length; i++){
			basicinfo = new Basicinfo();
			basicinfo.setId(id[i]);
			basicinfo.setValue(value[i]);
			list.add(basicinfo);
		}
		
		System.out.println(list.size());
		
		systemConfigService.updateSystemConfig(list);
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 906, null));
	}
	
	
	//用户查询页面
	@RequestMapping("/queryUser")
	public String queryUser(Model model) throws Exception{
		//将页面需要的数据取出，传到页面
		List<Dictinfo> groupList = systemConfigService.findDictinfoByType("s01");
		List<Dictinfo> userstateList = systemConfigService.findDictinfoByType("s02");
		
		model.addAttribute("groupList", groupList);
		model.addAttribute("userstateList", userstateList);
		
		return "/base/user/queryUser";
	}
	
	//用户查询页面的结果集
	//最终DataGridResultInfo通过@ResponseBody注解转成json格式
	//java对象的属性，转成json变成key,List集合转json后，变成数组
	@RequestMapping("/queryUserResult")
	public @ResponseBody DataGridResultInfo queryUserResult(SysuserQueryVo sysuserQueryVo, 
														int page, //DataGrid自动传递的页数
														int rows) //DataGrid自动传递的每页行数
														throws Exception{
		
		sysuserQueryVo = sysuserQueryVo!=null? sysuserQueryVo:new SysuserQueryVo();
		
		int total = userService.findSysuserCount(sysuserQueryVo);
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);		
		
		sysuserQueryVo.setPageQuery(pageQuery);
		
		List<SysuserCustom> list = userService.findSysuserList(sysuserQueryVo);
		
		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setRows(list);
		dataGridResultInfo.setTotal(total);
		return dataGridResultInfo;
	}
	
	//进入添加页面
	@RequestMapping("/addsysuser")	
	public String addsysuser(Model model){
		return "/base/user/addsysuser";
	}
	
	//添加保存
	//提交的结果转成json输出到页面
	//提交的数据统一使用包装类
	@RequestMapping("/addsysuserSubmit")	
	public @ResponseBody SubmitResultInfo addsysuserSubmit(Model model,SysuserQueryVo sysuserQueryVo) throws Exception{
		
		//String message = "操作成功！！！";
		
		//int type = 0;//成功

		//ResultInfo info = new ResultInfo();
		//info.setType(ResultInfo.TYPE_RESULT_SUCCESS);
		//info.setMessage("操作成功");
		
		
/*		//调用service执行添加
		try {
			userService.insertSysuser(sysuserQueryVo.getSysuserCustom());
		} catch (Exception e) {
			//输出异常信息
			e.printStackTrace();
			//异常信息解析
			//message = e.getMessage();
			//type = 1; //失败
			if(e instanceof ExceptionResultInfo){
				info = ((ExceptionResultInfo)e).getResultInfo();
			}else{
				info.setType(ResultInfo.TYPE_RESULT_FAIL);
				info.setMessage("未知错误！");
			}
		}*/
		
		//Map<String, Object> result = new HashMap<String, Object>();
		//result.put("type", type);
		//result.put("message", message);
		
		
		//使用全局异常处理器，不用捕获，全部抛出
		userService.insertSysuser(sysuserQueryVo.getSysuserCustom());
		
		//SubmitResultInfo result = new SubmitResultInfo(info);
		
		//将提交的结果返回页面
		//return result;
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 906, null));
	}	
	
	//删除用户
	@RequestMapping("/deleteSysuser")	
	public @ResponseBody SubmitResultInfo  deleteSysuser(String id) throws Exception{
		
		userService.deleteSysuser(id);
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 218, null));
	}
	
	//进入修改页面
	@RequestMapping("/editsysuser")	
	public String editsysuser(Model model,String id) throws Exception{
		SysuserCustom sysuserCustom = userService.findSysuserById(id);
		model.addAttribute("sysuserCustom", sysuserCustom);
		return "/base/user/editsysuser";
	}
	
	//修改用户
	@RequestMapping("/editsysusersubmit")	
	public @ResponseBody SubmitResultInfo  editsysusersubmit(String id,SysuserQueryVo sysuserQueryVo) throws Exception{
		
		userService.updateSysuser(id, sysuserQueryVo.getSysuserCustom());
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 906, null));
	}	
	
}
