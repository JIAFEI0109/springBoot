package com.cqabj.springboot.web.config.interceptor;

import com.cqabj.springboot.utils.StringUtils;
import com.cqabj.springboot.web.common.annotation.YamlSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 用于读取yml文件
 * @author fjia
 * Created by cqabj on 2018/1/27.
 */
@Aspect
@Order(1)
@Component
public class YamlSourceinterceptor {

	@Resource
	private ConfigurableApplicationContext context;

	@Before(value = "@annotation(com.cqabj.springboot.web.*)")
	public void doBefore(JoinPoint joinPoint){
		Object target = joinPoint.getTarget();
		YamlSource yamlSource = target.getClass().getAnnotation(YamlSource.class);
		String path = yamlSource.path();
		String prefix = yamlSource.prefix();

		try {
			org.springframework.core.io.Resource resource = context.getResource(path);
			YamlPropertySourceLoader sourceLoader = new YamlPropertySourceLoader();
			PropertySource<?> load = sourceLoader.load(prefix, resource, null);
			context.getEnvironment().getPropertySources().addFirst(load);
		} catch (IOException e) {
			StringUtils.outPutException(e);
		}
	}

	/* YmalFileApplicationContextInitializer */
}
