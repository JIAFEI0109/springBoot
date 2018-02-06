package com.cqabj.springboot.web.controller;

import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.ResultInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@ApiIgnore
@Controller
public class BaseController {


    /**
     * swagger跳转
     */
    @GetMapping(value = "/api")
    public String api() {
        return "redirect:swagger-ui.html";
    }

    /**
     * swagger跳转
     */
    @GetMapping(value = "/db")
    public String db(){
        return "redirect:druid";
    }


    @GetMapping(value = "/authExp")
    public ResultInfo<String> authExp() {
        return ResultInfo.createResult(CodeEnum.ERROR_20001);
    }

    /**
     * 未登录用户访问URL进入此方法
     */
    @PostMapping(value = "/logon")
    public ResultInfo<String> logon() {
        return ResultInfo.createResult(CodeEnum.ERROR_10000);
    }

}
