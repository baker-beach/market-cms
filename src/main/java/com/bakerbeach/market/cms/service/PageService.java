package com.bakerbeach.market.cms.service;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.box.Page;
import com.bakerbeach.market.cms.model.CmsContext;

public interface PageService {

	Page getPage(CmsContext cmsContext) throws PageServiceException;

	Box getBoxByType(String boxType) throws PageServiceException;

}
