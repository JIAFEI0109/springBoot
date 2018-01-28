package com.cqabj.springboot.web.config.security;

import com.cqabj.springboot.web.common.props.UserSecurityProperties;
import com.cqabj.springboot.web.security.LogoutSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.annotation.Resource;
import java.util.Map;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	@Resource
	private UserSecurityProperties securityProperties;



	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
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
				.and().authorizeRequests().antMatchers("/error").permitAll().antMatchers("/**").hasRole("USER")
				//允许来自/logon的请求
				.and().formLogin().loginPage("/logon").permitAll()
				//登出,同时是session失效并添加登出成功拦截器
				.and().logout().invalidateHttpSession(true).logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler())
				//
				.and().csrf().requireCsrfProtectionMatcher(userRequiresCsrfMatcher())
				//
				.and().rememberMe().key(securityProperties.getRemember().getKey()).rememberMeServices(null);
	}

	private RequestMatcher userRequiresCsrfMatcher() {
		UserRequiresCsrfMatcher csrfMatcher = new UserRequiresCsrfMatcher();
		csrfMatcher.setExecludeUrlsl(securityProperties.getCsrf().getExecludeUrls());
		return csrfMatcher;
	}
}
