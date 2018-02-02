package com.cqabj.springboot.web.config.cache;

import com.cqabj.springboot.model.common.IGlobalConstant;
import net.sf.ehcache.Ehcache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 启动缓存
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Configuration
public class CacheConfiguration {

    @Bean
    public Ehcache ehcache(EhCacheCacheManager ehCacheCacheManager) {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(ehCacheCacheManager.getCacheManager());
        ehCacheFactoryBean.setCacheName(IGlobalConstant.DEFAULT_CACHE_NAME);
        ehCacheFactoryBean.afterPropertiesSet();
        return ehCacheFactoryBean.getObject();
    }

    /**
     * ehcache主要的管理器
     */
    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        return new EhCacheCacheManager(ehCacheFactoryBean().getObject());
    }

    /**
     * 根据shared与否设置,
     * Spring分别通过CacheManager.creat()
     * 或new CacheManager()方式来创建一个ehcache基地
     * 也就是说通过这个来设置chche的基地是这里的Spring独用,
     * 还是跟别的(如hibernate的Ehcache共享)
     */
    @Bean
    public EhCacheManagerFactoryBean ehCacheFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("config/ehcache.xml"));
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

}
