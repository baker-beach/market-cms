package com.bakerbeach.market.cms.service;

import com.bakerbeach.market.cms.model.BoxTemplate;
import com.bakerbeach.market.cms.model.Content;
import com.bakerbeach.market.cms.model.Structure;

public interface PageDao {

	Structure findStructureById(String structureId) throws CmsDaoException;

	public BoxTemplate findBoxByType(String boxType) throws CmsDaoException;

	Content findContentById(String contentId) throws CmsDaoException;

}
