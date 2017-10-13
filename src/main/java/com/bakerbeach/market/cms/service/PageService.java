package com.bakerbeach.market.cms.service;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.box.Page;
import com.bakerbeach.market.cms.model.RequestContext;

public interface PageService {

	Page getPage(RequestContext cmsContext) throws PageServiceException;

	Box getBoxByType(String boxType) throws PageServiceException;

	void clearCache();

}
