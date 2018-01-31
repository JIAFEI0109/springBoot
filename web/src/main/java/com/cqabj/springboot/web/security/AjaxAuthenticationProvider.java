package com.cqabj.springboot.web.security;

import com.cqabj.springboot.model.common.CodeEnum;
import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.model.entity.UserInfo;
import com.cqabj.springboot.utils.DigestPass;
import com.cqabj.springboot.utils.NetworkUtil;
import com.cqabj.springboot.utils.ObjectUtil;
import com.cqabj.springboot.utils.StringUtils;
import com.cqabj.springboot.web.service.SpringSecurityService;
import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * spring security Ajax登录验证
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
@Slf4j
public class AjaxAuthenticationProvider extends AbstractAuthenticationProcessingFilter {

    private static final boolean  POST_ONLY = true;

    private SpringSecurityService springSecurityService;

    public AjaxAuthenticationProvider(SpringSecurityService springSecurityService) {
        super("/login");
        this.springSecurityService = springSecurityService;
    }

    /**
     * Ajax登录数据校验
     * @param request http请求
     * @param response http响应
     * @return 放回权限
     * @throws AuthenticationException 权限异常
     * @throws IOException io异常
     * @throws ServletException servlet异常
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException,
                                                                              IOException,
                                                                              ServletException {
        if (IGlobalConstant.POST_ONLY
            && !IGlobalConstant.HTTP_METHOD.equalsIgnoreCase(request.getMethod())) {
            throw new AuthenticationServiceException(
                "Authemtication method not supported: " + request.getMethod());
        }
        String username = obtainRequest(request, IGlobalConstant.LOGIN_USERNAME_PARAMETER);
        String password = obtainRequest(request, IGlobalConstant.LOGIN_PW_PARAMETER);
        String loginType = obtainRequest(request, IGlobalConstant.LONGIN_TYPE_PARAMETER);
        String agent = request.getHeader(IGlobalConstant.USER_AGENT);
        //agent为空时,默认为客户端请求
        if (agent.isEmpty()) {
            //获取客户端数据
        } else if (NetworkUtil.isIEBrowser(agent)) {
            log.debug("<==[{} - attemptAuthentication]==>处理IE传入数据", this.getClass());
            handleIERequest(request);
        }
        UserInfo userInfo = checkPwd(username, password);
        userInfo.setLoginType(loginType);
        UsernamePasswordAuthenticationToken token = new
                UsernamePasswordAuthenticationToken(userInfo.getLoginName(), userInfo.getPwd());
        //验证用户信息
        springSecurityService.validateUser(userInfo);
        //判断登陆类型:客户端/web端,客户端登录需要气囊验证,服务端无需签名验证
        if(IGlobalConstant.CLIENT_LOGIN.equals(loginType));
        return null;

    }

    /**
     * 用户和密码验证登录方式
     * @param username 用户登录名
     * @param password 用户登录密码
     * @return UserInfo
     */
    private UserInfo checkPwd(String username, String password) {
        log.debug("==[/login]==>参数: username={},密码=******", username);
        //
        UserInfo userInfo = springSecurityService.getByName(username);
        //验证登录用户信息
        if (userInfo == null) {
            throw new AuthenticationServiceException(CodeEnum.ERROR_20002.getMsg());
        }
        if (!userInfo.getPwd().equals(DigestPass.getDigestPassWord(password))) {
            throw new AuthenticationServiceException(CodeEnum.ERROR_10001.getMsg());
        }
        if (IGlobalConstant.DISABLED.equals(userInfo.getDisableFlag())) {
            throw new AuthenticationServiceException(CodeEnum.ERROR_20003.getMsg());
        }
        return userInfo;
    }

    private void handleIERequest(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            char[] buff = new char[IGlobalConstant.BUFFER_SIZE];
            int len;
            while ((len = reader.read(buff)) != IGlobalConstant.IO_READ_END_TAG) {
                builder.append(buff, IGlobalConstant.IO_READ_START_TAG, len);
            }
            List<String> params = Splitter.on(IGlobalConstant.IE_REQUEST_PARAMS_DELIMITER)
                .trimResults().omitEmptyStrings().splitToList(builder.toString());
            builder.setLength(IGlobalConstant.STRING_BUILDER);
            for (String param : params) {
                List<String> subParams = Splitter.on(IGlobalConstant.IE_REQUEST_PARAM_DELIMITER)
                    .trimResults().omitEmptyStrings().splitToList(param);
                request.setAttribute(subParams.get(IGlobalConstant.PARAM_NAME),
                    subParams.get(IGlobalConstant.PARAM_VALUE));
            }
        } catch (IOException e) {
            log.error("<==[{} - handleIERequest]==>获取IE请求数据错误: {},异常堆栈: {}", this.getClass(),
                e.getMessage(), ArrayUtils.toString(e.getStackTrace()));
        }
    }

    private String obtainRequest(HttpServletRequest request, String keyword) {
        String param = null;
        try {
            String req = request.getParameter(keyword);
            if (req == null) {
                req = ObjectUtil.typeConversion(request.getAttribute(keyword));
            }
            if (req != null) {
                param = new String(req.getBytes(IGlobalConstant.CHARSET_NAME_ISO88591),
                    IGlobalConstant.CHARSET_NAME_UTF8);
            }
        } catch (Exception e) {
            StringUtils.outPutException(e);
        }
        return param;
    }
}
