package com.bakerbeach.market.cms.view.jackson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.view.json.AbstractJackson2View;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.service.Helper;
import com.bakerbeach.market.commons.FieldMessage;
import com.bakerbeach.market.commons.Message;
import com.bakerbeach.market.commons.Messages;
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

	@Override
	protected Object filterAndWrapModel(Map<String, Object> model, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>(model.size());

		Box rootBox = (Box) model.get("self");
		if (rootBox != null) {
			result = getData(rootBox);
		}

		MessagesView messages = new MessagesView((Helper) model.get("helper"), (Messages) model.get("messages"));
		result.put("messages", messages);

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

	public static class MessagesView {
		List<MessageView> globalMessages = new ArrayList<>();
		List<MessageView> fieldMessages = new ArrayList<>();

		public MessagesView(Helper helper, Messages messages) {
			if (messages != null) {
				messages.getGlobalMessages().forEach(m -> {
					// TODO: remove that workaround and show all global messages
					// as soon they can be handled in frontend
					if (m.getTags() != null && m.getTags().contains(Message.TAG_BOX)) {
						globalMessages.add(new MessageView(helper, m));
					}
				});
				messages.getFieldMessages().forEach(m -> {
					fieldMessages.add(new FieldMessageView(helper, m));
				});
			}
		}

		public List<MessageView> getGlobalMessages() {
			return globalMessages;
		}
	}

	public static class MessageView {
		protected Message message;
		protected String translation;

		public MessageView(Helper helper, Message message) {
			this.message = message;
			this.translation = helper.t("message", message.getCode(), message.getArgs());
		}

		public Object[] getArgs() {
			return message.getArgs();
		}

		public String getCode() {
			return message.getCode();
		}

		public String getType() {
			return message.getType();
		}

		public String getTranslation() {
			return translation;
		}

	}

	public static class FieldMessageView extends MessageView {

		public FieldMessageView(Helper helper, FieldMessage message) {
			super(helper, message);
		}

		public String getName() {
			return ((FieldMessage) message).getName();
		}

		public Object getRejectedValue() {
			return ((FieldMessage) message).getRejectedValue();
		}

	}
}
