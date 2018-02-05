package com.cqabj.springboot.web.service;

import com.cqabj.springboot.model.entity.UserInfo;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 缓存专用service 该service下所有方法都走缓存
 * @author fjia
 * @version V1.0 --2018/2/5-${time}
 */
public interface EhCacheService {

    /**
     * 删除所有缓存
     */
    void refrushCache();

    /**
     * 根据方法名删除缓存(缓存的key)
     * @param functionName 方法名
     */
    void refrushCache(String functionName);

    /**
     * 获取当前缓存中cache key值
     * @return Map<String, String>
     */
    Map<String, String> getAllCacheInfos();

    /**
     * 获取资源与权限关系
     * @return Map<String,Collection<ConfigAttribute>>
     */
    Map<String, Collection<ConfigAttribute>> getAllResources();

    /**
     * 获取客户端资源与权限
     * @return Map<String,Collection<ConfigAttribute>>
     */
    Map<String, Collection<ConfigAttribute>> getClinentAllResources();

    /**
     * 获取用户的角色权限
     * @param user 用户信息
     * @return Set<GrantedAuthority>
     */
    Set<GrantedAuthority> getAuthorities(UserInfo user);

    /**
     * 判断当前用户是否具有访问路径的权限
     * @param request http请求
     * @param uId 用户uId
     * @return boolean
     */
    boolean checkAuth(HttpServletRequest request, Long uId);

}
