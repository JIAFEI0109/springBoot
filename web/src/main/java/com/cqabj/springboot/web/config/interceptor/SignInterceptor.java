package com.cqabj.springboot.web.config.interceptor;

import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.web.common.props.NoSignSecurityProperties;
import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;
import com.google.common.collect.Range;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 签名拦击器（servlet拦截器）
 * 拦截所有为标注的不拦截的接口,在controller执行之前进行拦截
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
public class SignInterceptor implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        boolean ifDoChain = false;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String url = String.valueOf(request.getRequestURL());
        NoSignSecurityProperties bean = getBean(NoSignSecurityProperties.class, request);
        //匹配免过滤路径
        for (String unFiltUrl : bean.getResources()) {
            if (containsUrl(url, bean.getContextPath()[0] + unFiltUrl)) {
            }
        }

    }

    @Override
    public void destroy() {

    }

    private <T> T getBean(Class<T> clazz, HttpServletRequest request) {
        return WebApplicationContextUtils
            .getRequiredWebApplicationContext(request.getServletContext()).getBean(clazz);
    }

    private boolean containsUrl(String url, String targetUrl) {
        //根节点匹配规则
        if (StringUtils.contains(targetUrl, IGlobalConstant.ALL_EXEMPTED)) {
            String subTarget = CharMatcher.anyOf("/**").removeFrom(targetUrl);
            if (subTarget.length() > url.length()
                || subTarget.length() < url.length() && url.charAt(subTarget.length()) != '/') {
                return false;
            }
            String subUrl = StringUtils.substring(url, 0, subTarget.length());

            return subUrl.equals(subTarget);

        }
        return targetUrl.equals(url);
    }
}
