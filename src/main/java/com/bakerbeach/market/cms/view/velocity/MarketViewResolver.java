package com.bakerbeach.market.cms.view.velocity;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

public class MarketViewResolver extends AbstractTemplateViewResolver {

    private String defaultTemplate;

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        try {
            return super.resolveViewName(viewName, locale);
        } catch (Exception e) {
            return super.resolveViewName(defaultTemplate, locale);
        }
    }

    public String getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public MarketViewResolver() {
        setViewClass(requiredViewClass());
    }

    @Override
    protected Class<?> requiredViewClass() {
        return MarketVelocityView.class;
    }

}
