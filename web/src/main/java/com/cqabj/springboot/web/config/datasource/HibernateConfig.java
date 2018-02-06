package com.cqabj.springboot.web.config.datasource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Hibernate及事物配置
 * @author fjia
 * @version V1.0 --2018/1/25-${time}
 */
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class HibernateConfig {

    @Bean(name = "sessionFactory")
    public SessionFactory sessionFactory(DataSource dataSource,
                                         Environment env) throws IOException {
        LocalSessionFactoryBean bean = new LocalSessionFactoryBean();
        bean.setDataSource(dataSource);
        Properties props = new Properties();
        props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show-sql", env.getProperty("hibernate.show-sql"));
        props.setProperty("hibernate.format-sql", env.getProperty("hibernate.format-sql"));
        props.setProperty("hibernate.use-sql-comments", env.getProperty("hibernate.use-sql-comments"));
        bean.setHibernateProperties(props);

        bean.setPhysicalNamingStrategy(PhysicalNamingStrategyStandardImpl.INSTANCE);
        bean.setPackagesToScan(StringUtils.split(env.getProperty("hibernate.packages-to-scan"), ","));
        bean.afterPropertiesSet();
        return bean.getObject();
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public HibernateTemplate hibernateTemplate(SessionFactory sessionFactory) {
        HibernateTemplate template = new HibernateTemplate();
        template.setSessionFactory(sessionFactory);
        return template;
    }
}
