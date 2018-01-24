package com.cqabj.springboot.model.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用于返回数据
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@Data
@ToString
@ApiModel(value = "消息实体")
public class ResultInfo<T> implements Serializable {
    private static final long serialVersionUID = -6463646750135559700L;

    @ApiModelProperty(value = "消息编码", required = true)
    private Long              code;

    @ApiModelProperty(value = "数据", required = true)
    private T                 data;

    @ApiModelProperty(value = "返回消息")
    private String            msg;

    @ApiModelProperty(value = "接口状态")
    private String            status;

    /**
     * 返回成功接口
     * @param data 传输的数据
     * @param <T> 数据类型
     * @return ResultInfo
     */
    public static <T> ResultInfo<T> createSuccessResult(T data) {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        resultInfo.code = CodeEnum.SUCCESS.getCode();
        resultInfo.msg = CodeEnum.SUCCESS.getMsg();
        resultInfo.data = data;
        return resultInfo;
    }

    /**
     * 返回成功接口(将接口状态返回)
     * @param data 传输的数据
     * @param status 接口状态
     * @param <T> 数据类型
     * @return ResultInfo
     */
    public static <T> ResultInfo<T> createSuccessResult(T data, String status) {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        resultInfo.code = CodeEnum.SUCCESS.getCode();
        resultInfo.msg = CodeEnum.SUCCESS.getMsg();
        resultInfo.status = status;
        resultInfo.data = data;
        return resultInfo;
    }

    /**
     * 返回失败的信息
     * @param data 传输的数据
     * @param <T> 数据类型
     * @return ResultInfo
     */
    public static <T extends Serializable> ResultInfo<T> createFailureResult(T data) {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        resultInfo.code = CodeEnum.SUCCESS.getCode();
        resultInfo.msg = CodeEnum.SUCCESS.getMsg();
        resultInfo.data = data;
        return resultInfo;
    }

    /**
     * 返回结果（无数据传输）
     * @param codeEnum 响应状态码
     * @param <T> 数据类型
     * @return ResultInfo
     */
    public static <T> ResultInfo<T> createResult(CodeEnum codeEnum) {
        ResultInfo<T> resultInfo = new ResultInfo<>();
        resultInfo.code = CodeEnum.SUCCESS.getCode();
        resultInfo.msg = CodeEnum.SUCCESS.getMsg();
        return resultInfo;
    }
}
