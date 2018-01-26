package com.cqabj.springboot.web.config.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * 拦截器装配
 * @author fjia
 * @version V1.0 --2018/1/25-${time}
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AutoAspect {

    /**
     * 设置事物
     * @param manager 事物管理器
     * @return 事物拦截器
     */
    @Bean
    public TransactionInterceptor transactionInterceptor(HibernateTransactionManager manager) {
        TransactionInterceptor interceptor = new TransactionInterceptor();
        interceptor.setTransactionManager(manager);
        String patten = "PROPAGATION_REQUIRED,ISOLATION_DEFAULT,timeout_20,-UisRollbackException";
        //回滚为-，不回滚+
        Properties props = new Properties();
        props.setProperty("get*", patten.concat("readOnly"));
        props.setProperty("exe*", patten.concat("readOnly"));
        props.setProperty("sav*", patten);
        props.setProperty("vali*", patten);
        props.setProperty("init*", patten);
        props.setProperty("add*", patten);
        props.setProperty("do*", patten);
        props.setProperty("set*", patten);
        props.setProperty("send*", patten);
        props.setProperty("reset*", patten);
        props.setProperty("insert*", patten);
        props.setProperty("mod*", patten);
        props.setProperty("process*", patten);
        props.setProperty("updat*", patten);
        props.setProperty("check*", patten);
        props.setProperty("del*", patten);
        interceptor.setTransactionAttributes(props);
        return interceptor;
    }

}
