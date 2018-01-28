package com.cqabj.springboot.web.common.utils;

import com.cqabj.springboot.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * 将yaml文件加载Environment
 * @author fjia
 * Created by cqabj on 2018/1/27.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YmalFileApplicationContextInitializer implements ApplicationContextInitializer {

	private String path;

	private String prefix;


	@Override
	public void initialize(ConfigurableApplicationContext context) {
		try {
			Resource resource = context.getResource(path);
			YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
			PropertySource<?> load = sourceLoader.load(path, resource, null);
			context.getEnvironment().getPropertySources().addFirst(load);
		} catch (IOException e) {
			StringUtils.outPutException(e);
		}

	}
}
