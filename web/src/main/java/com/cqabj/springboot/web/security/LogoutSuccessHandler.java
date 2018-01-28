package com.cqabj.springboot.web.security;

import com.cqabj.springboot.model.common.ResultInfo;
import com.cqabj.springboot.utils.NetworkUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Ajax 登出成功拦截器
 *
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
								Authentication authentication) throws IOException, ServletException {
		super.onLogoutSuccess(request, response, authentication);
		NetworkUtil.responseJSONMsg(response, ResultInfo.createSuccessResult("登出成功"));
	}
}
