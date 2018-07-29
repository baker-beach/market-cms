package com.bakerbeach.market.cms.service;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.bakerbeach.market.cms.model.BoxTemplate;
import com.bakerbeach.market.cms.model.Structure;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class PageDaoImpl implements PageDao {
    protected static final Logger log = LoggerFactory.getLogger(PageDaoImpl.class.getName());

    private MongoTemplate mongoTemplate;
    private String structureCollection;
    private String boxCollection;

    @Override
    public Structure findStructureById(String structureId) throws CmsDaoException {
        Bson filter = Filters.eq("structure_id", structureId);
        FindIterable<Document> dbo = getStructureCollection().find(filter);
        if (dbo != null)
            return new Structure(dbo.first());
        else
            throw new CmsDaoException.StructureNotFoundException("structure with ID " + structureId + " not found");
    }

    @Override
    public BoxTemplate findBoxByType(String boxType) throws CmsDaoException {
        Bson filter = Filters.eq("type", boxType);
        FindIterable<Document> dbo = getBoxCollection().find(filter);
        if (dbo != null) {
            return new BoxTemplate(dbo.first());
        } else {
            log.error("box with type " + boxType + " not found");
            throw new CmsDaoException("box with type " + boxType + " not found");
        }
    }

    public void setStructureCollection(String structureCollection) {
        this.structureCollection = structureCollection;
    }

    private MongoCollection<Document> getBoxCollection() {
        return mongoTemplate.getCollection(boxCollection);
    }

    private MongoCollection<Document> getStructureCollection() {
        return mongoTemplate.getCollection(structureCollection);
    }

    public void setBoxCollection(String boxCollection) {
        this.boxCollection = boxCollection;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

}
