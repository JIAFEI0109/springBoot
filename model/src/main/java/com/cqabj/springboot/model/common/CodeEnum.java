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
                      ERROR(-1L, "服务器内部错误(500,404)等等");

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
