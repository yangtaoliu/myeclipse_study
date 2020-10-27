package junit;

import org.junit.Test;

import cn.study.elec.domain.xsd.ElecSystemDDL;
import cn.study.elec.webservice.FindSystemByKeyword;
import cn.study.elec.webservice.FindSystemByKeywordResponse;
import cn.study.elec.webservice.IWebSystemDDLServiceSkeleton;

public class TestWebservice {

	/**测试接口的实现类*/
	@Test
	public void testWebService(){
		IWebSystemDDLServiceSkeleton ddlServiceSkeleton = new IWebSystemDDLServiceSkeleton();
		FindSystemByKeyword findSystemByKeyword = new FindSystemByKeyword();
		findSystemByKeyword.setArgs0("性别");
		FindSystemByKeywordResponse byKeywordResponse = ddlServiceSkeleton.findSystemByKeyword(findSystemByKeyword);
		ElecSystemDDL [] ddl = byKeywordResponse.get_return();
		if(ddl!=null && ddl.length>0){
			for(ElecSystemDDL systemDDL:ddl){
				System.out.println(systemDDL.getKeyword()+"   "+systemDDL.getDdlCode()+"   "+systemDDL.getDdlName());
			}
		}
		
	}
	
}
