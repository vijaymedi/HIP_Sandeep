package com.iig.gcp.lineage.lineagecontroller.dto;

public class FeedDetailDTO {
	private String feed_id;
	private String feed_name;
	private String project_id;
	private String src_conn;
	private String tgt_conn;

	public String getFeed_id() {
		return feed_id;
	}

	public void setFeed_id(String feed_id) {
		this.feed_id = feed_id;
	}

	public String getFeed_name() {
		return feed_name;
	}

	public void setFeed_name(String feed_name) {
		this.feed_name = feed_name;
	}

	public String getProject_id() {
		return project_id;
	}

	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	public String getSrc_conn() {
		return src_conn;
	}

	public void setSrc_conn(String src_conn) {
		this.src_conn = src_conn;
	}

	public String getTgt_conn() {
		return tgt_conn;
	}

	public void setTgt_conn(String tgt_conn) {
		this.tgt_conn = tgt_conn;
	}

}