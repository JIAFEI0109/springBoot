package com.cqabj.springboot.web.common.props;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * spring security相关配置
 *
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@Data
@ToString
@Component
@ConfigurationProperties("spring.security")
public class UserSecurityProperties {

	private RememberEntity remember;

	private AdminEntity admin;

	private String[] webAntMatchers;

	private CsrfEntity csrf;


	@Data
	@ToString
	public static class RememberEntity {
		private String key;
		private String paramter;
		private String cookieName;
	}

	@Data
	@ToString
	public static class AdminEntity {
		private String[] resources;
	}

	@Data
	@ToString
	public static class CsrfEntity {
		private Boolean enable;
		private List<String> execludeUrls;
	}

}
