package com.cqabj.springboot.web.common;

import com.cqabj.springboot.utils.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

/**
 * 两级缓存
 * 一级：ehcache
 * 二级：redisCache
 *
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Data
@Slf4j
public class EhRedisCache implements Cache {

    private String                        name;

    private Ehcache                       ehcache;

    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 1 * 60 * 60
     */
    private long                          liveTime   = 3600L;
    /**
     * 是否重新激活存放在redis中的时间
     */
    private boolean                       isKeepLive = true;
    /**
     * 是否激活redis存储服务
     * 默认不激活
     */
    private boolean                       isUseRedis = false;

    @Override
    public Object getNativeCache() {
        return this;
    }

    /**
     * 获取缓存中的值
     *
     * @param key 键
     * @return valueWrapper
     */
    @Override
    public ValueWrapper get(Object key) {
        Element value = ehcache.get(key);
        log.debug("Cache L1 (ehcache) :{}={}", key, value);
        //一级缓存有值，就直接返回
        if (value != null) {
            return new SimpleValueWrapper(value.getObjectValue());
        }
        //没有值就到二级缓存查询
        Object objectValue = getFromRedis(key.toString(), isKeepLive);
        //取出来之后缓存到本地
        ehcache.put(new Element(key, objectValue));
        log.debug("Cache L2 (redis) : {}={}", key, objectValue);
        return (objectValue != null ? new SimpleValueWrapper(objectValue) : null);
    }

    /**
     * 获取缓存中的值
     *
     * @param key 键
     * @param <T> 取值
     * @return T
     */
    public <T> T getValue(Object key) {
        ValueWrapper wrapper = get(key);
        if (wrapper == null) {
            return null;
        }
        return ObjectUtil.typeConversion(wrapper.get());
    }

    /**
     * 从redis中获取数据
     *
     * @param key        键
     * @param isKeepLive 是否需要更新村后时间 取默认值 3600m
     */
    public <T> T getFromRedis(final String key, final boolean isKeepLive) {
        return redisTemplate.execute(new RedisCallback<T>() {
            @Override
            public T doInRedis(RedisConnection connection) {
                byte[] keyBytes = key.getBytes();
                if (ArrayUtils.isEmpty(keyBytes)) {
                    return null;
                }
                byte[] value = connection.get(keyBytes);
                if (ArrayUtils.isEmpty(value)) {
                    return null;
                }

                if (isKeepLive && liveTime > 0) {
                    connection.expire(keyBytes, liveTime);
                }
                T result = null;
                try {
                    result = ObjectUtil.typeConversion(toObject(value));
                } catch (IOException | ClassNotFoundException e) {
                    log.error(e.getMessage());
                }
                return result;
            }
        }, true);
    }

    /**
     * 存放到缓存中(redis没有存活时间限制)
     * @param key   键
     * @param value 值
     */
    @Override
    public void put(Object key, Object value) {
        ehcache.put(new Element(key, value));
        if (isUseRedis) {
            putInRedis(key, value, null);
        }
    }

    /**
     * 存放到缓存中
     * @param key 键
     * @param value 值
     * @param objLiveTime 存活时间(redis)
     */
    public void put(Object key, Object value, Long objLiveTime) {
        ehcache.put(new Element(key, value));
        if (isUseRedis) {
            putInRedis(key, value, objLiveTime);
        }
    }

    /**
     * 清除对应的缓存
     * @param key 键
     */
    @Override
    public void evict(Object key) {
        ehcache.remove(key);
        if (isUseRedis) {
            evictRedis(key);
        }
    }

    @Override
    public void clear() {
        clearEhcache();
        if (isUseRedis) {
            clearRedis();
        }
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object o, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    /**
     * 清空redis
     */
    public void clearRedis() {
        redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) {
                connection.flushDb();
                return "clear done.";
            }
        }, true);
    }

    /**
     * 清除在redis中的缓存
     * @param key 键
     */
    public void evictRedis(final Object key) {
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) {
                return connection.del(key.toString().getBytes());
            }
        }, true);
    }

    /**
     * 清空ehcache
     */
    public void clearEhcache() {
        ehcache.removeAll();
    }

    /**
     * 存放到redis中
     *
     * @param key      键
     * @param value    值
     * @param objLiveTime 存活时间
     */
    public Long putInRedis(final Object key, final Object value, final Long objLiveTime) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) {
                byte[] keyBytes = key.toString().getBytes();
                byte[] valueBytes = toByteArray(value);
                connection.set(keyBytes, valueBytes);
                long tempTime = objLiveTime != null ? objLiveTime : liveTime;
                if (tempTime > 0) {
                    connection.expire(keyBytes, tempTime);
                }
                return 1L;
            }
        }, true);
    }

    /**
     * Object转byte[]
     *
     * @param value 对象
     * @return 二进制
     */
    private byte[] toByteArray(Object value) {
        byte[] bytes = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(value);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return bytes;
    }

    private Object toObject(byte[] value) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(value);
                ObjectInputStream ois = new ObjectInputStream(bis)) {
            return ois.readObject();
        }
    }

}
