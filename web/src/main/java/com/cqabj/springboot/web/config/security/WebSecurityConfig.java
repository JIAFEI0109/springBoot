package com.cqabj.springboot.web.config.security;

import com.cqabj.springboot.web.common.props.UserSecurityProperties;
import com.cqabj.springboot.web.security.AjaxAuthenticationProvider;
import com.cqabj.springboot.web.security.LogoutSuccessHandler;
import com.cqabj.springboot.web.security.MyTokenBasedRememberMeServices;
import com.cqabj.springboot.web.service.SpringSecurityService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;

/**
 * security安全框架
 * seourity默认禁止注解
 * 只有extends WebSecurityConfigurerAdapter 和@EnableGlobalMethodSecurity(prePostEnabled = true) 开起注解
 * 		prePostEnable
 * @author fjia
 * Created by cqabj on 2018/1/27.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private UserSecurityProperties securityProperties;
    @Resource
    private UserDetailsService     userDetailsService;
    @Resource
    private SpringSecurityService  springSecurityService;

    private MyTokenBasedRememberMeServices rememberMeServices() {
        MyTokenBasedRememberMeServices services = new MyTokenBasedRememberMeServices(
            securityProperties.getRemember().getKey(), userDetailsService);
        services.setCookieName(securityProperties.getRemember().getCookieName());
        services.setParameter(securityProperties.getRemember().getParamter());
        services.setSpringSecurityService(springSecurityService);
        return services;
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    private AjaxAuthenticationProvider ajaxLoginFilter() {
        return null;
    }

    /**
     *  保存了所有认证成功后用户的SessionInformation信息，
     *  每次用户访问服务器的会从sessionRegistry中查询出当前用户的session信息 ，
     *  判断是否过期以及刷新最后一次方法时间，默认的实现类SessionRegistryImpl，
     *  监听了session的销毁事件，若销毁，那么删除掉session信息
     *  有两个属性：
     *   <principal:Object,SessionIdSet>  以认证用户对象做key,多个sessionId 为value 。一个用户可以对应多个不同的session，
     *   表示同一个帐号可以同时在不同的浏览器上登录，可以配置最大允许同时登录的数
         private final ConcurrentMap<Object,Set<String>> principals = new ConcurrentHashMap<Object,Set<String>>();
        <sessionId:Object,SessionInformation> 以sessionid为key SessionInformation为value
         private final Map<String, SessionInformation> sessionIds = new ConcurrentHashMap<String, SessionInformation>();
     */
    private SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 控制登录用户session
     */
    private SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        //用于管理session
        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();
        ConcurrentSessionControlAuthenticationStrategy strategy = new ConcurrentSessionControlAuthenticationStrategy(
            sessionRegistry());
        //同时登录个数
        strategy.setMaximumSessions(1);
        //超过是否抛出异常
        strategy.setExceptionIfMaximumExceeded(false);
        delegateStrategies.add(strategy);
        //首先让原来的session过期，然后创建一个新的session，把原来session的属性拷贝到新的session中
        delegateStrategies.add(new SessionFixationProtectionStrategy());
        //用户认证成功后sessionRegistry调用registerNewSession，保存用户的信息和session
        delegateStrategies.add(new RegisterSessionAuthenticationStrategy(sessionRegistry()));
        return new CompositeSessionAuthenticationStrategy(delegateStrategies);
    }

    /**
     * web相关配置静态资源
     * @param web web
     * @throws Exception exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(securityProperties.getWebAntMatchers());
    }

    /**
     *
     * @param http http
     * @throws Exception exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //没有权限异常拦截向/authExp 跳转
        http.exceptionHandling().accessDeniedPage("/authExp")
            //除了error其他所有访问都具有USER权限
            .and().authorizeRequests().antMatchers("/error").permitAll().antMatchers("/**")
            .hasRole("USER")
            //允许来自/logon的请求
            .and().formLogin().loginPage("/logon").permitAll()
            //登出,同时使session失效并添加登出成功拦截器
            .and().logout().invalidateHttpSession(true).logoutUrl("/logout")
            .logoutSuccessHandler(new LogoutSuccessHandler())
            //跨域
            .and().csrf().requireCsrfProtectionMatcher(userRequiresCsrfMatcher())
            //自动登录
            .and().rememberMe().key(securityProperties.getRemember().getKey())
            .rememberMeServices(rememberMeServices())
            //控制登录用户,及session无效后跳转
            .and().sessionManagement()
            .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
            .invalidSessionUrl("/logon");
        //添加过滤器
        //.and().addFilter(, UsernamePasswordAuthenticationFilter.class);
    }

    private RequestMatcher userRequiresCsrfMatcher() {
        UserRequiresCsrfMatcher csrfMatcher = new UserRequiresCsrfMatcher();
        csrfMatcher.setExecludeUrlsl(securityProperties.getCsrf().getExecludeUrls());
        return csrfMatcher;
    }
}
