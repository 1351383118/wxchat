package com.zjh.wxchat._config;

import com.zjh.wxchat._bean._ai.OpenApiData;
import com.zjh.wxchat._bean._wx.StaticInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * bean配置
 *
 * @author 张俊虎
 * @date 2023/04/27
 */
@Configuration
@Slf4j
public class BeanConfig {
    /**
     * 令牌
     */
    @Value("${wechat.develop.token}")
    private final String token = null;
    /**
     * 第三方用户唯一凭证
     */
    @Value("${wechat.develop.app_id}")
    private final String appId = null;
    /**
     * 第三方用户唯一凭证密钥
     */
    @Value("${wechat.develop.app_secret}")
    private final String appSecret = null;
    /**
     * 消息加解密密钥
     */
    @Value("${wechat.develop.encoding_AESKey}")
    private final String encodingAESKey = null;
    /**
     * 获取url访问令牌
     */
    @Value("${wechat.develop.get_access_token_url}")
    private final String getAccessTokenUrl = null;
    /**
     * openApi静态信息路径
     */
    @Value("${ai.develop.ai_key}")
    private final String aiKey = null;
    /**
     * 得到微信静态信息
     *
     * @return {@link StaticInformation}
     */
    @Bean
    public StaticInformation getStaticInformation() {
        return new StaticInformation(token,appId,appSecret,encodingAESKey,getAccessTokenUrl);
    }
    /**
     * 得到openApi静态信息
     *
     * @return {@link OpenApiData}
     */
    @Bean
    public OpenApiData getOpenApiData() {
        return new OpenApiData(aiKey);
    }
}
