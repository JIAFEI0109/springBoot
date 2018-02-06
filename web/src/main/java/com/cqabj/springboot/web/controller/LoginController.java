package com.cqabj.springboot.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录接口
 * @author fjia
 * @version V1.0 --2018/2/6-${time}
 */
@Slf4j
@RestController
@AllArgsConstructor
@Api(tags = "LoginController")
public class LoginController {

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录验证", notes = "登录验证功能")
    @ApiImplicitParams(value = { @ApiImplicitParam(name = "loginName", value = "登录用户名", required = true, paramType = "query", dataType = "String"),
                                 @ApiImplicitParam(name = "loginPwd", value = "登录密码", required = true, paramType = "query", dataType = "String"),
                                 @ApiImplicitParam(name = "loginType", value = "登录类型", required = true, paramType = "query", dataType = "String") })
    public void login(HttpServletRequest request, HttpServletResponse response) {
        //调用的是spring security自身的登录方法
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "登出", notes = "登出功能")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        //调用的是spring security自身的登出方法
    }
}
