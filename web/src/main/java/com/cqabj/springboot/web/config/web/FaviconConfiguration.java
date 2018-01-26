package com.cqabj.springboot.web.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import java.util.Collections;

/**
 * 添加项目标识
 * @author fjia
 * @version V1.0 --2018/1/26-${time}
 */
@Configuration
public class FaviconConfiguration {

    @Bean
    public SimpleUrlHandlerMapping faviconhandlerMapping() {
        ResourceHttpRequestHandler handler = new ResourceHttpRequestHandler();
        handler.setLocations(Collections.<Resource> singletonList(new ClassPathResource("/")));
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setOrder(Integer.MIN_VALUE);
        mapping.setUrlMap(Collections.singletonMap("static/favicon.ico", handler));
        return mapping;
    }
}
