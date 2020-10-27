package yycg.business.pojo.vo;

import yycg.business.pojo.po.Ypxx;

public class YpxxCustom extends Ypxx{
	//开始价格
	private Float priceStart;
	//结束价格
	private Float priceEnd;
	
	//交易状态名称
	private String jyztmc;
	
	public Float getPriceStart() {
		return priceStart;
	}

	public void setPriceStart(Float priceStart) {
		this.priceStart = priceStart;
	}

	public Float getPriceEnd() {
		return priceEnd;
	}

	public void setPriceEnd(Float priceEnd) {
		this.priceEnd = priceEnd;
	}

	public String getJyztmc() {
		return jyztmc;
	}

	public void setJyztmc(String jyztmc) {
		this.jyztmc = jyztmc;
	}
	
}
