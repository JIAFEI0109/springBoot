package com.cqabj.springboot.web.config.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.util.DruidDataSourceUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源
 * @author fjia
 * @version V1.0 --2018/1/24-${time}
 */
@Configuration
public class DataSourceConfiguration {

    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(Environment env, LogFilter logFilter, Filter statFilter,
                                 Filter wallFilter) {
        /*
         * sql防火墙
         * druid监控：spring、service、dao调用统计
         */
        List<Filter> proxyFilters = new ArrayList<>();
        proxyFilters.add(wallFilter);
        proxyFilters.add(statFilter);
        proxyFilters.add(logFilter);

        return null;

    }
}
