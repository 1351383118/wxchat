package com.zjh.wxchat._config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateConfig
 *
 * @author 张俊虎
 * @date 2023/04/11
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 得到RestTemplate
     *
     * @return {@link RestTemplate}
     */
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
