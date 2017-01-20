package com.bakerbeach.market.cms.service;

import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheHandler {

	@Autowired
	private UrlService urlService;

	public void clearCache(Exchange ex) {
		urlService.clearCache();
	}

}
