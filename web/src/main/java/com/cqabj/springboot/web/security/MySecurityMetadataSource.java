package com.cqabj.springboot.web.security;

import com.cqabj.springboot.web.service.EhCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * 判断拦截的url是否与数据库中记录的权限URL相匹配,
 * 没有则直接通过,如有则进入用户权限验证阶段
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Slf4j
@Component("mySecurityMetadataSource")
public class MySecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Resource
    private EhCacheService ehCacheService;

    /**
     * 返回告辞请求所需要的权限,与数据库中的url进行匹配,
     * 如果匹配成功则进行权限验证,因此此方法决定url是否需要权限
     * (这里只将请求url所需要的权限取出,返回Collection<ConfigAttribute>)
     * @param obj 数据对象
     * @return config匹配
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object obj) {
        //从缓存区所有资源
        Map<String, Collection<ConfigAttribute>> resourceMap = ehCacheService.getAllResources();
        String requestUrl = ((FilterInvocation) obj).getRequestUrl();
        log.debug("<==[{} - getAttributes]==>requestUrl is {}", this.getClass(), requestUrl);
        Collection<ConfigAttribute> configAttributes = null;
        //检测请求与当前资源匹配的正确性
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
            boolean flag = requestUrl.contains(entry.getKey().trim());
            if (flag) {
                configAttributes = entry.getValue();
                break;
            }
        }
        return configAttributes;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return new HashSet<>();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
