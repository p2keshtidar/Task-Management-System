package com.example.taskmanagement.applicationconfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.taskmanagement.requestinterceptor.RequestInterceptor;

/**
 * <p>
 * Web configuration class for setting up MVC - related configurations.
 * </p>
 * <p>
 * this class implements the <strong> @WebMvcConfigurer </strong> interface to
 * customize the spring MVC configuration. It registers the
 * <strong> @RequestInterceptor </strong> to intercept http requests and
 * responses.
 * </p>
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	/**
	 * Adds custom interceptors to the registry.
	 * 
	 * @param registry - the <strong> @InterceptorRegistry </strong> to which the
	 *                 interceptor is added
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new RequestInterceptor());
	}
}