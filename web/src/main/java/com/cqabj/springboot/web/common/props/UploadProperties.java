package com.cqabj.springboot.web.common.props;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author fjia
 * @version V1.0 --2018/1/26-${time}
 */
@Data
@ToString
@Component
@PropertySource("classpath:config/upload.properties")
@ConfigurationProperties(prefix = "upload", ignoreUnknownFields = false)
public class UploadProperties {
	//@TODO 未完成
}
