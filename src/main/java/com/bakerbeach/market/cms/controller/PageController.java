package com.bakerbeach.market.cms.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bakerbeach.market.cms.box.Box;
import com.bakerbeach.market.cms.box.Page;
import com.bakerbeach.market.cms.box.ProcessableBox;
import com.bakerbeach.market.cms.box.ProcessableBoxException;
import com.bakerbeach.market.cms.box.RedirectException;
import com.bakerbeach.market.cms.model.CmsContext;
import com.bakerbeach.market.cms.model.Redirect;
import com.bakerbeach.market.cms.service.CmsContextHolder;
import com.bakerbeach.market.cms.service.Helper;
import com.bakerbeach.market.cms.service.PageService;
import com.bakerbeach.market.cms.service.PageServiceException.PageNotFoundException;
import com.bakerbeach.market.commons.Messages;
import com.bakerbeach.market.commons.MessagesImpl;

@Controller
public class PageController implements ApplicationContextAware{

	private static final Logger log = LoggerFactory.getLogger(PageController.class.getName());

	@Autowired
	private PageService pageService;

	@RequestMapping(value = "/**")
	public String getPage(ModelMap map, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttrs) {

		CmsContext cmsContext = CmsContextHolder.getInstance();
		cmsContext.setHttpServletRequest(request);
		cmsContext.setHttpServletResponse(response);
		cmsContext.setModelMap(map);
		
		if(map.get("messages") == null && request.getSession().getAttribute("messages") != null){
			Messages messages = (Messages)request.getSession().getAttribute("messages");
			map.put("messages", messages);
			request.getSession().setAttribute("messages",null);
		}
		
		if(map.get("messages") == null){
			Messages messages = new MessagesImpl();
			map.put("messages", messages);
		}
		

		
		map.put("cmsCtx", cmsContext);
		
		Helper helper = null;
	
		try {
			helper = (Helper) context.getBean(Class.forName(cmsContext.getHelperClass()));
		} catch (BeansException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		map.put("helper",helper);
		

		
		try {
			Page page = null;
			try{
				page = pageService.getPage(cmsContext);
			}catch(PageNotFoundException pnfe){
				cmsContext.setPageId(cmsContext.getDefaultPageId());
				page = pageService.getPage(cmsContext);
			}
				
			Redirect redirect = doActionRequest(page.getRootBox(), request, response, map);
			if (redirect != null) {
				map.clear();
				StringBuilder sb = new StringBuilder("redirect:");
				if (redirect.getType().equals(Redirect.URL_ID))
					sb.append(helper.pageUri(redirect.getValue()));
				else
					sb.append(redirect.getValue());
				if (redirect.getParams() != null) {
					sb.append(redirect.getParams());
				}
				return sb.toString();
			}

			doRenderRequest(page.getRootBox(), request, response, map);
			return page.getRootBox().getTemplate();
		}catch (Exception e) {
			log.error(ExceptionUtils.getMessage(e));
			map.clear();
			return "redirect:" + helper.pageUrl("exception");
		}

	}

	protected void doRenderRequest(Box box, HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		modelMap.put("page", box);
		modelMap.put("self", box);
		doTreeRenderRequest(box, request, response, modelMap);
	}

	protected Redirect doActionRequest(Box box, HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		if (box instanceof ProcessableBox) {
			try {
				((ProcessableBox) box).handleActionRequest(request, response, modelMap);
			} catch (RedirectException e) {
				return e.getRedirect();
			} catch (ProcessableBoxException e) {
				return new Redirect(CmsContextHolder.getInstance().getDefaultPageId(),"");
			}
		}

		for (Map.Entry<String, List<Box>> entry : box.getContainerMap().entrySet()) {
			for (Box b : entry.getValue()) {
				Redirect redirect = doActionRequest(b, request, response, modelMap);
				if (redirect != null)
					return redirect;
			}
		}
		return null;
	}

	private void doTreeRenderRequest(Box box, HttpServletRequest request, HttpServletResponse response,
			ModelMap modelMap) {
		box.handleRenderRequest(request, response, modelMap);

		for (Map.Entry<String, List<Box>> entry : box.getContainerMap().entrySet()) {
			for (Box b : entry.getValue())
				doTreeRenderRequest(b, request, response, modelMap);
		}
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
