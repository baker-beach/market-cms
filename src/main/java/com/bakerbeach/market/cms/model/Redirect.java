package com.bakerbeach.market.cms.model;

public class Redirect {

	private String value;
	private String params;
	private String type = URL_ID;

	public final static String RAW = "RAW";
	public final static String URL_ID = "URL_ID";

	public Redirect(String pageId, String params) {
		this.value = pageId;
		this.params = params;
	}

	public Redirect(String value, String params, String type) {
		this.value = value;
		this.params = params;
		this.type = type;
	}

	public String getPageId() {
		return value;
	}

	public String getParams() {
		return params;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
