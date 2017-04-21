package com.bakerbeach.market.cms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;

import com.bakerbeach.market.cms.model.CmsContext;
import com.bakerbeach.market.cms.model.UrlMappingInfo;

public class UrlServiceImpl implements UrlService {
	private UrlMappingDao urlMappingDao;
	private Map<String, Pattern> urlWildcardPatterns;

	private Map<String, UrlMappingInfo> urlMappingCache = new HashMap<>();

	@Value("${url.cache.time:6000000}")
	private Integer cacheTime;
	
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
		String shopId = cmsContext.getAppCode();
		String language = cmsContext.getCurrentLocale().getLanguage();

		UrlMappingInfo mapping = getMapping(url, shopId, language);

		if (mapping == null && urlWildcardPatterns
				.containsKey(cmsContext.getAppCode() + cmsContext.getCurrentLocale().getLanguage())) {
			Matcher matcher = urlWildcardPatterns
					.get(cmsContext.getAppCode() + cmsContext.getCurrentLocale().getLanguage()).matcher(url);
			if (matcher.find()) {
				mapping = getMapping(matcher.group(), shopId, language);
			}
		}

		return mapping;
	}

	private UrlMappingInfo getMapping(String url, String shopId, String language) {
		String key = new StringBuilder(url).append(":").append(shopId).append(":").append(language).toString();

		UrlMappingInfo mapping = urlMappingCache.get(key);
		if (mapping != null) {
			if (mapping.getLastUpdate().getTime() > (new Date()).getTime() - cacheTime) {
				return mapping;
			}
		}

		mapping = urlMappingDao.getRequestMappingByUrl(url, shopId, language);
		urlMappingCache.put(key, mapping);

		return mapping;
	}
	
	@Override
	public void save(UrlMappingInfo urlMappingInfo) {
		urlMappingDao.save(urlMappingInfo);
	}
	
	@Override
	public void clearCache() {
		urlMappingCache.clear();
	}
	
	@Override
	public List<UrlMappingInfo> getFilterUrls() {
		return urlMappingDao.geFilterUrls();

	}

}
