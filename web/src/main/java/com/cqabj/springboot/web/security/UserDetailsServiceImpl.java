package com.cqabj.springboot.web.security;

import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.web.service.EhCacheService;
import com.cqabj.springboot.web.service.SpringSecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 初始化当前用户权限
 * @author fjia
 * @version V1.0 --2018/2/5-${time}
 */
@Slf4j
@AllArgsConstructor
@Component("UserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private EhCacheService        ehCacheService;

    private SpringSecurityService springSecurityService;

    /**
     * 登录智叟获取当前用户信息
     * @param loginName 登录名
     * @return UserDetails 返回Security用户详情
     */
    @Override
    public UserDetails loadUserByUsername(String loginName) {
        log.debug("<==[{} - loadUserByUsername]==>获取用户信息", this.getClass());
        //获取用户信息
        UserInfo user = springSecurityService.getByName(loginName);
        if (user == null) {
            log.debug("<==[{} - loadUserByUsername]==>{}用户不存在", this.getClass(), loginName);
            throw new UsernameNotFoundException(loginName.concat("用户不存在!"));
        }
        //封装成spring security的user
        return new User(user.getUserName(), user.getLoginPwd(), ehCacheService.getAuthorities(user));
    }
}
