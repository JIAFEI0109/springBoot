package com.cqabj.springboot.web.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fjia
 * @version V1.0 --2018/2/6-${time}
 */
@Data
@Component
@ConfigurationProperties("swagger")
public class SwaggerProperties {

    private String        basePackage;
    private String        title;
    private String        description;
    private String        termsOfServiceUrl;
    private String        version;
    private String        license;
    private String        licenseUrl;
    private ContactEntity contact;

    @Data
    public static class ContactEntity {
        private String name;
        private String url;
        private String mail;
    }

}
