package com.zjh.wxchat._utils;

import com.alibaba.fastjson2.JSON;
import com.zjh.wxchat._bean._wx.StaticInformation;
import com.zjh.wxchat._bean._wx.WxAccessToken;
import com.zjh.wxchat._exception.AccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * WxUtils
 *
 * @author 张俊虎
 * @date 2023/04/11
 */
@Component
@Slf4j
public class WxUtils {
    /**
     * restTemplate
     */
    @Resource
    private RestTemplate restTemplate;
    /**
     * redisTemplate
     */
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private StaticInformation staticInformation;
    /**
     * 获取访问令牌
     */
    public String getAccessToken(){
        Object accessToken = redisUtil.get("access_token");
        if (!ObjectUtils.isEmpty(accessToken)){
            return JSON.parseObject(accessToken.toString(),WxAccessToken.class).getAccess_token();
        }else {
            String accessTokenUrl = staticInformation.getGetAccessTokenUrl();
            accessTokenUrl += "&appid=" + staticInformation.getAppId() + "&secret=" + staticInformation.getAppSecret();
            WxAccessToken wxAccessToken = restTemplate.getForObject(accessTokenUrl, WxAccessToken.class);
            if (ObjectUtils.isEmpty(wxAccessToken)){
                throw new AccessTokenException("未返回信息");
            }
            if (StringUtils.hasText(wxAccessToken.getErrcode()) && StringUtils.hasText(wxAccessToken.getErrmsg())){
                throw new AccessTokenException("错误码:"+wxAccessToken.getErrcode()+"\t错误信息:"+wxAccessToken.getErrmsg());
            }
            log.info("获取到令牌:{}",wxAccessToken.getAccess_token());
            redisUtil.set("access_token",JSON.toJSONString(wxAccessToken),wxAccessToken.getExpires_in());
            return wxAccessToken.getAccess_token();
        }
    }

    public static Boolean checkSignature(String token,String timestamp,String nonce,String signature){
        //排序
        List<String> checkData = Arrays.asList(token, timestamp, nonce);
        Collections.sort(checkData);
        //校验
        return DigestUtils.sha1Hex(checkData.stream().collect(Collectors.joining())).equals(signature);
    }
}
