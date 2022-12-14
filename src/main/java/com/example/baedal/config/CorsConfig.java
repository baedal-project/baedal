package com.example.baedal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Value("${BACKEND_URL}")
    private String backendUrl;
    @Value("${BACKEND_DEV_URL}")
    private String backenDevdUrl;
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source =new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);// 내서버가 응답을 할 떄 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        config.addAllowedOrigin(backendUrl);//Production 응답을 허용하겠다.
        config.addAllowedOrigin(backenDevdUrl);//Dev 응답을 허용하겠다.
        config.addAllowedOrigin("http://localhost:5173");//모든 ip의 응답을 허용하겠다.
        config.addAllowedHeader("*");//모든 header의 응답을 허용하겠다.
        config.addAllowedMethod("*");//모든 post,get,putmdelete,patch 요청울 허용하겠다.
        config.addExposedHeader("*");
        source.registerCorsConfiguration("/api/**",config);
        return new CorsFilter(source);
    }
}

