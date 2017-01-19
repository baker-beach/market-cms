package com.bakerbeach.market.cms.service;

import java.util.List;

import com.bakerbeach.market.cms.model.UrlMappingInfo;

public interface UrlMappingDao {
	
	String getRedirectUrl(String redirect, String shopId, String language);
	
	String getPageUrl(String pageId, String shopId, String language);
	
	List<UrlMappingInfo> getWildcardMappings();
	
	UrlMappingInfo getRequestMappingByUrl(String url, String shopId, String language);
	
	List<UrlMappingInfo> geFilterUrls();

}
