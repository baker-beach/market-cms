package com.bakerbeach.market.cms.service;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.box.Page;
import com.bakerbeach.market.cms.model.CmsContext;

public class PageServiceImpl implements PageService{
	
	private static final Logger log = LoggerFactory.getLogger(PageService.class.getName());

	private PageDao pageDao;

	@Override
	public Page getPage(CmsContext cmsContext) throws PageServiceException {
		try {
			return pageDao.findPageById(cmsContext.getPageId());
		} catch (Exception e) {
			log.error(ExceptionUtils.getMessage(e));
			log.error("page id: " + cmsContext.getPageId());
			log.error("path: " + cmsContext.getPath());
			throw new PageServiceException.PageNotFoundException();
		}

	}

	public PageDao getPageDao() {
		return pageDao;
	}

	public void setPageDao(PageDao pageDao) {
		this.pageDao = pageDao;
	}

	@Override
	public Box getBoxByType(String boxType) throws PageServiceException {
		try {
			return pageDao.findBoxByType(boxType);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			throw new PageServiceException.PageNotFoundException();
		}
	}
}