package com.cqabj.springboot.web.service.impl;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.model.entity.SysResources;
import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.model.exception.ProcessException;
import com.cqabj.springboot.web.common.EhRedisCache;
import com.cqabj.springboot.web.dao.SysResourceDao;
import com.cqabj.springboot.web.dao.UserInfoDao;
import com.cqabj.springboot.web.service.EhCacheService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.core.env.Environment;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存实现类
 * @author fjia
 * @version V1.0 --2018/2/5-${time}
 */
@Slf4j
@Service
@AllArgsConstructor
public class EhCacheServiceImpl extends BaseService implements EhCacheService {

    private Environment    env;
    private UserInfoDao    userInfoDao;
    private EhRedisCache   ehRedisCache;
    private SysResourceDao sysResourceDao;

    static final String    URL_START = "..";

    @Override
    public void refrushCache() {
        ehRedisCache.clearEhcache();
    }

    @Override
    public void refrushCache(String functionName) {
        List keys = ehRedisCache.getEhcache().getKeys();
        //遍历cacheKey集合,删除缓存数据
        for (Object key : keys) {
            String cacheKey = String.valueOf(key);
            if (cacheKey.contains(functionName)) {
                ehRedisCache.evict(cacheKey);
                log.debug("<==[{} - refreshCache]==>删除缓存: {}", this.getClass(), cacheKey);
            }
        }

    }

    @Override
    public Map<String, String> getAllCacheInfos() {
        Map<String, String> cacheMap = new HashMap<>();
        List keys = ehRedisCache.getEhcache().getKeys();
        //遍历cahceKey集合,获取缓存数据
        for (Object key : keys) {
            String cacheKey = String.valueOf(key);
            Object obj = ehRedisCache.get(cacheKey);
            if (obj != null) {
                cacheMap.put(cacheKey, String.valueOf(obj));
            }
        }
        return cacheMap;
    }

    @Override
    public Map<String, Collection<ConfigAttribute>> getAllResources() {
        Map<String, Collection<ConfigAttribute>> map = new HashMap<>();
        //获取所有资源
        List<SysResources> sysResourcesList = sysResourceDao.finAll(SysResources.class);
        for (SysResources sysResources : sysResourcesList) {
            Collection<ConfigAttribute> configAttributes = new ArrayList<>();
            //以权限名封装为Spring的Security Object
            SecurityConfig config = new SecurityConfig(
                "ROLE_" + sysResources.getResourceType() + "_" + sysResources.getResourceId() + "_"
                                                       + sysResources.getResourceName());
            configAttributes.add(config);
            map.put(sysResources.getInterfacePath(), configAttributes);
        }
        return map;
    }

    @Override
    public Map<String, Collection<ConfigAttribute>> getClinentAllResources() {
        String contextPath = env.getProperty("server.contextPath");
        Map<String, Collection<ConfigAttribute>> map = new HashMap<>();
        //获取所有资源
        List<SysResources> sysResourcesList = sysResourceDao.getClientAllResources();
        for (SysResources sysResources : sysResourcesList) {
            Collection<ConfigAttribute> configAttributes = new ArrayList<>();
            //以权限名封装为Spring的security object
            SecurityConfig config = new SecurityConfig(
                "ROLE_" + sysResources.getResourceType() + "_" + sysResources.getResourceId() + "_"
                                                       + sysResources.getResourceName());
            configAttributes.add(config);
            map.put(contextPath + sysResources.getInterfacePath(), configAttributes);
        }
        return map;
    }

    @Override
    public Set<GrantedAuthority> getAuthorities(UserInfo user) {
        //Set去重
        Set<GrantedAuthority> autSet = new HashSet<>();
        log.debug("<==[user]==>{}", user);
        //这里要查数据库,由于在rememebr me时 要先调用此方法 然而当前登录用户还没有初始化权限信息而报错
        List<SysResources> sysResourcesList = sysResourceDao.getResourcesByUid(user.getUId());
        for (SysResources sysResources : sysResourcesList) {
            autSet.add(new SimpleGrantedAuthority(
                "ROLE_" + sysResources.getResourceType() + "_" + sysResources.getResourceId() + "_"
                                                  + sysResources.getResourceName()));
        }
        //给所有登录用户自动增加ROLE_USER权限
        autSet.add(new SimpleGrantedAuthority("ROLE_USER"));
        return autSet;
    }

    @Override
    public boolean checkAuth(HttpServletRequest request, Long uId) {
        Map<String, Collection<ConfigAttribute>> resourceMap = getClinentAllResources();
        String requestURL = request.getRequestURL().toString();
        if (requestURL.startsWith(URL_START)) {
            requestURL = requestURL.substring(2, requestURL.length());
        }
        Collection<ConfigAttribute> configCollection = null;
        //检查请求与当前资源匹配的正确性
        for (Map.Entry<String, Collection<ConfigAttribute>> entry : resourceMap.entrySet()) {
            boolean flag = com.cqabj.springboot.utils.StringUtils.match(entry.getKey(), requestURL);
            log.debug("<==[{} - checkAuthority]==>{}-->{}: {}", this.getClass(), requestURL,
                entry.getKey(), flag);
            if (flag) {
                configCollection = entry.getValue();
                //找到匹配的第一个url之后,跳出循环
                break;
            }
        }
        //没有匹配到则放行
        if (configCollection == null) {
            return false;
        }
        log.debug("<==[{} - checkAuthority]==>menuUrl: {}", this.getClass(), requestURL);
        UserInfo user = userInfoDao.getUserInfoByUID(uId);
        if (user == null) {
            throw new ProcessException(CodeEnum.ERROR_20002);
        }
        //存入当前用户
        request.setAttribute(IGlobalConstant.CURRENT_USER, user);
        Collection<GrantedAuthority> grantedAuthority = getAuthorities(user);
        for (ConfigAttribute configAttribute : configCollection) {
            //访问所请求资源需要的权限
            String needPermission = configAttribute.getAttribute();
            log.debug("<==[{} - checkAuthority]==>needPermission is: {}", this.getClass(),
                needPermission);
            //用户所拥有的权限authentication
            for (GrantedAuthority authority : grantedAuthority) {
                if (needPermission.equals(authority.getAuthority())){
                    return true;
                }
            }
        }
        return false;
    }
}
