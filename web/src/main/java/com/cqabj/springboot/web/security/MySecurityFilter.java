package com.cqabj.springboot.web.security;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * url拦截器
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class MySecurityFilter extends AbstractSecurityInterceptor implements Filter {

    private FilterInvocationSecurityMetadataSource securityMetadataSource;

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.securityMetadataSource;
    }

    @Override
    public Class<FilterInvocation> getSecureObjectClass() {
        //下面的MyAccessDecisionManager的supports方面必须返回true,否则会提醒类型错误
        return FilterInvocation.class;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        //暂时不需要初始化
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        log.debug("<==[{} - doFilter]==>开始过滤", this.getClass());
        FilterInvocation filter = new FilterInvocation(request, response, filterChain);
        invoke(filter);
    }

    private void invoke(FilterInvocation filter) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(filter);
        try {
            filter.getChain().doFilter(filter.getRequest(), filter.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {
        //暂时不需要销毁
    }
}
