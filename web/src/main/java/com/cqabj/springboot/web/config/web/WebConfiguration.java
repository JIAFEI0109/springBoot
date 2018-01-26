package com.cqabj.springboot.web.config.web;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.cqabj.springboot.web.common.props.UploadProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fjia
 * @version V1.0 --2018/1/26-${time}
 */
@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    private static final String OUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Resource
    private UploadProperties uploadProperties;

    /**
     * 设置hibernate延迟加载
     * hibrnate+spring配合,如果设置lazy=true(延迟加载),读取了父数据hibernae就会关闭Session
     * 之后读取子数据时就会抛出lazyinit错误
     *
     * 由spring提供OpenSessionInViewFilter就是保持Session状态直到request将全部页面发送到客户端
     */
    @Bean
    public FilterRegistrationBean openSessionInViewFilter() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new OpenSessionInViewFilter());
        Map<String, String> map = new HashMap<>(1);
        map.put("singleSession", "true");
        bean.setInitParameters(map);
        bean.setAsyncSupported(true);
        bean.addUrlPatterns("/*");
        bean.setName("openSessionInViewFilter");
        bean.setOrder(Integer.MAX_VALUE);
        return bean;
    }

    /**
     * 默认使用fastjson处理返回对象
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverters = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //json格式漂亮的,并且禁止循环引用
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat,
            SerializerFeature.DisableCircularReferenceDetect);
        fastJsonConfig.setDateFormat(OUT_DATE_FORMAT);
        fastConverters.setFastJsonConfig(fastJsonConfig);

        /* 支持的文件类型 */
        List<String> array = new ArrayList<>();
        array.add(MediaType.APPLICATION_JSON_UTF8_VALUE);
        array.add(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        array.add(MediaType.IMAGE_JPEG_VALUE);
        array.add(MediaType.TEXT_HTML_VALUE);
        array.add(MediaType.IMAGE_GIF_VALUE);
        array.add(MediaType.IMAGE_PNG_VALUE);
        fastConverters.setSupportedMediaTypes(MediaType.parseMediaTypes(array));

        converters.add(fastConverters);
    }

    /**
     * 配置session,request监听器,在service,controller中可以
     * 直接通过@actoware方式直接获取
     * HttpSession session;HeepServletrequest request
     */
    @Bean
    public RequestContextFilter requestContextFilter() {
        return new RequestContextFilter();
    }

    /* 自定义拦截器：如url签名验证*/
    /*
    public FilterRegistrationBean sigFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
    }
    */

    /**
     * 静态资源(图片、文档存放地址)路径
     * 当请求这些资源时,自动映射文件存放地址
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);

    }
}
