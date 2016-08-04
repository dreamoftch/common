package com.tch.test.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class SolrUtil {

	private static SolrClient solr = null;

	static {
		String urlString = "http://localhost:8983/solr/db";
		solr = new HttpSolrClient.Builder(urlString).build();
	}

	public static void main(String[] args) throws SolrServerException, IOException {
		query();
//		add();
//		query();
		//delete();
		//query();
	}

	public static void query() throws SolrServerException, IOException {
		SolrQuery query = new SolrQuery();
		query.setQuery("id:99999 AND node:\"solrj added node\" AND parent_nid:99999");
		query.setRows(3);
		query.setSort("id", ORDER.asc);
		QueryResponse response = solr.query(query);
		SolrDocumentList list = response.getResults();
		System.out.println(list.getNumFound());
		list.forEach((solrDocument) -> {
			System.out.println(solrDocument);
		});
	}

	public static void delete() throws SolrServerException, IOException {
		solr.deleteByQuery("*:*");
		solr.commit();
	}

	public static void add() throws SolrServerException, IOException {
		UpdateResponse response = solr.addBean(new Node(99999L, 99999L, 666666L, "solrj added node"));
		/*SolrInputDocument document = new SolrInputDocument();
		document.addField("uid", 666666);
		document.addField("node", "solrj added node");
		document.addField("id", 99999);
		document.addField("parent_nid", 99999);
		UpdateResponse response = solr.add(document);*/
		System.out.println(response);
		solr.commit();
	}
	
	public static class Node{
		@Field
		private Long id;
		@Field
		private Long parent_nid;
		@Field
		private Long uid;
		@Field
		private String node;
		public Node(Long id, Long parent_nid, Long uid, String node) {
			super();
			this.id = id;
			this.parent_nid = parent_nid;
			this.uid = uid;
			this.node = node;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getParent_nid() {
			return parent_nid;
		}
		public void setParent_nid(Long parent_nid) {
			this.parent_nid = parent_nid;
		}
		public Long getUid() {
			return uid;
		}
		public void setUid(Long uid) {
			this.uid = uid;
		}
		public String getNode() {
			return node;
		}
		public void setNode(String node) {
			this.node = node;
		}
	}

}
