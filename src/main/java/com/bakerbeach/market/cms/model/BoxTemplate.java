package com.bakerbeach.market.cms.model;

import java.util.Date;
import java.util.Map;

public class BoxTemplate {
	private Map<String, Object> map;
	private Date lastUpdate = new Date();

	public BoxTemplate(Map<String, Object> map) {
		this.map = map;
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

}
