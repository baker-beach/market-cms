package com.bakerbeach.market.cms.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.bakerbeach.market.cms.model.UrlMappingInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

public class UrlMappingDaoImpl implements UrlMappingDao {

	private MongoTemplate mongoTemplate;
	private String requestMappingCollection;
	private Map<String, Map<String, Object>> urlCache = new HashMap<String, Map<String, Object>>();

	@SuppressWarnings("unchecked")
	public String getRedirectUrl(String redirect, String shopId, String language) {
		if (!urlCache.containsKey("_" + redirect + shopId + language)) {
			QueryBuilder qb = new QueryBuilder();
			qb.and("urls.redirect_id").is(redirect);
			qb.and("urls.shop_code").is(shopId);
			qb.and("urls.lang").is(language);
			DBObject dbo = getRequestMappingCollection().findOne(qb.get());
			if (dbo != null)
				urlCache.put("_" + redirect + shopId + language, (Map<String, Object>) dbo.toMap());
		}

		UrlMappingInfo urlMapping = decodeRequestMapping(urlCache.get("_" + redirect + shopId + language));
		String uri = null;
		if (urlMapping != null)
			try {
				for (Map<String, String> entry : urlMapping.getUrls()) {
					if (entry.get("shop_code").equals(shopId) && entry.get("lang").equals(language)) {
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

	@SuppressWarnings("unchecked")
	public String getPageUrl(String urlId, String shopId, String language) {
		if (!urlCache.containsKey(urlId + shopId + language)) {
			QueryBuilder qb = new QueryBuilder();
			qb.and("url_id").is(urlId);
			qb.and("urls.shop_code").is(shopId);
			qb.and("urls.lang").is(language);
			DBObject dbo = getRequestMappingCollection().findOne(qb.get());
			if (dbo != null)
				urlCache.put(urlId + shopId + language, (Map<String, Object>) dbo.toMap());
		}

		UrlMappingInfo urlMapping = decodeRequestMapping(urlCache.get(urlId + shopId + language));
		String uri = null;
		if (urlMapping != null)
			try {
				for (Map<String, String> entry : urlMapping.getUrls()) {
					if (entry.get("shop_code").equals(shopId) && entry.get("lang").equals(language)) {
						uri = entry.get("value");
						break;
					}
				}
			} catch (NullPointerException e) {
			}
		return uri;
	}
	
	@SuppressWarnings("unchecked")
	public UrlMappingInfo getRequestMappingByUrl(String url, String shopId, String language) {
		if (!urlCache.containsKey(url + shopId + language)) {
			QueryBuilder qb = new QueryBuilder();
			qb.and("urls.value").is(url);
			qb.and("urls.shop_code").is(shopId);
			qb.and("urls.lang").is(language);
			DBObject dbo = getRequestMappingCollection().findOne(qb.get());
			if (dbo != null)
				urlCache.put(url + shopId + language, (Map<String, Object>) dbo.toMap());
		}
		return decodeRequestMapping(urlCache.get(url + shopId + language));
	}
	
	@SuppressWarnings("unchecked")
	public List<UrlMappingInfo> geFilterUrls() {
		List<UrlMappingInfo> result = new ArrayList<UrlMappingInfo>();
		QueryBuilder qb = new QueryBuilder();
		qb.and("filter").exists(1);
		DBCursor dbc = getRequestMappingCollection().find(qb.get());
		while(dbc.hasNext()){
			result.add(decodeRequestMapping((Map<String, Object>)dbc.next().toMap()));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<UrlMappingInfo> getWildcardMappings() {
		List<UrlMappingInfo> result = new ArrayList<UrlMappingInfo>();
		QueryBuilder qb = new QueryBuilder();
		qb.and("wildcard").exists(1);
		DBCursor dbc = getRequestMappingCollection().find(qb.get());
		while(dbc.hasNext()){
			result.add(decodeRequestMapping((Map<String, Object>)dbc.next().toMap()));
		}
		return result;
	}
	
	public void save(UrlMappingInfo urlMapping) {
		DBObject dbo = encodeRequestMapping(urlMapping);
		
		QueryBuilder qb = new QueryBuilder();
		qb.and("url_id").is(urlMapping.getUrlId());

		getRequestMappingCollection().findAndModify(qb.get(), null, null, false, new BasicDBObject("$set", dbo), false, true);
//		getRequestMappingCollection().save(dbo);
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

	protected DBCollection getRequestMappingCollection() {
		return mongoTemplate.getCollection(requestMappingCollection);
	}

	public void setRequestMappingCollection(String requestMappingCollection) {
		this.requestMappingCollection = requestMappingCollection;
	}

}
