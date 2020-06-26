package com.mboot.config;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mboot
 * @date 2020-06-26 17:03
 */

@Configuration
public class MybatisPlusConfig {

	/***
	 * plus Performance optimization
	 * 
	 * @return
	 */
	@Bean
	public PerformanceInterceptor performanceInterceptor() {
		PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
		performanceInterceptor.setMaxTime(1000);
		performanceInterceptor.setFormat(true);
		return performanceInterceptor;
	}

	/**
	 * mybatis-plus Pagination plugin
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

}
