package com.cqabj.springboot.web.service.impl;

import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.model.exception.ProcessException;
import com.cqabj.springboot.utils.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

/**
 * 基础service提供 HttpSession和HttpServletRequest
 * @author fjia
 * @version V1.0 --2018/1/29-${time}
 */
@Slf4j
public abstract class BaseService {

    @Resource
    private HttpSession        session;
    @Resource
    private HttpServletRequest request;

    /**
     * 向session
     * @param key 键
     * @param value 值
     * @param <T> 类型
     * @return T
     */
    <T extends Serializable> T addToSession(String key, T value) {
        log.debug("<==[{} - addToSession]==>Session中存储数据:{}", this.getClass(), key);
        session.setAttribute(key, value);
        return value;
    }

    /**
     * 向request存值
     * @param key 键
     * @param value 值
     * @param <T> 类型
     * @return T
     */
    <T extends Serializable> T addToRequest(String key, T value) {
        log.debug("<==[{} - addToRequest]==>Request中存储数据:{}", this.getClass(), key);
        request.setAttribute(key, value);
        return value;
    }

    /**
     * 取当前登录用户
     */
    UserInfo getCurrentLogonInfo() {
        UserInfo userInfo = ObjectUtil
            .typeConversion(session.getAttribute(IGlobalConstant.CURRENT_USER));
        if (userInfo == null) {
            throw new ProcessException(CodeEnum.ERROR_10000);
        }
        return userInfo;
    }

    /**
     * 从session中取值
     * @param key 键
     * @return object
     */
    <T> T getFromSession(String key) {
        return ObjectUtil.typeConversion(session.getAttribute(key));
    }

    /**
     * 从request中取值
     * @param key 键
     * @return object
     */
    <T> T getFromRequest(String key) {
        return ObjectUtil.typeConversion(request.getAttribute(key));
    }
}
