package com.zjh.wxchat._config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * 代理配置
 *
 * @author 张俊虎
 * @date 2023/04/21
 */
@Configuration
public class ProxyConfig {
    /**
     * 代理主机
     */
    @Value("${proxy.host}")
    private String proxyHost;
    /**
     * 代理端口
     */
    @Value("${proxy.port}")
    private Integer proxyPort;
    /**
     * 获得代理
     *
     * @return {@link Proxy}
     */
    @Bean
    public Proxy getProxy(){
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }
}
