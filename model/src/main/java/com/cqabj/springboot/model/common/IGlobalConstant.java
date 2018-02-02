package com.cqabj.springboot.model.common;

/**
 * 通用属性接口
 * (成员变量	只能是常量。默认修饰符 public static final 成员方法	只能是抽象方法。默认修饰符 public abstract)
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
public interface IGlobalConstant {
    /**
     * 默认cahce key
     */
    String  DEFAULT_CACHE_NAME          = "DEFAULT_CACHE";

    String  CURRENT_USER                = "currentUser";
    /**
     * cookietoken 大小
     */
    int     COOKIE_TOKENS_SIZE          = 3;
    /**
     * ciookie第一个（用户名）
     */
    int     COOKIE_TOKENS_USERNAME      = 0;
    /**
     * ciookie第二个（存活时间）
     */
    int     COOKIE_TOKENS_EXPIRYTIME    = 1;
    /**
     * ciookie第三个（密码）
     */
    int     COOKIE_TOKENS_SIGNATURE     = 2;
    /**
     * cookie存活时间最小值
     */
    int     COOKIE_LIFE_TIME            = 0;
    /**
     * 秒转毫秒倍数
     */
    Long    SECONDS_TO_MILLISECONDS     = 1000L;
    /**
     * 只允许post请求
     */
    boolean POST_ONLY                   = true;
    /**
     * http请求方法
     */
    String  HTTP_METHOD                 = "POST";
    /**
     * 字符编码ISO-8895-1
     */
    String  CHARSET_NAME_ISO88591       = "ISO-8859-1";
    /**
     * 字符编码UTF-8
     */
    String  CHARSET_NAME_UTF8           = "UTF-8";
    /**
     * 登录名 键
     */
    String  LOGIN_USERNAME_PARAMETER    = "loginName";
    /**
     * 登录密码 键
     */
    String  LOGIN_PW_PARAMETER          = "loginPwd";
    /**
     * 登录方式
     */
    String  LOGIN_TYPE_PARAMETER        = "loginType";
    /**
     * 浏览器类型
     */
    String  USER_AGENT                  = "user-agent";
    /**
     * 缓存大小
     */
    int     BUFFER_SIZE                 = 1024;
    /**
     * 流读取开始标志
     */
    int     IO_READ_START_TAG           = -1;
    /**
     * 流读取结束标志
     */
    int     IO_READ_END_TAG             = -1;
    /**
     * iE浏览器参数连接符
     */
    String  IE_REQUEST_PARAMS_DELIMITER = "&";
    /**
     * iE浏览器键值连接符
     */
    String  IE_REQUEST_PARAM_DELIMITER  = "=";
    /**
     * 参数名
     */
    int     PARAM_NAME                  = 0;
    /**
     * 参数值
     */
    int     PARAM_VALUE                 = 1;

    /**
     * 清空Stringbuilder
     */
    int     STRING_BUILDER              = 0;
    /**
     * 禁用（value=0）
     */
    Long    DISABLED                    = 0L;
    /**
     * 登录类型:服务端登录
     */
    String  SERVICE_LOGIN               = "1";
    /**
     * 登录类型：客户端登录
     */
    String  CLIENT_LOGIN                = "2";
    /**
     * 所有的请求都免检
     */
    String  ALL_EXEMPTED                = "/**";
    /**
     * 正斜线
     */
    char    FORWARD_SLASH               = '/';
    /**
     * 签名验证时间 5分钟
     */
    long    SIGN_LIVE_TIME              = 300000;
    /**
     * 冒号(:)
     */
    String  COLON                       = ":";

}
