package com.bakerbeach.market.cms.service;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.box.Page;

public interface PageDao {
	
	public Page findPageById(String pageId) throws CmsDaoException;
	
	public Box findBoxByType(String boxType) throws CmsDaoException;

}
