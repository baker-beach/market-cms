package com.bakerbeach.market.cms.service;

import java.util.Map;

import com.bakerbeach.market.cms.model.CmsContext;
import com.bakerbeach.market.cms.model.CmsContextImpl;

public class CmsContextFactoryImpl implements CmsContextFactory {

	private Map<String, CmsContext> contextDefinitions;

	public CmsContext newInstance(String host, String path) throws CmsContextException {
		try {
			CmsContext contextDefinition = contextDefinitions.get(host);
			CmsContextImpl cmsContext = new CmsContextImpl(contextDefinition);
			cmsContext.setPath(path);
			cmsContext.setHost(host);

			return cmsContext;
		} catch (Exception e) {
			throw new CmsContextException(e);
		}
	}

	public void setContextDefinitions(Map<String, CmsContext> contextDefinitions) {
		this.contextDefinitions = contextDefinitions;
	}
}
