package com.study.solr;

public class SolrTest {
}


/*


package cn.study.bean;

import org.apache.solr.client.solrj.beans.Field;

public class Article {
	@Field(value="id")
	private int id;

	@Field(value="title")
	private String title;

	@Field(value="content")
	private String content;

	@Field(value="price")
	private double price;

	@Field(value="name")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", content=" + content + ", price=" + price + "]";
	}


}



package cn.study.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;


public class SolrJ {
	@Test
	public void addIndex() throws Exception{
		String urlString = "http://localhost:8983/solr";

		SolrServer solr = new HttpSolrServer(urlString);

		//第一种添加方式
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "552");
		doc.addField("name", "Gouda cheese wheel");
		doc.addField("xxxxxx_ss", "dynamic");
		UpdateResponse response = solr.add(doc);

		//第二种添加方式
//		Article article = new Article();
//		article.setId(9999);
//		article.setTitle("高富帅");
//		article.setContent("白富美");
//		article.setPrice(19.00);
//		solr.addBean(article);
//
//		solr.commit();


//		List<Article> articleList = new ArrayList<Article>();
//		for(int i=1;i<=25;i++){
//			Article article = new Article();
//			article.setId(i);
//			article.setTitle("标题填在这里");
//			article.setContent("午夜星空");
//			article.setPrice(19.00);
//			article.setName("星空");
//			articleList.add(article);
//		}
//
//
//		solr.addBeans(articleList);

		solr.commit();
	}

	@Test
	public void del() throws Exception{
		String urlString = "http://localhost:8983/solr";

		SolrServer solr = new HttpSolrServer(urlString);

		solr.deleteById("552197");

		List<String> ids = new ArrayList<String>();
		for(int i=1; i<=25; i++){
			ids.add(String.valueOf(i));
		}
		solr.deleteById(ids);
		solr.commit();
	}

	//更新的话如果是id相同，会直接更新，和新增相同

	@Test
	public void find() throws Exception{
		String urlString = "http://localhost:8983/solr";

		SolrServer solr = new HttpSolrServer(urlString);

		SolrQuery params = new SolrQuery();

		params.setQuery("name:星空");

		QueryResponse queryResponse = solr.query(params);

		SolrDocumentList documentList = queryResponse.getResults();
		if(documentList!=null && documentList.size()>0){
			for(SolrDocument document:documentList){
				System.out.println("id:" + document.get("id"));
			}
		}
	}

	@Test
	public void findWithPage() throws Exception{
		String urlString = "http://localhost:8983/solr";

		SolrServer solr = new HttpSolrServer(urlString);

		SolrQuery params = new SolrQuery();

		params.setStart(10);

		params.setRows(10);//默认也是十条

		params.setQuery("name:星空");

		QueryResponse queryResponse = solr.query(params);

		SolrDocumentList documentList = queryResponse.getResults();
		if(documentList!=null && documentList.size()>0){
			for(SolrDocument document:documentList){
				System.out.println("id:" + document.get("id"));
			}
		}
	}

	@Test
	public void findWithHighlight() throws Exception{
		String urlString = "http://localhost:8983/solr";

		SolrServer solr = new HttpSolrServer(urlString);

		SolrQuery params = new SolrQuery();

		params.setQuery("description:星空");

		//开启高亮
		params.setHighlight(true);
		params.setHighlightSimplePre("<font color='red'>");
		params.setHighlightSimplePost("</font>");

		//需要高亮哪些字段	下面2中方法结果一样
		//params.setParam("hl.fl", "description");
		params.addHighlightField("description");

		QueryResponse queryResponse = solr.query(params);

		//返回所有的结果
		SolrDocumentList documentList = queryResponse.getResults();

		//返回高亮之后的结果
		Map<String, Map<String, List<String>>> map = queryResponse.getHighlighting();


		if(documentList!=null && documentList.size()>0){
			for(SolrDocument document:documentList){
				Object id = document.get("id");
				Map<String, List<String>> fieldMap = map.get(id);
				List<String> list = fieldMap.get("description");
				System.out.println(list.toString() );
				System.out.println("长度：" + list.size());
			}
		}
	}
}



*/