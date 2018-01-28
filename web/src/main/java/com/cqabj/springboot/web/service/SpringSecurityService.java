package com.cqabj.springboot.web.service;

import com.cqabj.springboot.model.entity.UserInfo;

/**
 * 系统权限配置service
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
public interface SpringSecurityService {

	/**
	 * 根据登录名获取系统用户登录信息（不包含权限信息）
	 * @param userName 用户名
	 * @return UserInfo 用户信息
	 */
	UserInfo getByNameWithNoAuth(String userName);

	/**
	 * 验证用户有效性
	 * @param user 用户信息
	 */
	void validateUser(UserInfo user);

	/**
	 * 初始化用户权限数据
	 * @param user 用户信息
	 */
	void initData(UserInfo user);
}
