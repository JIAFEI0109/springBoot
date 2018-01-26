package com.cqabj.springboot.web.config.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;

/**
 * 数据源
 * @author fjia
 * @version V1.0 --2018/1/24-${time}
 */
@Configuration
public class DataSourceConfiguration {

    @Primary
    @ConfigurationProperties("spring.datasource.druid")
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource() {
        DruidDataSource build = new DruidDataSource();
        //maybeGetDriverClassName(build);
        return build;
    }

    /**
     * 通过url头部节点判断出对应的数据库类型
     * @param build 数据源
     */
    private void maybeGetDriverClassName(DruidDataSource build) {
        String driverClass = build.getDriverClassName();
        String url = build.getUrl();
//        if (!StringUtils.isEmpty(driverClass)
//            && !StringUtils.isEmpty(url)) {
            //String driverClassName = DatabaseDriver.fromJdbcUrl(build.getUrl())
            String driverClassName = DatabaseDriver.fromJdbcUrl("jdbc:mysql://192.168.2.55:3306/springboot?useUnicode=true&characterEncoding=UTF-8")
                .getDriverClassName();
            if (!StringUtils.isEmpty(driverClassName)) {
                build.setDriverClassName(driverClassName);
            }
//        }
    }
}
