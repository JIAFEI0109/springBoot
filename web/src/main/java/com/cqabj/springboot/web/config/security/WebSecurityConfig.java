package com.cqabj.springboot.web.config.security;

import com.cqabj.springboot.web.common.props.UserSecurityProperties;
import com.cqabj.springboot.web.security.AjaxAuthenticationFailureHandler;
import com.cqabj.springboot.web.security.AjaxAuthenticationProvider;
import com.cqabj.springboot.web.security.LogoutSuccessHandler;
import com.cqabj.springboot.web.security.MyAccessDecisionManager;
import com.cqabj.springboot.web.security.MySecurityFilter;
import com.cqabj.springboot.web.security.MySecurityMetadataSource;
import com.cqabj.springboot.web.security.MyTokenBasedRememberMeServices;
import com.cqabj.springboot.web.service.SpringSecurityService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.springframework.security.web.util.matcher.RequestMatcher;
import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

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
    private UserSecurityProperties           userSecurityProperties;
    @Resource
    private UserDetailsService               userDetailsService;
    @Resource
    private SpringSecurityService            springSecurityService;
    @Resource
    private AjaxAuthenticationFailureHandler ajaxFailureHandler;
    @Resource
    private AuthenticationSuccessHandler     ajaxSuccessHandler;
    @Resource
    private MySecurityMetadataSource         mySecurityMetadataSource;
    @Resource
    private MyAccessDecisionManager          myAccessDecisionManager;


    private MyTokenBasedRememberMeServices rememberMeServices() {
        MyTokenBasedRememberMeServices services = new MyTokenBasedRememberMeServices(
            userSecurityProperties.getRemember().getKey(), userDetailsService);
        services.setCookieName(userSecurityProperties.getRemember().getCookieName());
        services.setParameter(userSecurityProperties.getRemember().getParameter());
        services.setSpringSecurityService(springSecurityService);
        return services;
    }

    private RequestMatcher userRequiresCsrfMatcher() {
        UserRequiresCsrfMatcher csrfMatcher = new UserRequiresCsrfMatcher();
        csrfMatcher.setExecludeUrlsl(userSecurityProperties.getCsrf().getExecludeUrls());
        return csrfMatcher;
    }

    private Filter mySecurityFilter() throws Exception {
        MySecurityFilter filter = new MySecurityFilter();
        //身份验证的主要策略设置接口
        filter.setAuthenticationManager(authenticationManager());
        //验证是否需要权限访问
        //并将访问的url需要的权限返回
        filter.setSecurityMetadataSource(mySecurityMetadataSource);
        //授权访问(判断是否有权限访问)
        filter.setAccessDecisionManager(myAccessDecisionManager);
        return filter;
    }

    private AjaxAuthenticationProvider ajaxLoginFilter() throws Exception {
        //登录处理拦截器
        AjaxAuthenticationProvider provider = new AjaxAuthenticationProvider(springSecurityService);
        //身份验证的主要策略设置接口
        provider.setAuthenticationManager(authenticationManager());
        //登录失败处理拦截器
        provider.setAuthenticationFailureHandler(ajaxFailureHandler);
        //登录成功处理拦截器
        provider.setAuthenticationSuccessHandler(ajaxSuccessHandler);
        //设置cookieToken,下次就直接登录
        provider.setRememberMeServices(rememberMeServices());
        return provider;
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
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(userSecurityProperties.getWebAntMatchers());
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
            .and().rememberMe().key(userSecurityProperties.getRemember().getKey())
            .rememberMeServices(rememberMeServices())
            //控制登录用户,及session无效后跳转
            .and().sessionManagement()
            .sessionAuthenticationStrategy(sessionAuthenticationStrategy())
            .invalidSessionUrl("/logon")
            //添加过滤器
            .and().addFilterAt(ajaxLoginFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(mySecurityFilter(), FilterSecurityInterceptor.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(userDetailsService);
    }

}
