package com.cqabj.springboot.web;

import com.cqabj.springboot.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author fjia
 * @version V1.0 --2018/1/30-${time}
 */

public class MyApplicationContextInitializer implements
                                             ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext context) {

        String[] resourcesPath = { "config.yml", "path.yml" };
        for (String resourcePath : resourcesPath) {
            addResources(context, resourcePath);
        }
    }

    private void addResources(ConfigurableApplicationContext context, String resourcePath) {
        try {
            Resource resource = context.getResource("classpath:" + resourcePath);
            YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
            PropertySource<?> load = sourceLoader.load(resourcePath.replace(".yml", ""), resource,
                null);
            context.getEnvironment().getPropertySources().addFirst(load);
        } catch (IOException e) {
            StringUtils.outPutException(e);
        }

    }
}
