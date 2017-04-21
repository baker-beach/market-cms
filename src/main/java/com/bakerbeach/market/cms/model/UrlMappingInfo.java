package com.bakerbeach.market.cms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class UrlMappingInfo extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	private static final String URLS_KEY = "urls";
	private static final String ACTION_KEY = "action";
	private static final String DATA_KEY = "data";
	private static final String PAGE_ID_KEY = "page_id";
	private static final String URL_ID_KEY = "url_id";
	private static final String LAST_UPDATE_KEY = "last_update";
	
	public UrlMappingInfo() {
		put(LAST_UPDATE_KEY, new Date());
		put(URLS_KEY, new ArrayList<Map<String, String>>());
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Map<String, String>> getUrls() {
		return (ArrayList<Map<String, String>>) get(URLS_KEY);
	}

	public String getAction() {
		return (String) get(ACTION_KEY);
	}

	public void setAction(String action) {
		put(ACTION_KEY, action);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getData(){
		return (Map<String, Object>) get(DATA_KEY);
	}

	public void setData(Map<String,Object> data) {
		put(DATA_KEY, data);
	}

	public String getPageId(){
		return (String) get(PAGE_ID_KEY);
	}

	public void setPageId(String pageId){
		put(PAGE_ID_KEY, pageId);
	}
	
	public String getUrlId(){
		return (String) get(URL_ID_KEY);
	}

	public void setUrlId(String urlId){
		put(URL_ID_KEY, urlId);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public Date getLastUpdate() {
		return (Date) get(LAST_UPDATE_KEY);
	}

}

