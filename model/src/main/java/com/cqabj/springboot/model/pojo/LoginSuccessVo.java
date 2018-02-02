package com.cqabj.springboot.model.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author fjia
 * @version V1.0 --2018/2/2-${time}
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class LoginSuccessVo implements Serializable {
    private static final long serialVersionUID = 8117493323959426745L;

    private Long              uId;
    private String            loginName;
    //...添加想要的数据
}
