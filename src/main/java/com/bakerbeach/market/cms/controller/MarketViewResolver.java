package com.bakerbeach.market.cms.controller;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

public class MarketViewResolver extends VelocityViewResolver {

    private String defaultTemplate;

    @Override
    public View resolveViewName(String viewName, Locale locale) throws Exception {
        try {
            return super.resolveViewName(viewName, locale);
        } catch (Exception e) {
            return super.resolveViewName(defaultTemplate, locale);
        }
    }

    /**
     * @return the defaultTemplate
     */
    public String getDefaultTemplate() {
        return defaultTemplate;
    }

    /**
     * @param defaultTemplate the defaultTemplate to set
     */
    public void setDefaultTemplate(String defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

}
