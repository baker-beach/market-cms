package com.bakerbeach.market.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bakerbeach.market.cms.model.CmsContext;
import com.bakerbeach.market.cms.model.UrlMappingInfo;

public class UrlServiceImpl implements UrlService {

	private UrlMappingDao urlMappingDao;
	private Map<String, Pattern> urlWildcardPatterns;

	public UrlMappingDao getUrlMappingDao() {
		return urlMappingDao;
	}

	public void setUrlMappingDao(UrlMappingDao urlmappingDao) {
		this.urlMappingDao = urlmappingDao;
	}

	@Override
	public String getPageUrl(String urlId, String shopId, String language) {
		return urlMappingDao.getPageUrl(urlId, shopId, language);
	}

	public void init() {

		List<UrlMappingInfo> wildcardMappings = urlMappingDao.getWildcardMappings();

		Map<String, String> tempMap = new HashMap<String, String>();

		for (UrlMappingInfo urlMappingInfo : wildcardMappings) {
			ArrayList<Map<String, String>> urls = urlMappingInfo.getUrls();
			for (Map<String, String> entry : urls) {
				if (tempMap.containsKey(entry.get("shop_code") + entry.get("lang"))) {
					String pattern = tempMap.get(entry.get("shop_code") + entry.get("lang"));
					tempMap.put(entry.get("shop_code") + entry.get("lang"), pattern + "|^" + entry.get("value"));
				} else {
					tempMap.put(entry.get("shop_code") + entry.get("lang"), "^" + entry.get("value"));
				}
			}
		}
		urlWildcardPatterns = new HashMap<String, Pattern>();
		for (String key : tempMap.keySet()) {
			urlWildcardPatterns.put(key, Pattern.compile(tempMap.get(key)));
		}
	}

	@Override
	public UrlMappingInfo getRequestMapping(String url, Map<String, String[]> parameterMap, CmsContext cmsContext) {
		String searchUrl = url;

		UrlMappingInfo urlMappingInfo = urlMappingDao.getRequestMappingByUrl(searchUrl, cmsContext.getAppCode(),
				cmsContext.getCurrentLocale().getLanguage());

		if (urlMappingInfo == null && urlWildcardPatterns
				.containsKey(cmsContext.getAppCode() + cmsContext.getCurrentLocale().getLanguage())) {
			Matcher matcher = urlWildcardPatterns.get(cmsContext.getAppCode() + cmsContext.getCurrentLocale().getLanguage()).matcher(url);
			if (matcher.find()) {
				searchUrl = matcher.group();
				urlMappingInfo = urlMappingDao.getRequestMappingByUrl(searchUrl, cmsContext.getAppCode(),
						cmsContext.getCurrentLocale().getLanguage());
			}
		}

		return urlMappingInfo;

	}

	@Override
	public List<UrlMappingInfo> getFilterUrls() {
		return urlMappingDao.geFilterUrls();

	}

}
