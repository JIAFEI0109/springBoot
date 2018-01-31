package com.cqabj.springboot.web.common.props;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 不进行数字签名帕努单权限相关配置属性
 * @author fjia
 * @version V1.0 --2018/1/31-${time}
 */
@Data
@ToString
@NoArgsConstructor
@ConfigurationProperties("nosign")
public class NoSignSecurityProperties {

    private String[] resources;

    private String[] contextPath;

}
