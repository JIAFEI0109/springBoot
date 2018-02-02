package com.cqabj.springboot.web.config.cache;

import com.cqabj.springboot.web.common.props.RedisConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis的配置器
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Slf4j
@Configuration
public class RedisConfig {

    /**
     * 构造jedisPoolConfig
     */
    @Bean
    public JedisPoolConfig getRedisConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public JedisConnectionFactory getConnectioonFactory(RedisConfigProperties redisConfig) {
        //jedis连接工厂
        JedisConnectionFactory factory = new JedisConnectionFactory();
        //添加连接地址、端口、密码等
        factory.setPoolConfig(getRedisConfig());
        factory.setHostName(redisConfig.getHost());
        factory.setPort(redisConfig.getPort());
        if (StringUtils.isNotEmpty(redisConfig.getPassword())) {
            factory.setPassword(redisConfig.getPassword().trim());
        }
        factory.setDatabase(redisConfig.getDatabase());
        //是否使用连接池
        factory.setUsePool(true);
        log.debug("JedisConnectionFactory bean init success.");
        return factory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConfigProperties redisConfig) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        //key String模式
        template.setKeySerializer(getStringRedisSerializer());
        //value jackson序列化对象
        template.setValueSerializer(genericJackson2JsonRedisSerializer());
        template.setConnectionFactory(getConnectioonFactory(redisConfig));
        //主动触发设置成功后方法
        template.afterPropertiesSet();
        return template;
    }

    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    private StringRedisSerializer getStringRedisSerializer() {
        return new StringRedisSerializer();
    }
}
