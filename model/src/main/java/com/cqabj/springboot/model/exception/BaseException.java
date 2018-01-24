package com.cqabj.springboot.model.exception;

import com.cqabj.springboot.model.common.CodeEnum;

import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义异常的基础所有自定义异常都继承它
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -5229137046936831504L;

    @Setter
    private CodeEnum          code;

    public BaseException(CodeEnum code) {
        super(code.getMsg());
        this.code = code;
    }

}
