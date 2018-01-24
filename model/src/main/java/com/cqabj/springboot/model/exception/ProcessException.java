package com.cqabj.springboot.model.exception;

import com.cqabj.springboot.model.common.CodeEnum;

/**
 * controller,service,dao中主动抛出的异常
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
public class ProcessException extends BaseException {

    private static final long serialVersionUID = -62050147398767908L;

    public ProcessException(CodeEnum code) {
        super(code);
    }
}
