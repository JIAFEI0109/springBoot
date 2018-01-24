package com.cqabj.springboot.web.config.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.LogFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fjia
 * @version V1.0 --2018/1/24-${time}
 */
@Configuration
public class FiltersConfig {

    /**
     * Druid内置提供了四种LogFilter（Log4jFilter、Log4j2Filter、CommonsLogFilter、Slf4jLogFilter
     * ），用于输出JDBC执行的日志。这些Filter都是Filter-Chain扩展机制中的Filter
     */
    @Bean
    public LogFilter logFilter() {
        Slf4jLogFilter logFilter = new Slf4jLogFilter();
        //不开启jdbc日志
        logFilter.setStatementExecutableSqlLogEnable(false);
        return logFilter;
    }

    /**
     * Druid内置提供一个StatFilter，用于统计监控信息。
     * 比如：面sql 可提供一个web页面
     */
    @Bean
    public Filter statFilter() {
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(10000);
        statFilter.setLogSlowSql(true);
        return statFilter;
    }

    /**
     * 防御sql注入的filter:wall
     */
    @Bean
    public Filter wallFileter() {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setDbType("mysql");
        wallFilter.setConfig(wallConfig());
        return wallFilter;
    }

    /**
     * META-INF/druid/wall/mysql是给定的目录读取文件
     */
    private WallConfig wallConfig() {
        WallConfig wallConfig = new WallConfig();
        wallConfig.setDir("META-INF/druid/wall/mysql");
        return wallConfig;
    }
}
