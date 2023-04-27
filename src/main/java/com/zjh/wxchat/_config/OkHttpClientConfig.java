package com.zjh.wxchat._config;

import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * OkHttp客户端配置
 *
 * @author 张俊虎
 * @date 2023/04/21
 */
@Configuration
public class OkHttpClientConfig {
    /**
     * 代理
     */
    @Resource
    private Proxy proxy;
    /**
     * 得到客户端
     *
     * @return {@link OkHttpClient}
     */
    @Bean
    public OkHttpClient getOkHttpClient(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        return new OkHttpClient
                .Builder()
                .proxy(proxy)//自定义代理
                .addInterceptor(httpLoggingInterceptor)//自定义日志输出
                .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
                .connectTimeout(10, TimeUnit.SECONDS)//自定义超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .readTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .build();
    }
}
