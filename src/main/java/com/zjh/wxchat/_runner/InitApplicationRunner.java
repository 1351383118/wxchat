package com.zjh.wxchat._runner;

import com.zjh.wxchat._utils.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class InitApplicationRunner implements ApplicationRunner {
    /**
     * wxUtils
     */
    @Resource
    private WxUtils wxUtils;
    @Override
    public void run(ApplicationArguments args) {
        log.info("初始化,accessToken是{}",wxUtils.getAccessToken());
    }
}
