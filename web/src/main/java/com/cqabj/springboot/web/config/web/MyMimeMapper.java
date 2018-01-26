package com.cqabj.springboot.web.config.web;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.MimeMappings;
import org.springframework.context.annotation.Configuration;

/**
 * 定义web mime mapper
 * @author fjia
 * @version V1.0 --2018/1/26-${time}
 */
@Configuration
public class MyMimeMapper implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("docx", "application/msword; charset=utf-8");
        container.setMimeMappings(mappings);
    }
}
