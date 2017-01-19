package com.bakerbeach.market.cms.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.box.Page;
import com.bakerbeach.market.cms.box.PageImpl;
import com.bakerbeach.market.cms.box.SimpleBox;
import com.bakerbeach.market.commons.CopyHelper;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class PageDaoImpl implements PageDao, ApplicationContextAware{
	
	protected static final Logger log = LoggerFactory.getLogger(PageDaoImpl.class.getName());

	private MongoTemplate mongoTemplate;
	private String structureCollection;
	private String boxCollection;
	private String contentCollection;
	
	private Map<String, Map<String, Object>> boxCache = new HashMap<String, Map<String, Object>>();
	private Map<String, Map<String, Object>> structureCache = new HashMap<String, Map<String, Object>>();
	private Map<String, Map<String, Object>> contentCache = new HashMap<String, Map<String, Object>>();
	

	public boolean clearCache() {
		boxCache = new HashMap<String, Map<String, Object>>();
		structureCache = new HashMap<String, Map<String, Object>>();
		contentCache = new HashMap<String, Map<String, Object>>();
		return true;
	}

	public Page findPageById(String pageId) throws CmsDaoException {
		try {
			Map<String, Object> structureDescription = findStructureDocumentById(pageId);
			return decodePage(structureDescription);
		} catch (CmsDaoException e) {
			log.error(e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			throw new CmsDaoException("error while loading page with ID " + pageId);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> findStructureDocumentById(String structureId) throws CmsDaoException {
		log.debug("load structure " + structureId);
		if (!structureCache.containsKey(structureId)) {
			QueryBuilder qb = new QueryBuilder();
			qb.and("structure_id").is(structureId);
			DBObject dbo = getStructureCollection().findOne(qb.get());
			if (dbo != null)
				structureCache.put(structureId, dbo.toMap());
			else
				throw new CmsDaoException.StructureNotFoundException("structure with ID " + structureId + " not found");
		}
		return structureCache.get(structureId);
	}

	private Page decodePage(Map<String, Object> structureDescription) throws CmsDaoException {
		PageImpl page = new PageImpl();
		decodeTree(structureDescription, page, null, null);
		Box box = page.getBoxById((String) structureDescription.get("box_id"));
		page.setRootBox(box);
		return page;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void decodeTree(Map<String, Object> dbo, PageImpl page, Box parentBox, String containerId)
			throws CmsDaoException {
		if (dbo.containsKey("box_type")) {
			Box box = findBoxByType((String) dbo.get("box_type"));
			if (dbo.containsKey("box_id")) {
				box.setId((String) dbo.get("box_id"));
			}
			if (dbo.containsKey("data")) {
				box.getData().putAll((Map) dbo.get("data"));
			}
			if (dbo.containsKey("content_id")) {
				box.setContentId((String) dbo.get("content_id"));
				Map<String, Object> content = findContentById((String) dbo.get("content_id"));
				box.getData().putAll(content);
			}
			if (parentBox != null)
				parentBox.addChildBox(containerId, box);
			page.addBox(box);
			if (dbo.containsKey("containers")) {
				for (Map.Entry<String, List<Map>> container : ((Map<String, List<Map>>) dbo.get("containers"))
						.entrySet()) {
					for (Map childBoxStructure : container.getValue()) {
						decodeTree(childBoxStructure, page, box, container.getKey());
					}
				}
			}
		} else if (dbo.containsKey("structure_ref")) {
			Map<String, Object> structureDescription = findStructureDocumentById((String) dbo.get("structure_ref"));
			decodeTree(structureDescription, page, parentBox, containerId);
		}
	}

	@SuppressWarnings("unchecked")
	public Box findBoxByType(String boxType) throws CmsDaoException {
		log.debug("load box " + boxType);
		try {
			if (!boxCache.containsKey(boxType)) {
				QueryBuilder qb = new QueryBuilder();
				qb.and("type").is(boxType);
				DBObject dbo = getBoxCollection().findOne(qb.get());
				if (dbo != null) {
					boxCache.put(boxType, dbo.toMap());
				} else {
					log.error("box with type " + boxType + " not found");
					throw new CmsDaoException("box with type " + boxType + " not found");
				}
			}
			return decodeBox(boxCache.get(boxType));
		} catch (Exception e) {
			log.error(ExceptionUtils.getFullStackTrace(e));
			log.error("error while loading box with type " + boxType);
			throw new CmsDaoException("error while loading box with type " + boxType);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Box decodeBox(Map<String, Object> boxDescription) throws CmsDaoException {
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
					box.getData().put("template", boxDescription.get("template"));
			}
		}
		return box;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findContentById(String contentId) throws CmsDaoException {
		log.debug("content_id: " + contentId);
		if (contentCache.containsKey(contentId)) {
			return contentCache.get(contentId);
		} else {
			QueryBuilder qb = new QueryBuilder();
			qb.and("content_id").is(contentId);
			try {
				DBObject dbo = getContentCollection().findOne(qb.get());
				if (dbo != null) {
					contentCache.put(contentId, (Map<String, Object>) dbo.toMap().get("data"));
					return (Map<String, Object>) dbo.toMap().get("data");
				} else {
					return new HashMap<>();
				}
			} catch (Exception e) {
				throw new CmsDaoException("error while loading content with ID " + contentId);
			}
		}
	}

	public void setStructureCollection(String structureCollection) {
		this.structureCollection = structureCollection;
	}

	private DBCollection getStructureCollection() {
		return mongoTemplate.getCollection(structureCollection);
	}

	private DBCollection getBoxCollection() {
		return mongoTemplate.getCollection(boxCollection);
	}

	public void setBoxCollection(String boxCollection) {
		this.boxCollection = boxCollection;
	}

	private DBCollection getContentCollection() {
		return mongoTemplate.getCollection(contentCollection);
	}

	public void setContentCollection(String contentCollection) {
		this.contentCollection = contentCollection;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
    private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        context = ac;
    }

}
