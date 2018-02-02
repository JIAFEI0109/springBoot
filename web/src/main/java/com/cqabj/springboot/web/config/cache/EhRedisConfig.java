package com.cqabj.springboot.web.config.cache;

import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.web.common.EhRedisCache;
import net.sf.ehcache.Ehcache;
import org.aspectj.weaver.tools.cache.SimpleCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 开启EnableCaching注解
 * redis和ehcache缓存方法
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Configuration
@EnableCaching(proxyTargetClass = true)
public class EhRedisConfig extends CachingConfigurerSupport {
    @Resource
    private RedisTemplate<Object, Object> redisTemplate;
    @Resource
    private Ehcache                       ehcache;
    /**
     * 是否激活redis服务
     */
    @Value("${spring.redis.service-on}")
    private boolean                       serviceOn;

    /**
     * 构造spring缓存管理器
     * @return 缓存管理器
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        //简单管理器
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<EhRedisCache> caches = new ArrayList<>();
        caches.add(ehRedisCache());
        //可以管理cache多实例
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    public EhRedisCache ehRedisCache() {
        EhRedisCache redisCache = new EhRedisCache();
        //设置ehcache
        redisCache.setEhcache(ehcache);
        //判断redis是否开启
        if (serviceOn) {
            //判断是否存活
            isAvailable();
            //设置redis模板
            redisCache.setRedisTemplate(redisTemplate);
            redisCache.setUseRedis(serviceOn);
        }
        //设置redisCache名称
        redisCache.setName(IGlobalConstant.DEFAULT_CACHE_NAME);
        return redisCache;
    }

    /**
     * 通过redisTemplate来查询信息。
     * 来判断redis服务是否可用
     */
    private void isAvailable() {
        redisTemplate.getClientList();
    }

}
