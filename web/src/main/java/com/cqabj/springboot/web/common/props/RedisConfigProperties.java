package com.cqabj.springboot.web.common.props;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * reid配置信息类
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Data
@ToString
@Component
@ConfigurationProperties("spring.redis")
public class RedisConfigProperties {

    private Integer    database;
    private String     host;
    private Integer    port;
    private String     password;
    private PoolEntity pool;

    @Data
    @ToString
    public static class PoolEntity {
        private Long maxIdle;
        private Long minIdle;
        private Long maxActive;
        private Long maxWait;
    }
}
