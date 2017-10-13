package com.bakerbeach.market.cms.service;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.bakerbeach.market.cms.model.RequestContext;

public class CmsContextHolder {

	public static final String CMS_CONTEXT_REQUEST_ATTRIBUTES_KEY = "shop_context_request_attributes";

	public static RequestContext getInstance() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		RequestContext cmsCtx = (RequestContext) requestAttributes.getAttribute(CMS_CONTEXT_REQUEST_ATTRIBUTES_KEY,
				RequestAttributes.SCOPE_REQUEST);

		return cmsCtx;
	}

	public static void setInstance(RequestContext cmsCtx) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.setAttribute(CMS_CONTEXT_REQUEST_ATTRIBUTES_KEY, cmsCtx, RequestAttributes.SCOPE_REQUEST);
	}

}
