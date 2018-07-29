package com.bakerbeach.market.cms.handler;

import java.io.IOException;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bakerbeach.market.cms.service.PageService;
import com.bakerbeach.market.cms.service.UrlService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CmsCacheHandler {
    final Logger log = LoggerFactory.getLogger(CmsCacheHandler.class);

    @Autowired
    private UrlService urlService;

    @Autowired
    private PageService pageService;

    public void clearCache(Exchange ex) {
        try {
            urlService.clearCache();
            pageService.clearCache();
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> getPayload(Message message) throws Exception {
        Map<String, Object> payload = null;
        try {
            if (message.getBody() instanceof String) {
                String payloadAsString = (String) message.getBody();
                if (payloadAsString != null && !payloadAsString.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(payloadAsString, Map.class);
                    message.setBody(map);
                }
            }

            payload = (Map<String, Object>) message.getBody();
        } catch (IOException e) {
            log.error(ExceptionUtils.getStackTrace(e));
        }

        return payload;
    }

}
