package com.cqabj.springboot.web.config.swagger;

import com.cqabj.springboot.web.common.props.SwaggerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fjia
 * @version V1.0 --2018/2/6-${time}
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    private static final String KEY_NAME    = "value";
    private static final String KEY_PASS_AS = "header";

    @Resource
    private SwaggerProperties   swaggerProperties;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
            .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
            .paths(PathSelectors.any()).build()
            //允许忽略预定义的响应消息默认值
            .useDefaultResponseMessages(false)
            //配置安全策略
            .securitySchemes(headList());
    }

    @Bean
    public UiConfiguration getUiConfig() {
        //url
        return new UiConfiguration(null,
            //docExpansion            => none | list
            "list",
            //apliSorter              => alpha
            "alpha",
            //defaultModelRendering   => schema | model
            "model",
            //enableJsonEditer        => true | false
            UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false,
            //showRequestHeaders      => true | false
            true, null);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(swaggerProperties.getTitle())
            .description(swaggerProperties.getDescription())
            .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
            .version(swaggerProperties.getVersion()).license(swaggerProperties.getLicense())
            .licenseUrl(swaggerProperties.getLicenseUrl())
            .contact(new Contact(swaggerProperties.getContact().getName(),
                swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getMail()))
            .build();
    }

    /**
     * request签名验证头部
     */
    private List<ApiKey> headList() {
        ApiKey apiKey1 = new ApiKey("Authorization", KEY_NAME, KEY_PASS_AS);
        ApiKey apiKey2 = new ApiKey("Msg-Ident", KEY_NAME, KEY_PASS_AS);
        ApiKey apiKey3 = new ApiKey("Sign-Date", KEY_NAME, KEY_PASS_AS);
        List<ApiKey> list = new ArrayList<>();
        list.add(apiKey1);
        list.add(apiKey2);
        list.add(apiKey3);
        return list;
    }
}
