package com.cqabj.springboot.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    private static int    notFind       = -1;

    private static String unknownString = "unkonwn";

    private static final ObjectMapper MAPPER = new ObjectMapper();

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
        if (ip != null && ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String i : ips) {
                if (!(unknownString.equalsIgnoreCase(i))) {
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
        if (ip == null || ip.length() == 0 || unknownString.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client_IP");
            debugLogger(method, "Proxy-Client-IP", ip);
        }
        if (ip == null || ip.length() == 0 || unknownString.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client_IP");
            debugLogger(method, "WL-Proxy-Client-IP", ip);
        }
        if (ip == null || ip.length() == 0 || unknownString.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            debugLogger(method, "HTTP_CLIENT_IP", ip);
        }
        if (ip == null || ip.length() == 0 || unknownString.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            debugLogger(method, "HTTP_X_FORWARDED_FOR", ip);
        }
        if (ip == null || ip.length() == 0 || unknownString.equalsIgnoreCase(ip)) {
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
        if (result.contains("msie 7")) {
            result = "ie7";
        } else if (result.contains("mise 8")) {
            result = "ie8";
        } else if (result.contains("mise 9")) {
            result = "ie9";
        } else if (result.contains("mise 10")) {
            result = "ie10";
        } else if (result.contains("mise")) {
            result = "ie";
        } else if (result.contains("opera")) {
            result = "opera";
        } else if (result.contains("firefox")) {
            result = "firefox";
        } else if (result.contains("webkit")) {
            result = "firefox";
        } else if (result.contains("gecko")) {
            result = "ie11";
        } else if (result.contains("chrome")) {
            result = "chrome";
        } else {
            result = "others";
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
        return result.contains("msie") || result.contains("gecko") && result.contains("rv:11");
    }

    /**
     * 获取post请求内容（二进制格式）
     * @param request 请求
     * @return byte[]
     */
    public static byte[] getRrequestPostBytes(HttpServletRequest request) {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];

        try {
            for (int i = 0; i < contentLength;) {
                int readlen = request.getInputStream().read(buffer, i, contentLength - i);
                if (readlen == -1) {
                    break;
                }
                i += readlen;
            }
            return buffer;
        } catch (IOException e) {
            StringUtils.outPutException(e);
        }
        return buffer;
    }

    /**
     * 获取post骑牛内容
     * @param request 请求
     * @return String
     */
    public static String getRequestPostStr(HttpServletRequest request) {
        String result = "";
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
    public static void responseJSONMsg(HttpServletResponse response, Object obj){
        debugLogger("responseJSONMsg","输出数据","开始");
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            MAPPER.writeValue(response.getOutputStream(), obj);
        } catch (IOException e) {
            log.error(StringUtils.outPutException(e));
        }
    }

}
