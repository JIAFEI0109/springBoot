package com.cqabj.springboot.web.service.impl;

import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.model.entity.SysResources;
import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.web.dao.SysResourceDao;
import com.cqabj.springboot.web.dao.UserInfoDao;
import com.cqabj.springboot.web.service.SpringSecurityService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限验证
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@Slf4j
@Service
@AllArgsConstructor
public class SpringSecurityServiceImpl extends BaseService implements SpringSecurityService {

    private UserInfoDao    userInfoDao;

    private SysResourceDao sysResourceDao;

    @Override
    public UserInfo getByName(String userName) {
        return userInfoDao.getUserInfoByName(userName);
    }

    @Override
    public void validateUser(UserInfo user) {

    }

    @Override
    public void initData(UserInfo user) {
        //获取当前用户可用资源信息
        List<SysResources> resourcesList = sysResourceDao.getResourcesByUid(user.getUId());
        if (resourcesList == null) {
            throw new AuthenticationServiceException(CodeEnum.ERROR_10008.getMsg());
        }
        //注入用户资源
        user.setSysResourcesList(resourcesList);
        //绑定用户
        addToSession(IGlobalConstant.CURRENT_USER, user);
    }
}
