package com.cqabj.springboot.web;

import com.cqabj.springboot.utils.StringUtils;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @author fjia
 * @version V1.0 --2018/1/30-${time}
 */
public class MyApplicationContextInitializer implements
                                             ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        String[] resourcesPath = { "config.yml" };
        for (String resourcePath : resourcesPath) {
            addResources(context, resourcePath);
        }
    }

    private void addResources(ConfigurableApplicationContext context, String resourcePath) {
        try {
            Resource resource = context.getResource("classpath:" + resourcePath);
            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            PropertySource<?> load = sourceLoader.load("base", resource, null);
            context.getEnvironment().getPropertySources().addFirst(load);
        } catch (IOException e) {
            StringUtils.outPutException(e);
        }

    }
}
