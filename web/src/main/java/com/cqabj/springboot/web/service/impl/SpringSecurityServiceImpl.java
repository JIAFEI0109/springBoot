package com.cqabj.springboot.web.service.impl;

import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.web.dao.UserInfoDao;
import com.cqabj.springboot.web.service.SpringSecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 权限验证
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SpringSecurityServiceImpl extends BaseService implements SpringSecurityService {

    private UserInfoDao userInfoDao;


    @Override
    public UserInfo getByName(String userName) {
        return userInfoDao.getUserInfoByName(userName);
    }

    @Override
    public void validateUser(UserInfo user) {

    }

    @Override
    public void initData(UserInfo user) {
        
    }
}
