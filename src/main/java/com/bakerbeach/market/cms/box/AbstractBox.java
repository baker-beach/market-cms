package com.bakerbeach.market.cms.box;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.ServletRequestDataBinder;

import com.bakerbeach.market.commons.FieldMessageImpl;
import com.bakerbeach.market.commons.Message;
import com.bakerbeach.market.commons.Messages;

@SuppressWarnings("serial")
public abstract class AbstractBox extends HashMap<String, Object> implements Box {
	protected static Logger log = LoggerFactory.getLogger(Class.class.getName());

	public static final String TYPE_KEY = "type";
	public static final String TEMPLATE_KEY = "template";
	public static final String DATA_KEY = "data";
	public static final String META_DATA_KEY = "metadata";
	public static final String CONTENT_KEY = "content";

	protected Date startDate;
	protected Date endDate;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	protected String id;
	protected Map<String, List<Box>> childBoxesByContainer = new HashMap<>();
	{
		setData(new HashMap<String, Object>());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return (String) get(TYPE_KEY);
	}

	public void setType(String type) {
		put(TYPE_KEY, type);
	}

	public String getTemplate() {
		if (this.isActive()) {
			if (getData().containsKey(TEMPLATE_KEY))
				return (String) getData().get(TEMPLATE_KEY);
			return (String) get(TEMPLATE_KEY);
		} else
			return "/empty";
	}

	public void setTemplate(String template) {
		put(TEMPLATE_KEY, template);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getData() {
		return (Map<String, Object>) get(DATA_KEY);
	}

	public void setData(Map<String, Object> data) {
		put(DATA_KEY, data);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getMetaData() {
		return (Map<String, Object>) get(META_DATA_KEY);
	}

	public void setMetaData(Map<String, Object> data) {
		put(META_DATA_KEY, data);
	}

	public void addChildBox(String region, Box box) {
		if (childBoxesByContainer.containsKey(region))
			childBoxesByContainer.get(region).add(box);
		else {
			List<Box> boxes = new ArrayList<>();
			boxes.add(box);
			childBoxesByContainer.put(region, boxes);
		}
	}

	@Override
	public Map<String, List<Box>> getContainerMap() {
		return childBoxesByContainer;
	}

	@Override
	public List<Box> getChildBoxesByContainer(String region) {
		if (!childBoxesByContainer.containsKey(region)) {
			childBoxesByContainer.put(region, new ArrayList<Box>());
		}
		return childBoxesByContainer.get(region);
	}

	public void handleRenderRequest(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
	}

	@Override
	public String toString() {
		return this.getClass().getCanonicalName();
	}

	protected BindingResult bind(Object bindingObject, HttpServletRequest request) {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		ServletRequestDataBinder servletRequestDataBinder = new ServletRequestDataBinder(bindingObject);
		servletRequestDataBinder.setValidator(new SpringValidatorAdapter(validator));
		servletRequestDataBinder.bind(request);
		servletRequestDataBinder.validate();
		BindingResult result = servletRequestDataBinder.getBindingResult();
		return result;
	}

	public void getFieldErrors(BindingResult result, Messages messages) {
		for (ObjectError tempError : result.getAllErrors()) {
			if (tempError instanceof FieldError) {
				String name = ((FieldError) tempError).getField();
				if (messages.getFieldError(name) == null) {
					messages.addFieldError(new FieldMessageImpl(name, Message.TYPE_ERROR, tempError.getDefaultMessage(), tempError.getArguments()));
				}
			}
		}
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setStartDate(String date) {
		try {
			this.startDate = simpleDateFormat.parse(date);
		} catch (ParseException e) {
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setEndDate(String date) {
		try {
			this.endDate = simpleDateFormat.parse(date);
		} catch (ParseException e) {
		}
	}

	public boolean isActive() {
		Date now = new Date();
		if (startDate != null && now.before(startDate))
			return false;
		if (endDate != null && now.after(endDate))
			return false;
		return true;
	}

}
