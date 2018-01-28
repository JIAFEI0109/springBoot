package com.cqabj.springboot.web.config.security;

import lombok.Data;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fjia
 * Created by cqabj on 2018/1/28.
 */
@Data
public class UserRequiresCsrfMatcher implements RequestMatcher {
	private final Set<String> allowedMethods = new HashSet<>(Arrays.asList("GET","HEAD","TRACE","OPTIONS"));

	private List<String> execludeUrlsl;


	@Override
	public boolean matches(HttpServletRequest request) {
		if (execludeUrlsl != null && !execludeUrlsl.isEmpty()){
			String servletPath = request.getRequestURL().toString();
			for (String url:execludeUrlsl){
				if (servletPath.contains(url)){
					return false;
				}
			}
		}
		return this.allowedMethods.contains(request.getMethod());
	}
}
