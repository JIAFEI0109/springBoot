package com.cqabj.springboot.model.common;

/**
 * 通用属性接口
 * (成员变量	只能是常量。默认修饰符 public static final 成员方法	只能是抽象方法。默认修饰符 public abstract)
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
public interface IGlobalConstant {

    String CURRENT_USER             = "currentUser";
    /**
     * cookietoken 大小
     */
    int    COOKIE_TOKENS_SIZE       = 3;
    /**
     * ciookie第一个（用户名）
     */
    int    COOKIE_TOKENS_USERNAME   = 0;
    /**
     * ciookie第二个（存活时间）
     */
    int    COOKIE_TOKENS_EXPIRYTIME = 1;
    /**
     * ciookie第三个（密码）
     */
    int    COOKIE_TOKENS_SIGNATURE  = 2;
    /**
     * cookie存活时间最小值
     */
    int    COOKIE_LIFE_TIME         = 0;
    /**
     * 秒转毫秒倍数
     */
    Long   SECONDS_TO_MILLISECONDS  = 1000L;
}
