package com.bakerbeach.market.cms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.bakerbeach.market.cms.model.BoxTemplate;
import com.bakerbeach.market.cms.model.Structure;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class PageDaoImpl implements PageDao {
		protected static final Logger log = LoggerFactory.getLogger(PageDaoImpl.class.getName());

	private MongoTemplate mongoTemplate;
	private String structureCollection;
	private String boxCollection;
	
	@Override
	@SuppressWarnings("unchecked")
	public Structure findStructureById(String structureId) throws CmsDaoException {
		QueryBuilder qb = new QueryBuilder();
		qb.and("structure_id").is(structureId);
		DBObject dbo = getStructureCollection().findOne(qb.get());
		if (dbo != null)
			return new Structure(dbo.toMap());
		else
			throw new CmsDaoException.StructureNotFoundException("structure with ID " + structureId + " not found");
	}
	
	@SuppressWarnings("unchecked")
	public BoxTemplate findBoxByType(String boxType) throws CmsDaoException {
		QueryBuilder qb = new QueryBuilder();
		qb.and("type").is(boxType);
		DBObject dbo = getBoxCollection().findOne(qb.get());
		if (dbo != null) {
			return new BoxTemplate(dbo.toMap());
		} else {
			log.error("box with type " + boxType + " not found");
			throw new CmsDaoException("box with type " + boxType + " not found");
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

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

}
