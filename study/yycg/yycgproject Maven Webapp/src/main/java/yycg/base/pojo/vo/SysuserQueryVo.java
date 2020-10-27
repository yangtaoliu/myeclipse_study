package yycg.base.pojo.vo;

/**
 * 
 * <p>Title: SysuserQueryVo</p>
 * <p>Description: 包装类，用于页面向action传递参数，将数据传到mybatis</p>
 * <p>Company: www.example.com</p> 
 * @author	insist
 * @date	2020年6月24日下午3:23:58
 * @version 1.0
 */
public class SysuserQueryVo {
	
	//分页参数
	private PageQuery pageQuery;
	
	//用户查询条件
	private SysuserCustom sysuserCustom;
	
	//...  其它扩展的查询条件
	
	
	public SysuserCustom getSysuserCustom() {
		return sysuserCustom;
	}

	public PageQuery getPageQuery() {
		return pageQuery;
	}

	public void setPageQuery(PageQuery pageQuery) {
		this.pageQuery = pageQuery;
	}

	public void setSysuserCustom(SysuserCustom sysuserCustom) {
		this.sysuserCustom = sysuserCustom;
	}
	
}
