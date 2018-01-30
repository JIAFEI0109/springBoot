package com.cqabj.springboot.web.security;

import com.cqabj.springboot.model.common.IGlobalConstant;
import com.cqabj.springboot.utils.NetworkUtil;
import com.cqabj.springboot.web.service.SpringSecurityService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@Slf4j
public class MyTokenBasedRememberMeServices extends TokenBasedRememberMeServices {

    @Setter
    @Getter
    private SpringSecurityService springSecurityService;

    public MyTokenBasedRememberMeServices(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    /**
     * cookie自动登录
     * @param cookieTokens cookie
     * @param request 情趣
     * @param response 响应
     * @return UserDetails
     */
    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
                                                 HttpServletResponse response) {

        if (cookieTokens.length != IGlobalConstant.COOKIE_TOKENS_SIZE) {
            throw new InvalidCookieException(
                "Cookie token did not contain 3 tokens, but contained '"
                                             + Arrays.asList(cookieTokens) + "'");
        }
        long tokenExpiryTime;
        try {
            tokenExpiryTime = Long
                .parseLong(cookieTokens[IGlobalConstant.COOKIE_TOKENS_EXPIRYTIME]);
        } catch (NumberFormatException e) {
            throw new InvalidCookieException(
                "Cookie token[1] did not contain a valid number (contained '"
                                             + cookieTokens[IGlobalConstant.COOKIE_TOKENS_EXPIRYTIME]
                                             + "')");
        }
        if (isTokenExpired(tokenExpiryTime)) {
            throw new InvalidCookieException(
                "Cookie token[1] has expired (expired on '" + new Date(tokenExpiryTime)
                                             + "'; current time is '" + new Date() + "')");
        }
        UserDetails userDetails = getUserDetailsService()
            .loadUserByUsername(cookieTokens[IGlobalConstant.COOKIE_TOKENS_USERNAME]);
        String expectedTokenSignature = makeTokenSignature(tokenExpiryTime,
            userDetails.getUsername(), userDetails.getPassword(),
            NetworkUtil.getIpAddress(request));
        if (!equals(expectedTokenSignature,
            cookieTokens[IGlobalConstant.COOKIE_TOKENS_SIGNATURE])) {
            throw new InvalidCookieException("Cookie token[2] contained signature '"
                                             + cookieTokens[IGlobalConstant.COOKIE_TOKENS_SIGNATURE]
                                             + "' but expected '" + expectedTokenSignature + "'");
        }
        springSecurityService.initData(springSecurityService
            .getByName(cookieTokens[IGlobalConstant.COOKIE_TOKENS_USERNAME]));
        return userDetails;
    }

    @Override
    protected boolean isTokenExpired(long tokenExpiryTime) {
        return tokenExpiryTime < System.currentTimeMillis();
    }

    /**
     * 手动登录
     * @param request 请求
     * @param response 响应
     * @param successfulAuthentication 成功权限
     */
    @Override
    public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
                               Authentication successfulAuthentication) {

        String username = this.retrieveUserName(successfulAuthentication);
        String password = this.retrievePassword(successfulAuthentication);
        if (!StringUtils.hasLength(username)) {
            log.debug("<==[{} - onLoginSuccess]==>Unable to retrieve username", this.getClass());
            return;
        }

        if (!StringUtils.hasLength(password)) {
            UserDetails user = this.getUserDetailsService().loadUserByUsername(username);
            password = user.getPassword();
            if (!StringUtils.hasLength(password)) {
                log.debug("<==[{} - onLoginSuccess]==>Unable to obtain password for user: {}",
                    this.getClass(), username);
                return;
            }
        }

        int tokenLifeTime = calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();
        expiryTime += IGlobalConstant.SECONDS_TO_MILLISECONDS
                      * (tokenLifeTime < IGlobalConstant.COOKIE_LIFE_TIME ? TWO_WEEKS_S
                          : tokenLifeTime);
        String ipAddress = NetworkUtil.getIpAddress(request);
        String signature = makeTokenSignature(expiryTime, username, password, ipAddress);
        setCookie(new String[] { username, Long.toString(expiryTime), signature }, tokenLifeTime,
            request, response);
        log.debug("<==[{} - onLoginSuccess]==>Added remember-me cookie for user={},expiry={}",
            this.getClass(), username, new Date(expiryTime));
    }

    private String makeTokenSignature(long tokenExpiryTime, String username, String password,
                                      String ipAddress) {
        String data = username + ":" + tokenExpiryTime + ":" + password + ":" + getKey() + ":"
                      + ipAddress;
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No MD5 algorithm available!");
        }
        return Arrays.toString(Hex.encode(digest.digest(data.getBytes())));
    }

    private static boolean equals(String expected, String actual) {
        byte[] expectedBytes = bytesUtf8(expected);
        byte[] actualBytes = bytesUtf8(actual);
        if (expectedBytes == null || actualBytes == null
            || expectedBytes.length != actualBytes.length) {
            return false;
        } else {
            int result = 0;

            for (int i = 0; i < expectedBytes.length; ++i) {
                result |= expectedBytes[i] ^ actualBytes[i];
            }

            return result == 0;
        }
    }

    private static byte[] bytesUtf8(String s) {
        return s == null ? null : Utf8.encode(s);
    }

    @Override
    protected int calculateLoginLifetime(HttpServletRequest request,
                                         Authentication authentication) {
        return getTokenValiditySeconds();
    }

    @Override
    protected String retrieveUserName(Authentication authentication) {
        if (isMyInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            if (authentication.getPrincipal() == null) {
                return null;
            }
        }
        return authentication.getCredentials().toString();
    }

    @Override
    protected String retrievePassword(Authentication authentication) {
        if (isMyInstanceOfUserDetails(authentication)) {
            return ((UserDetails) authentication.getPrincipal()).getPassword();
        } else {
            return authentication.getCredentials() == null ? null
                : authentication.getCredentials().toString();
        }
    }

    private boolean isMyInstanceOfUserDetails(Authentication authentication) {
        return authentication.getPrincipal() instanceof UserDetails;
    }

}
