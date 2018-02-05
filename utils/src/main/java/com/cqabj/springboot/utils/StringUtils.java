package com.cqabj.springboot.utils;

import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Properties;

/**
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@Slf4j
public class StringUtils {

    /**
     * 打印异常的堆栈信息
     * @param e 异常
     * @return String 异常信息
     */
    public static String outPutException(Exception e) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(buf, true));
        String expMessage = buf.toString();
        return expMessage;
    }

    /**
     * 打印异常的堆栈信息
     * @param e 异常
     * @return String 异常信息
     */
    public static String outPutException(Throwable e) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        e.printStackTrace(new PrintWriter(buf, true));
        String expMessage = buf.toString();
        return expMessage;
    }

    /**
     * 获取WEB—INF/config/+source中指定的properties的属性值
     * @param propName 属性的名称（"dataSource.password"）
     * @param source 资源地址 ("/hibernate/hibernate.properties")
     * @return String 返回属性名称对应的value
     */
    public static String getPropByName(String propName, String source) {
        try {
            String url = StringUtils.class.getResource("/").getPath().replace("%20", " ");
            String path = url.substring(0, url.indexOf("WEB-INF")) + "WEB-INF/config" + source;
            //jar包中的class文件调用这个接口时返回的path以"file:"开头，这里需调用外部的资源文件所以需要删除file：
            if (path.startsWith("file")) {
                path = path.substring(5);
            }
            Properties config = new Properties();
            config.load(new FileInputStream(path));
            return config.getProperty(propName);
        } catch (Exception e) {
            log.error("读取配置文件失败:" + outPutException(e));
        }
        return null;
    }

    /**
     * 匹配url
     * @param source 源字符串
     * @param target 目标字符串
     * @return boolean
     */
    public static boolean match(String source, String target) {
        //?转为&,避免正则中匹配,*转为.*匹配任意字符,前后加上^和&
        //没有*号即为^source$必须完全匹配
        source = "^" + source.replace("?", "&").replace("*", ".*") + "$";
        //?转为&,避免正则中匹配
        target = target.replace("?", "&");
        return target.matches(source);
    }
}
