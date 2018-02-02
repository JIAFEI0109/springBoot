package com.cqabj.springboot.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 验证当前用户是否有被拦截的url的访问权限
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Slf4j
@Component("myAccessDecisionManager")
public class MyAccessDecisionManager implements AccessDecisionManager {

    /**
     * 在权限表中存在的url才会被拦截,然后在这边进行验证
     * @param authentication 权限
     * @param obj 对象数据
     * @param configAttributes 配置属性
     */
    @Override
    public void decide(Authentication authentication, Object obj,
                       Collection<ConfigAttribute> configAttributes) {
        log.debug("<==[{} - decide]==>判断用户是否具有访问该url权限", this.getClass());
        if (configAttributes == null) {
            return;
        }
        //请求资源拥有的权限(一个资源对多个权限)
        for (ConfigAttribute configAttribute : configAttributes) {
            //访问所请求资源所需要的权限
            String needPermission = configAttribute.getAttribute();
            log.debug("<==[{} - decide]==>needPermission is {}", this.getClass(), needPermission);
            //用户所有拥有的权限
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (needPermission.equals(authority.getAuthority())) {
                    return;
                }
            }
        }
        log.error("<==[{} - decide]==>没有权限访问", this.getClass());
        //没有权限抛出异常
        throw new AccessDeniedException("没有权限访问");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
