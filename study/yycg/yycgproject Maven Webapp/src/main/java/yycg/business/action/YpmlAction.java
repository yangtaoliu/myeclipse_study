package yycg.business.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import yycg.base.pojo.po.Dictinfo;
import yycg.base.pojo.vo.ActiveUser;
import yycg.base.pojo.vo.PageQuery;
import yycg.base.process.context.Config;
import yycg.base.process.result.DataGridResultInfo;
import yycg.base.process.result.ExceptionResultInfo;
import yycg.base.process.result.ResultInfo;
import yycg.base.process.result.ResultUtil;
import yycg.base.process.result.SubmitResultInfo;
import yycg.base.service.SystemConfigService;
import yycg.business.pojo.po.GysypmlControl;
import yycg.business.pojo.vo.GysypmlCustom;
import yycg.business.pojo.vo.GysypmlQueryVo;
import yycg.business.pojo.vo.YpxxCustom;
import yycg.business.service.YpmlService;
import yycg.business.service.YpxxService;

/**
 * 
 * <p>Title: YpmlAction</p>
 * <p>Description: 供货商药品目录</p>
 * <p>Company: www.example.com</p> 
 * @author	insist
 * @date	2020年7月16日下午6:24:00
 * @version 1.0
 */
@Controller
@RequestMapping("/ypml")
public class YpmlAction {
	
	@Autowired
	YpmlService ypmlService;
	
	@Autowired
	SystemConfigService systemConfigService;
	
	//查询页面
	@RequestMapping("/queryGysypml")
	public String queryGysypml(Model model) throws Exception{
		
		//准备数据
		List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");
		List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");
		List<Dictinfo> controllist = systemConfigService.findDictinfoByType("008");
		
		
		model.addAttribute("yplblist", yplblist);
		model.addAttribute("jyztlist", jyztlist);
		model.addAttribute("controllist", controllist);
		
		return "/business/ypml/querygysypml";
	}
	
	@RequestMapping("/queryGysypmlResult")
	//查询列表结果集json
	public @ResponseBody DataGridResultInfo queryGysypmlResult(GysypmlQueryVo gysypmlQueryVo,
									HttpSession session,					
									int page, //DataGrid自动传递的页数
									int rows) //DataGrid自动传递的每页行数
									throws Exception{
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		
		ActiveUser activeUser = (ActiveUser) session.getAttribute(Config.ACTIVEUSER_KEY);
		
		String usergysId = activeUser.getSysid();
		
		//String usergysId = "0dc94edc-08cf-11e3-8a4f-60a44cea4388";
		
		//列表总数
		int total = ypmlService.findGysypmlCount(usergysId, gysypmlQueryVo);
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);		
		
		gysypmlQueryVo.setPageQuery(pageQuery);
		
		//分页查询列表
		List<GysypmlCustom> list = ypmlService.findGysypmlList(usergysId, gysypmlQueryVo);
		
		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setRows(list);
		dataGridResultInfo.setTotal(total);
		return dataGridResultInfo;		
	}
	
	
	//查询页面
	@RequestMapping("/queryAddGysypml")
	public String queryAddGysypml(Model model) throws Exception{
		
		//准备数据
		List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");
		List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");
		List<Dictinfo> controllist = systemConfigService.findDictinfoByType("008");
		
		
		model.addAttribute("yplblist", yplblist);
		model.addAttribute("jyztlist", jyztlist);
		model.addAttribute("controllist", controllist);
		
		return "/business/ypml/querygysypmladd";
	}
	
	@RequestMapping("/queryAddGysypmlResult")
	//查询列表结果集json
	public @ResponseBody DataGridResultInfo queryAddGysypmlResult(GysypmlQueryVo gysypmlQueryVo,
									HttpSession session,					
									int page, //DataGrid自动传递的页数
									int rows) //DataGrid自动传递的每页行数
									throws Exception{
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		
		ActiveUser activeUser = (ActiveUser) session.getAttribute(Config.ACTIVEUSER_KEY);
		
		String usergysId = activeUser.getSysid();
		
		//String usergysId = "0dc94edc-08cf-11e3-8a4f-60a44cea4388";
		
		//列表总数
		int total = ypmlService.findAddGysypmlCount(usergysId, gysypmlQueryVo);
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);		
		
		gysypmlQueryVo.setPageQuery(pageQuery);
		
		//分页查询列表
		List<GysypmlCustom> list = ypmlService.findAddGysypmlList(usergysId, gysypmlQueryVo);
		
		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setRows(list);
		dataGridResultInfo.setTotal(total);
		return dataGridResultInfo;		
	}	
	
	
	@RequestMapping("/addGysypmlSubmit")
	public @ResponseBody SubmitResultInfo addGysypmlSubmit(
								GysypmlQueryVo gysypmlQueryVo,
								HttpSession session,
								int[] indexs
								){
		
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		ActiveUser activeUser = (ActiveUser) session.getAttribute(Config.ACTIVEUSER_KEY);
		//当前用户所属单位 即供货商的id
		String usergysId = activeUser.getSysid();	
		
		
		//页面提交的业务数据（多个），要处理的业务数据，页面中传入的参数
		List<YpxxCustom> list = gysypmlQueryVo.getYpxxCustoms();		
		int sucessCount = 0;
		int failedCount = 0;
		
		List<ResultInfo> errorMsgs = new ArrayList<ResultInfo>();
		
		for(int i = 0; i<indexs.length; i++){
			
			ResultInfo resultInfo = null;
			
			YpxxCustom ypxxCustom = list.get(indexs[i]);
			String ypxxid = ypxxCustom.getId();//页面中传入的参数
			
			try {
				//根据选中行的序号获取要处理的业务数据(单个)
				ypmlService.insertGysypml(usergysId, ypxxid);
			} catch (Exception e) {
				//进行异常解析
				e.printStackTrace();
				if(e instanceof ExceptionResultInfo){
					resultInfo = ((ExceptionResultInfo) e).getResultInfo();
				}else{
					//构造未知错误异常
					resultInfo = ResultUtil.createFail(Config.MESSAGE, 900, null);
				}
			}
			if(resultInfo == null){
				//说明成功
				sucessCount++;
			}else{
				failedCount++;
				//记录失败原因
				errorMsgs.add(resultInfo);
			}
		}
		//提示用户成功数量、失败数量、失败原因
		
		/*ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 907, new Object[]{
				sucessCount,
				failedCount
		}));*/
		
		//改成返回详细信息
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 907, new Object[]{
				sucessCount,
				failedCount
		}), errorMsgs);		
	}
	
	
	@RequestMapping("/deleteGysypmlSubmit")
	public @ResponseBody SubmitResultInfo deleteGysypmlSubmit(
								GysypmlQueryVo gysypmlQueryVo,
								HttpSession session,
								int[] indexs
								) throws Exception{
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		ActiveUser activeUser = (ActiveUser) session.getAttribute(Config.ACTIVEUSER_KEY);
		//当前用户所属单位 即供货商的id
		String usergysId = activeUser.getSysid();	
		
		
		//页面提交的业务数据（多个），要处理的业务数据，页面中传入的参数
		List<YpxxCustom> list = gysypmlQueryVo.getYpxxCustoms();
		for(int i = 0; i<indexs.length; i++){
			YpxxCustom ypxxCustom = list.get(indexs[i]);
			String ypxxId = ypxxCustom.getId();//页面中传入的参数
			ypmlService.deleteGysypml(usergysId, ypxxId);
		}
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 406, null));
	}	
	
	//查询页面
	@RequestMapping("/queryGysypmlControl")
	public String queryGysypmlControl(Model model) throws Exception{
		
		//准备数据
		List<Dictinfo> yplblist = systemConfigService.findDictinfoByType("001");
		List<Dictinfo> jyztlist = systemConfigService.findDictinfoByType("003");
		List<Dictinfo> controllist = systemConfigService.findDictinfoByType("008");
		
		
		model.addAttribute("yplblist", yplblist);
		model.addAttribute("jyztlist", jyztlist);
		model.addAttribute("controllist", controllist);
		
		return "/business/ypml/querygysypmlcontrol";
	}
	
	
	@RequestMapping("/queryGysypmlControlResult")
	//查询列表结果集json
	public @ResponseBody DataGridResultInfo queryGysypmlControlResult(
									GysypmlQueryVo gysypmlQueryVo,				
									int page, //DataGrid自动传递的页数
									int rows) //DataGrid自动传递的每页行数
									throws Exception{
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		
		//列表总数
		int total = ypmlService.findGysypmlControlCount(gysypmlQueryVo);
		PageQuery pageQuery = new PageQuery();
		pageQuery.setPageParams(total, rows, page);		
		
		gysypmlQueryVo.setPageQuery(pageQuery);
		
		//分页查询列表
		List<GysypmlCustom> list = ypmlService.findGysypmlControlList(gysypmlQueryVo);
		
		DataGridResultInfo dataGridResultInfo = new DataGridResultInfo();
		dataGridResultInfo.setRows(list);
		dataGridResultInfo.setTotal(total);
		return dataGridResultInfo;		
	}	
	
	@RequestMapping("/gysypmlControlSubmit")
	public @ResponseBody SubmitResultInfo gysypmlControlSubmit(
								GysypmlQueryVo gysypmlQueryVo,
								int[] indexs
								) throws Exception{
		gysypmlQueryVo = gysypmlQueryVo!=null? gysypmlQueryVo:new GysypmlQueryVo();
		
		
		//页面提交的业务数据（多个），要处理的业务数据，页面中传入的参数
		List<YpxxCustom> list = gysypmlQueryVo.getYpxxCustoms();
		List<GysypmlCustom> gysypmlCustoms = gysypmlQueryVo.getGysypmlCustoms();
		GysypmlControl gysypmlControl = null;
		for(int i = 0; i<indexs.length; i++){
			YpxxCustom ypxxCustom = list.get(indexs[i]);
			GysypmlCustom gysypmlCustom = gysypmlCustoms.get(indexs[i]);
			String ypxxId = ypxxCustom.getId();//页面中传入的参数
			String usergysId = gysypmlCustom.getUsergysid();
			String control = gysypmlCustom.getControl();
			String advice = gysypmlCustom.getAdvice();
			
			gysypmlControl = new GysypmlControl();
			gysypmlControl.setYpxxid(ypxxId);
			gysypmlControl.setUsergysid(usergysId);
			gysypmlControl.setControl(control);
			gysypmlControl.setAdvice(advice);
			
			
			ypmlService.updateGysypmlControl(usergysId, ypxxId, gysypmlControl);
		}
		
		return ResultUtil.createSubmitResult(ResultUtil.createSuccess(Config.MESSAGE, 906, null));
	}	
			
}
