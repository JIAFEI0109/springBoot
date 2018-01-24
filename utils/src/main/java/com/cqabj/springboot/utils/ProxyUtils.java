package com.cqabj.springboot.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 取代理对象的原始对象
 * @author fjia
 * @version V1.0 --2018/1/23-${time}
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyUtils {

    /**
     * 通过代理对象获取原始对象
     * @param proxy 代理对象
     * @return Object 原始对象
     */
    public static Object getTargetObject(Object proxy) {
        try {
            //不是代理对象
            if (!AopUtils.isAopProxy(proxy)) {
                return proxy;
            }
            //jdk动态代理
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else {
                //cglib代理
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            log.error(StringUtils.outPutException(e));
        }
        return proxy;
    }

    /**
     * 获取cglib代理的对象
     * @param proxy 代理对象
     * @return Object
     */
    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        AdvisedSupport support = ObjectUtil.typeConversion(advised.get(dynamicAdvisedInterceptor));
        return support.getTargetSource().getTarget();
    }

    /**
     * 获取jdk动态代理的对象
     * @param proxy jdk动态代理
     * @return Object
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        //将此对象设置为可访问对象
        h.setAccessible(true);
        //返回由该字段表示的字段的值，在指定的对象上。如果该值具有原始类型，则该值将自动封装在对象中。
        //o  - 从中​​提取表示字段的值的对象
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        AdvisedSupport support = ObjectUtil.typeConversion(advised.get(aopProxy));
        return support.getTargetSource().getTarget();
    }
}
