package com.bakerbeach.market.cms.service;

import com.bakerbeach.market.cms.model.CmsContext;

public interface CmsContextFactory {
	
	CmsContext newInstance(String host, String path) throws CmsContextException;

}
