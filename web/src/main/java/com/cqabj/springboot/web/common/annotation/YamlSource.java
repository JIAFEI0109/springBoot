package com.cqabj.springboot.web.common.annotation;

import java.lang.annotation.*;

/**
 * 用于读取yml文件
 * @author fjia
 * Created by cqabj on 2018/1/27.
 */

/* 作用为TYPE 接口、类、枚举、注解;METHOD 方法;FIELD 字段、枚举 */
@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD})
/* 注解会在class细节吗文件中存在,在运行时可以通过反射获取到 */
@Retention(RetentionPolicy.RUNTIME)
/* 说明该注解将被包含在javadoc中 */
@Documented
public @interface YamlSource {

	String path() default "";

	String prefix() default "";

	boolean ignoreResourceNotFound() default false;
}
