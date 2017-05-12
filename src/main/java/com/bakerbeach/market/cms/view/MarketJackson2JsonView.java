package com.bakerbeach.market.cms.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.view.json.AbstractJackson2View;

import com.bakerbeach.market.cms.box.Box;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MarketJackson2JsonView extends AbstractJackson2View {
	public static final String DEFAULT_CONTENT_TYPE = "application/json";

	private Set<String> modelKeys;

	public MarketJackson2JsonView() {
		super(Jackson2ObjectMapperBuilder.json().build(), DEFAULT_CONTENT_TYPE);
	}

	public MarketJackson2JsonView(ObjectMapper objectMapper) {
		super(objectMapper, DEFAULT_CONTENT_TYPE);
	}

	@Override
	protected Object filterModel(Map<String, Object> model) {
		Map<String, Object> result = new HashMap<String, Object>(model.size());

		Box rootBox = (Box) model.get("self");
		if (rootBox != null) {
			result = getData(rootBox);
		}

		return result;
	}

	protected Map<String, Object> getData(Box box) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", box.getId());
		result.putAll(box.getData());

		for (Entry<String, List<Box>> e : box.getContainerMap().entrySet()) {
			Map<String, Object> container = new HashMap<String, Object>();
			result.put(e.getKey(), container);

			for (Box childBox : e.getValue()) {
				container.put(childBox.getId(), getData(childBox));
			}
		}

		return result;
	}

	@Override
	public void setModelKey(String modelKey) {
		this.modelKeys = Collections.singleton(modelKey);
	}

	public void setModelKeys(Set<String> modelKeys) {
		this.modelKeys = modelKeys;
	}

	public final Set<String> getModelKeys() {
		return this.modelKeys;
	}

}
