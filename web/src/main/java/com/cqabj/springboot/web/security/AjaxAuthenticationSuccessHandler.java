package com.cqabj.springboot.web.security;

import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.utils.NetworkUtil;
import com.cqabj.springboot.utils.ObjectUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ajax登录成功拦截器
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Component("ajaxSuccessHandler")
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * ajax登录成功是,进入该方法获取失败提示数据
     * @param request http请求
     * @param response http响应
     * @param authentication 权限
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        UserInfo userInfo = ObjectUtil
            .typeConversion(request.getSession().getAttribute(IGlobalConstant.CURRENT_USER));
        NetworkUtil.responseJSONMsg(response, userInfo);
    }
}
