package com.cqabj.springboot.web.security;

import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.ResultInfo;
import com.cqabj.springboot.utils.NetworkUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Component("ajaxFailureHandler")
public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {

    /**
     * Ajax登录失败,进入该方法获取失败提示数据
     * @param request http请求
     * @param response http响应
     * @param e 登录过程抛出的异常信息
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) {
        //设置返回错误编码
        CodeEnum codeEnum = CodeEnum.ERROR_10001;
        String codeMsg = e.getMessage();
        if (codeMsg.equals(CodeEnum.ERROR_10004.getMsg())) {
            codeEnum = CodeEnum.ERROR_10004;
        }
        if (codeMsg.equals(CodeEnum.ERROR_10008.getMsg())) {
            codeEnum = CodeEnum.ERROR_10008;
        }

        NetworkUtil.responseJSONMsg(response, ResultInfo.createResult(codeEnum));
    }
}
