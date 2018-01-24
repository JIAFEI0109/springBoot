package com.cqabj.springboot.model.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @author fjia
 * @version V1.0 --2018/1/22-${time}
 */
@Data
@ToString
@ApiModel(value = "消息实体")
public class PageResultInfo<T> implements Serializable {
    private static final long serialVersionUID = -5270145250143270494L;

    @ApiModelProperty(value = "消息编码", required = true)
    private Long              code;

    @ApiModelProperty(value = "数据", required = true)
    private List<T>           data;

    @ApiModelProperty(value = "返回消息")
    private String            msg;

    @ApiModelProperty(value = "当前页码")
    private Integer           pageIndex;

    @ApiModelProperty(value = "分页条数")
    private Integer           pageSize;

    @ApiModelProperty(value = "总数")
    private Long              total;

    /**
     * 返回不用的分页的数据
     * @param data 传输的数据
     * @param <T> 数据类型
     * @return PageResultInfo
     */
    public static <T> PageResultInfo<T> createSuccessResult(List<T> data) {
        PageResultInfo<T> ret = new PageResultInfo<>();
        ret.code = CodeEnum.SUCCESS.getCode();
        ret.msg = CodeEnum.SUCCESS.getMsg();
        ret.data = data;
        return ret;
    }

    /**
     * 放回分页数据并将分页信息返回
     * @param data 传输数据
     * @param pageIndex 当前页码
     * @param pageSize 分页页码
     * @param total 总数
     * @param <T> 类型
     * @return PageResultInfo
     */
    public static <T> PageResultInfo<T> createSuccessResult(List<T> data, Integer pageIndex,
                                                            Integer pageSize, Long total) {
        PageResultInfo<T> ret = new PageResultInfo<>();
        ret.code = CodeEnum.SUCCESS.getCode();
        ret.msg = CodeEnum.SUCCESS.getMsg();
        ret.pageIndex = pageIndex;
        ret.pageIndex = pageSize;
        ret.total = total;
        return ret;
    }

    /**
     * 返回错误的结果
     * @param <T> 类型
     * @return PageResultInfo
     */
    public static <T extends Serializable> PageResultInfo<T> createFailureResult() {
        PageResultInfo<T> ret = new PageResultInfo<>();
        ret.code = CodeEnum.ERROR.getCode();
        ret.msg = CodeEnum.ERROR.getMsg();
        return ret;
    }

    /**
     * 返回结果
     * @param codeEnum 返回结果状态码
     * @param <T> 类型
     * @return PageResultInfo
     */
    public static <T> PageResultInfo<T> createResult(CodeEnum codeEnum) {
        PageResultInfo<T> ret = new PageResultInfo<>();
        ret.code = codeEnum.getCode();
        ret.msg = codeEnum.getMsg();
        return ret;
    }
}
