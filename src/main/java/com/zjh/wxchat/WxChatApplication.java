package com.zjh.wxchat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zjh.wxchat._mapper")
public class WxChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxChatApplication.class, args);
    }

}
