package com.cqabj.springboot.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 获取客户端信息的工具
 * @author fjia
 * @version V1.0 --2018/1/23-${time}
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkUtil {

    private static final int          NOT_FIND       = -1;

    private static final String       UNKNOWN_STRING = "unkonwn";

    private static final int          IP_LEN         = 15;

    private static final ObjectMapper MAPPER         = new ObjectMapper();

    public enum BrowserType {
                             /**
                              * ie
                              */
                             IE("ie", "msie"),
                             /**
                              * ie7
                              */
                             IE7("ie7", "msie 7"),
                             /**
                              * ie8
                              */
                             IE8("ie8", "msie 8"),
                             /**
                              * ie9
                              */
                             IE9("ie9", "msie 9"),
                             /**
                              * ie10
                              */
                             IE10("ie10", "msie"),
                             /**
                              * ie11
                              */
                             IE11("ie11", "gecko", "rv:11"),
                             /**
                              * opera
                              */
                             OPERA("opera", "opera"),
                             /**
                              * firefox
                              */
                             FIREFOX("firefox", "firefox"),
                             /**
                              * firefox 
                              */
                             WEBKIT("webkit", "webkit"),
                             /**
                              * chrome
                              */
                             CHROME("chrome", "chrome"),
                             /**
                              * others 
                              */
                             OTHERS("others", "others");

        @Getter
        private String   name;
        @Getter
        private String[] tag;

        BrowserType(String name, String... tag) {
            this.name = name;
            this.tag = tag;
        }

        private String firstTag() {
            return this.tag[0];
        }
    }

    /**
     * 判断是否ajax
     * @param request 请求
     * @return boolean
     */
    public static boolean getRequestType(HttpServletRequest request) {
        return "xmlhttprequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    /**
     * 获取请求主机IP地址，如果通过代理进来，则透过防火墙获取真实IP地址
     * @param request 请求
     * @return String
     */
    public static String getIpAddress(HttpServletRequest request) {
        /* 请求主机IP地址,如果通过代理进来,则透过防火墙获取真实IP地址 */
        String ip = getRemoteAddr(request);
        if (ip != null && ip.length() > IP_LEN) {
            String[] ips = ip.split(",");
            for (String i : ips) {
                if (!(UNKNOWN_STRING.equalsIgnoreCase(i))) {
                    ip = i;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取请求中的ip地址
     * @param request 请求
     * @return string
     */
    private static String getRemoteAddr(HttpServletRequest request) {
        String method = "getRemoteAddr";
        String ip = request.getHeader("x-forwarded-for");
        debugLogger(method, "x-forwarded-for", ip);
        if (ip == null || ip.length() == 0 || UNKNOWN_STRING.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client_IP");
            debugLogger(method, "Proxy-Client-IP", ip);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_STRING.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client_IP");
            debugLogger(method, "WL-Proxy-Client-IP", ip);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_STRING.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            debugLogger(method, "HTTP_CLIENT_IP", ip);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_STRING.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            debugLogger(method, "HTTP_X_FORWARDED_FOR", ip);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN_STRING.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * debug日志输出
     * @param method method
     * @param key key
     * @param value value
     */
    private static void debugLogger(String method, String key, String value) {
        if (log.isDebugEnabled()) {
            log.debug("mthod;[{}]-{}-{}", method, key, value);
        }
    }

    /**
     * 获取浏览器版本信息
     * @param agent 浏览器的标识
     * @return string
     */
    public static String getBrowserName(String agent) {
        String result = agent.toLowerCase();
        if (result.contains(BrowserType.IE7.firstTag())) {
            result = BrowserType.IE7.getName();
        } else if (result.contains(BrowserType.IE8.firstTag())) {
            result = BrowserType.IE8.getName();
        } else if (result.contains(BrowserType.IE9.firstTag())) {
            result = BrowserType.IE9.getName();
        } else if (result.contains(BrowserType.IE10.firstTag())) {
            result = BrowserType.IE10.getName();
        } else if (result.contains(BrowserType.IE.firstTag())) {
            result = BrowserType.IE.getName();
        } else if (result.contains(BrowserType.OPERA.firstTag())) {
            result = BrowserType.OPERA.getName();
        } else if (result.contains(BrowserType.FIREFOX.firstTag())) {
            result = BrowserType.FIREFOX.getName();
        } else if (result.contains(BrowserType.WEBKIT.firstTag())) {
            result = BrowserType.WEBKIT.getName();
        } else if (result.contains(BrowserType.IE11.firstTag())
                   && result.contains(BrowserType.IE11.getTag()[1])) {
            result = BrowserType.IE11.getName();
        } else if (result.contains(BrowserType.CHROME.firstTag())) {
            result = BrowserType.CHROME.getName();
        } else {
            result = BrowserType.OTHERS.getName();
        }
        debugLogger("getBrowserName", "浏览器", result);
        return result;
    }

    /**
     * 判断是否属于IE浏览器
     * @param agent 浏览标识
     * @return boolean
     */
    public static boolean isIEBrowser(String agent) {
        String result = agent.toLowerCase();
        return result.contains(BrowserType.IE.firstTag())
               || result.contains(BrowserType.IE11.firstTag())
                  && result.contains(BrowserType.IE11.getTag()[1]);
    }

    /**
     * 获取post请求内容（二进制格式）
     * @param request 请求
     * @return byte[]
     */
    public static byte[] getRrequestPostBytes(HttpServletRequest request) {
        int contentLength = request.getContentLength();

        if (contentLength < 0) {
            return new byte[0];
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            int readlen;
            while ((readlen = request.getInputStream().read()) != NOT_FIND) {
                buffer.write(readlen);
            }
        } catch (IOException e) {
            log.error(StringUtils.outPutException(e));
        }
        return buffer.toByteArray();
    }

    /**
     * 获取post骑牛内容
     * @param request 请求
     * @return String
     */
    public static String getRequestPostStr(HttpServletRequest request) {
        String result = null;
        byte[] buffer = getRrequestPostBytes(request);
        if (buffer == null) {
            return null;
        }
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null) {
            charEncoding = "UTF-8";
        }

        try {
            result = new String(buffer, charEncoding);
        } catch (UnsupportedEncodingException e) {
            StringUtils.outPutException(e);
        }
        return result;
    }

    /**
     * 输出对应json信息
     */
    public static void responseJSONMsg(HttpServletResponse response, Object obj) {
        debugLogger("responseJSONMsg", "输出数据", "开始");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            MAPPER.writeValue(response.getOutputStream(), obj);
        } catch (IOException e) {
            log.error(StringUtils.outPutException(e));
        }
    }

}
