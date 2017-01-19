package com.bakerbeach.market.cms.service;

import java.util.List;
import java.util.Map;

import com.bakerbeach.market.cms.model.CmsContext;
import com.bakerbeach.market.cms.model.UrlMappingInfo;

public interface UrlService {
	
	String getPageUrl(String pageId, String shopId, String language);

	UrlMappingInfo getRequestMapping(String url, Map<String, String[]> parameterMap, CmsContext cmsContext);

	List<UrlMappingInfo> getFilterUrls();

}
