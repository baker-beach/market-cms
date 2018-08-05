package com.bakerbeach.market.cms.view.velocity;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ExtProperties;
import org.springframework.util.StringUtils;

public class MarketResourceLoader extends ResourceLoader {

    public static final String SPRING_RESOURCE_LOADER = "spring-resource-loader";

    protected final Log logger = LogFactory.getLog(getClass());

    private org.springframework.core.io.ResourceLoader resourceLoader;

    private String[] resourceLoaderPaths;

    @Override
    public void init(ExtProperties configuration) {
        this.resourceLoader = (org.springframework.core.io.ResourceLoader) this.rsvc.getApplicationAttribute(SPRING_RESOURCE_LOADER);
        String resourceLoaderPath = configuration.getString("path");
        if (this.resourceLoader == null) {
            throw new IllegalArgumentException("'resourceLoader' application attribute must be present for SpringResourceLoader");
        }
        if (resourceLoaderPath == null) {
            throw new IllegalArgumentException("'resourceLoaderPath' application attribute must be present for SpringResourceLoader");
        }
        this.resourceLoaderPaths = StringUtils.commaDelimitedListToStringArray(resourceLoaderPath);
        for (int i = 0; i < this.resourceLoaderPaths.length; i++) {
            String path = this.resourceLoaderPaths[i];
            if (!path.endsWith("/")) {
                this.resourceLoaderPaths[i] = path + "/";
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("SpringResourceLoader for Velocity: using resource loader [" + this.resourceLoader + "] and resource loader paths " + Arrays.asList(this.resourceLoaderPaths));
        }
    }

    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }

    @Override
    public Reader getResourceReader(String templateName, String encoding) throws ResourceNotFoundException {

        if (logger.isDebugEnabled()) {
            logger.debug("Looking for Velocity resource with name [" + templateName + "]");
        }
        for (String resourceLoaderPath : this.resourceLoaderPaths) {
            org.springframework.core.io.Resource resource = this.resourceLoader.getResource(resourceLoaderPath + templateName);
            Reader reader;
            try {
                reader = buildReader(resource.getInputStream(), encoding);
                return reader;
            } catch (IOException ex) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not find Velocity resource: " + resource);
                }
            }
        }
        throw new ResourceNotFoundException("Could not find resource [" + templateName + "] in Spring resource loader path");

    }

}
