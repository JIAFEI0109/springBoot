package com.cqabj.springboot.model.common;

/**
 * 全局返回码
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
public enum CodeEnum {
                      /**
                       * 请求成功
                       */
                      SUCCESS(0L, "请求成功"),
                      /**
                       * 服务器内部错误
                       */
                      ERROR(-1L, "服务器内部错误(500,404)等等"),
                      /**
                       * 请登录
                       */
                      ERROR_10000(10000L, "请登录"),
                      /**
                       * 账号或密码错误
                       */
                      ERROR_10001(10001L, "账号或密码错误"),
                      /**
                       * 重复提交
                       */
                      ERROR_10002(10002L, "重复提交"),
                      /**
                       * 非法提交
                       */
                      ERROE_10003(10003L, "非法提交"),
                      /**
                       * 签名验证失败
                       */
                      ERROR_10004(10004L, "签名验证失败"),
                      /**
                       * 签名已过期
                       */
                      ERROR_10005(10005L, "签名已过期"),
                      /**
                       * 当前用户无可用资源信息
                       */
                      ERROR_10008(10008L, "当前用户无可用资源信息"),
                      /**
                       * 登录用于没有权限
                       */
                      ERROR_20001(20001L, "登录用户没有权限"),
                      /**
                       * 用户信息不存在
                       */
                      ERROR_20002(20002L, "用户信息不存在"),
                      /**
                       * 用户已经失效,无法登陆
                       */
                      ERROR_20003(20003L, "用户已经失效,无法登陆");

    private Long   code;
    private String msg;

    CodeEnum(Long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
