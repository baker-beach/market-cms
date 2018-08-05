package com.bakerbeach.market.cms.view.velocity;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

public class MarketVelocityEngine extends VelocityEngine implements ResourceLoaderAware {

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.setApplicationAttribute(MarketResourceLoader.SPRING_RESOURCE_LOADER, resourceLoader);
    }

}
