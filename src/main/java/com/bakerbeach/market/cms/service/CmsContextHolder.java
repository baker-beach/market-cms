package com.bakerbeach.market.cms.service;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.bakerbeach.market.cms.model.CmsContext;

public class CmsContextHolder {

	public static final String CMS_CONTEXT_REQUEST_ATTRIBUTES_KEY = "shop_context_request_attributes";

	public static CmsContext getInstance() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		CmsContext cmsCtx = (CmsContext) requestAttributes.getAttribute(CMS_CONTEXT_REQUEST_ATTRIBUTES_KEY,
				RequestAttributes.SCOPE_SESSION);

		return cmsCtx;
	}

	public static void setInstance(CmsContext cmsCtx) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		requestAttributes.setAttribute(CMS_CONTEXT_REQUEST_ATTRIBUTES_KEY, cmsCtx, RequestAttributes.SCOPE_SESSION);
	}

}
