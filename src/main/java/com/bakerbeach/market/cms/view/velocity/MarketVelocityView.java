package com.bakerbeach.market.cms.view.velocity;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.BeansException;
import org.springframework.core.NestedIOException;
import org.springframework.web.servlet.view.AbstractTemplateView;

public class MarketVelocityView extends AbstractTemplateView {

    @Override
    protected void initApplicationContext() throws BeansException {
    }

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        try {
            // Check that we can get the template, even if we might subsequently get it
            // again.
            getTemplate(getUrl());
            return true;
        } catch (ResourceNotFoundException ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("No Velocity view found for URL: " + getUrl());
            }
            return false;
        } catch (Exception ex) {
            throw new NestedIOException("Could not load Velocity template for URL [" + getUrl() + "]", ex);
        }
    }

    @Override
    protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Context velocityContext = createVelocityContext(model, request, response);
        doRender(velocityContext, response);
    }

    protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return createVelocityContext(model);
    }

    protected Context createVelocityContext(Map<String, Object> model) throws Exception {
        return new VelocityContext(model);
    }

    protected void doRender(Context context, HttpServletResponse response) throws Exception {
        mergeTemplate(getTemplate(getUrl()), context, response);
    }

    protected Template getTemplate(String name) throws Exception {
        return MarketVelocityEngineHolder.getInstance().getTemplate(name, "UTF-8");
    }

    protected void mergeTemplate(Template template, Context context, HttpServletResponse response) throws Exception {

        try {
            template.merge(context, response.getWriter());
        } catch (MethodInvocationException ex) {
            // #TODO
        }
    }

}
