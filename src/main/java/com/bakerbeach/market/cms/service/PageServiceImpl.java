package com.bakerbeach.market.cms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.box.Page;
import com.bakerbeach.market.cms.box.PageImpl;
import com.bakerbeach.market.cms.box.SimpleBox;
import com.bakerbeach.market.cms.model.BoxTemplate;
import com.bakerbeach.market.cms.model.RequestContext;
import com.bakerbeach.market.cms.model.Content;
import com.bakerbeach.market.cms.model.Structure;
import com.bakerbeach.market.commons.CopyHelper;

public class PageServiceImpl implements PageService, ApplicationContextAware {
	private static final Logger log = LoggerFactory.getLogger(PageService.class.getName());
	
//	private static final Integer DEFAULT_CACHE_TIME = 6000000;

	private PageDao pageDao;
	
	private Map<String, Structure> structureCache = new HashMap<String, Structure>();
	private Map<String, BoxTemplate> boxTemplateCache = new HashMap<String, BoxTemplate>();
	private Map<String, Content> contentCache = new HashMap<String, Content>();
	
	private ApplicationContext context;

	@Value("${page.cache.time:6000000}")
	private Integer cacheTime;
	
	@Override
	public Page getPage(RequestContext cmsContext) throws PageServiceException {
		try {
			Structure structure = getStructure(cmsContext.getPageId());
			return decodePage(structure);
		} catch (Exception e) {
			log.error(ExceptionUtils.getMessage(e));
			log.error("page id: " + cmsContext.getPageId());
			log.error("path: " + cmsContext.getPath());
			throw new PageServiceException.PageNotFoundException();
		}
	}
	
	private Page decodePage(Structure structure) throws CmsDaoException {
		PageImpl page = new PageImpl();
		decodeTree(structure, page, null, null);
		Box box = page.getBoxById((String) structure.get("box_id"));
		page.setRootBox(box);
		return page;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void decodeTree(Structure structure, PageImpl page, Box parentBox, String containerId)
			throws CmsDaoException {
		if (structure.containsKey("box_type")) {
			Box box = getBox((String) structure.get("box_type"));
			if (structure.containsKey("box_id")) {
				box.setId((String) structure.get("box_id"));
			}
			if (structure.containsKey("data")) {
				box.getData().putAll((Map) structure.get("data"));
			}
			if (structure.containsKey("content_id")) {
				box.setContentId((String) structure.get("content_id"));
				Content content = getContent((String) structure.get("content_id"));
				if (content != null && content.get() != null) {
					box.getData().putAll(content.get());					
				}
			}
			if (parentBox != null)
				parentBox.addChildBox(containerId, box);
			page.addBox(box);
			if (structure.containsKey("containers")) {
				for (Map.Entry<String, List<Map>> container : ((Map<String, List<Map>>) structure.get("containers"))
						.entrySet()) {
					for (Map childBoxStructure : container.getValue()) {
						decodeTree(new Structure(childBoxStructure), page, box, container.getKey());
					}
				}
			}
		} else if (structure.containsKey("structure_ref")) {
			Structure childStructure = getStructure((String) structure.get("structure_ref"));
			decodeTree(childStructure, page, parentBox, containerId);
		}
	}

	private Structure getStructure(String id) throws CmsDaoException {
		Structure structure = structureCache.get(id);
		if (structure != null) {
			if (structure.getLastUpdate().getTime() > (new Date()).getTime() - cacheTime) {
				return structure;
			}
		}

		structure = pageDao.findStructureById(id);
		structureCache.put(id, structure);
		return structure;
	}
	
	@Override
	public Box getBoxByType(String boxType) throws PageServiceException {
		try {
			return getBox(boxType);
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			throw new PageServiceException.PageNotFoundException();
		}
	}

	private Box getBox(String type) throws CmsDaoException {
		BoxTemplate template = boxTemplateCache.get(type);
		if (template != null) {
			if (template.getLastUpdate().getTime() > (new Date()).getTime() - cacheTime) {
				return decodeBox(template);
			}
		}
		
		template = pageDao.findBoxByType(type);
		boxTemplateCache.put(type, template);
		return decodeBox(template);
	}

	private Content getContent(String contentId) throws CmsDaoException {
		log.debug("content_id: " + contentId);
		
		Content content = contentCache.get(contentId);
		if (content != null) {
			if (content.getLastUpdate().getTime() > (new Date()).getTime() - cacheTime) {
				return content;
			}
		}
		
		content = pageDao.findContentById(contentId);
		contentCache.put(contentId, content);
		return content;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Box decodeBox(BoxTemplate boxDescription) throws CmsDaoException {
		Box box = null;

		if (boxDescription != null) {
			String clazz = (String) boxDescription.get("class");
			if (clazz != null) {
				try {
					box = (Box) context.getBean(clazz);
					//box = (Box) Class.forName(clazz).newInstance();
				} catch (BeansException e) {
					log.error("error while loading box class " + clazz);
					log.error(ExceptionUtils.getFullStackTrace(e));
					box = (Box) context.getBean(SimpleBox.class); //new SimpleBox();
				}
			} else {
				box = (Box) context.getBean(SimpleBox.class);
			}
			if (box != null) {
				if(boxDescription.get("data") != null)
					box.getData().putAll((Map) CopyHelper.copy((Map<String,Object>)boxDescription.get("data")));
				if(boxDescription.get("template") != null)
					box.setTemplate((String)boxDescription.get("template"));
			}
		}
		return box;
	}

	@Override
	public void clearCache() {
		structureCache.clear();
		boxTemplateCache.clear();
		contentCache.clear();
	}

	public PageDao getPageDao() {
		return pageDao;
	}

	public void setPageDao(PageDao pageDao) {
		this.pageDao = pageDao;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;		
	}

}