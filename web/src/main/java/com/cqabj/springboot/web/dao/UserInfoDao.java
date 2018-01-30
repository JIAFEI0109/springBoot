package com.cqabj.springboot.web.dao;


import com.cqabj.springboot.model.entity.UserInfo;

/**
 * 用户基础信息接口
 * @author fjia
 * @version V1.0 --2018/1/29-${time}
 */
public interface UserInfoDao extends CrudDao {

	/**
	 * 通过id来获取UserInfo相关信息
	 * @param userName 用户id
	 * @return UserInfo
	 */
	UserInfo getUserInfoByName(String userName);
}
