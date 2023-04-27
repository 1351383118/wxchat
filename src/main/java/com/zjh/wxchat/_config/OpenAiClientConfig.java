package com.zjh.wxchat._config;


import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.zjh.wxchat._bean._ai.OpenApiData;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * 开放人工智能客户端配置
 *
 * @author 张俊虎
 * @date 2023/04/20
 */
@Configuration
public class OpenAiClientConfig {
    /**
     * 开放api数据
     */
    @Resource
    private OpenApiData openApiData;
    /**
     * 客户端
     */
    @Resource
    private OkHttpClient okHttpClient;

    /**
     * 开放人工智能服务
     *
     * @return {@link OpenAiClient}
     */
    @Bean
    public OpenAiClient openAiService(){
        return OpenAiClient.builder()
                .apiKey(Collections.singletonList(openApiData.getAi_key()))
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient)
                .build();
    }
}
