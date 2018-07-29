package com.bakerbeach.market.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.bakerbeach.market.cms.model.UrlMappingInfo;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class UrlMappingDaoImpl implements UrlMappingDao {

    private MongoTemplate mongoTemplate;
    private String requestMappingCollection;
    private Map<String, Map<String, Object>> urlCache = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public String getRedirectUrl(String redirect, String shopId, String language) {
        if (!urlCache.containsKey("_" + redirect + shopId + language)) {
            Bson filter = Filters.and(Filters.eq("urls.redirect_id", redirect), Filters.eq("urls.app_code", shopId), Filters.eq("urls.lang", language));
            FindIterable<Document> documentList = getRequestMappingCollection().find(filter);
            if (documentList != null)
                urlCache.put("_" + redirect + shopId + language, documentList.first());
        }

        UrlMappingInfo urlMapping = decodeRequestMapping(urlCache.get("_" + redirect + shopId + language));
        String uri = null;
        if (urlMapping != null)
            try {
                for (Map<String, String> entry : urlMapping.getUrls()) {
                    if (entry.get("app_code").equals(shopId) && entry.get("lang").equals(language)) {
                        uri = entry.get("value");
                        break;
                    }
                }
            } catch (NullPointerException e) {
            }
        return uri;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getPageUrl(String urlId, String shopId, String language) {
        if (!urlCache.containsKey(urlId + shopId + language)) {
            Bson filter = Filters.and(Filters.eq("url_id", urlId), Filters.eq("urls.app_code", shopId), Filters.eq("urls.lang", language));
            FindIterable<Document> documentList = getRequestMappingCollection().find(filter);
            if (documentList != null)
                urlCache.put(urlId + shopId + language, documentList.first());
        }

        UrlMappingInfo urlMapping = decodeRequestMapping(urlCache.get(urlId + shopId + language));
        String uri = null;
        if (urlMapping != null)
            try {
                for (Map<String, String> entry : urlMapping.getUrls()) {
                    if (entry.get("app_code").equals(shopId) && entry.get("lang").equals(language)) {
                        uri = entry.get("value");
                        break;
                    }
                }
            } catch (NullPointerException e) {
            }
        return uri;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UrlMappingInfo getRequestMappingByUrl(String url, String shopId, String language) {
        if (!urlCache.containsKey(url + shopId + language)) {
            Bson filter = Filters.and(Filters.eq("urls.value", url), Filters.eq("urls.app_code", shopId), Filters.eq("urls.lang", language));
            FindIterable<Document> documentList = getRequestMappingCollection().find(filter);
            if (documentList != null)
                urlCache.put(url + shopId + language, documentList.first());
        }
        return decodeRequestMapping(urlCache.get(url + shopId + language));
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UrlMappingInfo> geFilterUrls() {
        List<UrlMappingInfo> result = new ArrayList<>();
        Bson filter = Filters.exists("filter");
        FindIterable<Document> documentList = getRequestMappingCollection().find(filter);
        for (Document document : documentList) {
            result.add(decodeRequestMapping(document));
        }
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<UrlMappingInfo> getWildcardMappings() {
        List<UrlMappingInfo> result = new ArrayList<>();
        Bson filter = Filters.exists("wildcard");
        FindIterable<Document> documentList = getRequestMappingCollection().find(filter);
        for (Document document : documentList) {
            result.add(decodeRequestMapping(document));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(UrlMappingInfo urlMapping) {
        DBObject dbo = encodeRequestMapping(urlMapping);

        Bson filter = Filters.eq("url_id", urlMapping.getUrlId());
        getRequestMappingCollection().findOneAndUpdate(filter, new Document(dbo.toMap()));
    }

    public static UrlMappingInfo decodeRequestMapping(Map<String, Object> map) {
        if (map != null) {
            UrlMappingInfo requestMapping = new UrlMappingInfo();
            requestMapping.putAll(map);
            return requestMapping;
        }
        return null;
    }

    public static DBObject encodeRequestMapping(UrlMappingInfo urlMapping) {
        BasicDBObjectBuilder objectBuilder = BasicDBObjectBuilder.start(urlMapping);
        return objectBuilder.get();
    }

    protected MongoCollection<Document> getRequestMappingCollection() {
        return mongoTemplate.getCollection(requestMappingCollection);
    }

    public void setRequestMappingCollection(String requestMappingCollection) {
        this.requestMappingCollection = requestMappingCollection;
    }

}
