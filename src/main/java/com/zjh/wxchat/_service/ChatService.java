package com.zjh.wxchat._service;

import com.alibaba.fastjson2.JSON;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.zjh.wxchat._bean._ai.OpenApiData;
import com.zjh.wxchat._bean._wx.GetWxMessage;
import com.zjh.wxchat._bean._wx.SendWxMessage;
import com.zjh.wxchat._enum.MessageEnum;
import com.zjh.wxchat._utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 聊天服务
 *
 * @author 张俊虎
 * @date 2023/04/20
 */
@Service
@Slf4j
public class ChatService {
    /**
     * 开放api数据
     */
    @Resource
    private OpenApiData openApiData;
    /**
     * 线程池
     */
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * redis工具
     */
    @Resource
    private RedisUtil redisUtil;
    /**
     * 开放人工智能客户端
     */
    @Resource
    private OpenAiClient openAiClient;
    /**
     * 存储请求计数
     */
    private static ConcurrentHashMap<String, Long> requestCountMap = new ConcurrentHashMap<>();

    /**
     * 聊天
     *
     * @param getWxMessage 得到消息
     * @return {@link String}
     */
    public SendWxMessage chat(GetWxMessage getWxMessage){
        SendWxMessage sendWxMessage = new SendWxMessage();
        sendWxMessage.setToUserName(getWxMessage.getFromUserName());
        sendWxMessage.setFromUserName(getWxMessage.getToUserName());
        sendWxMessage.setCreateTime(new Date().getTime());
        //重复消息过滤
        long now = System.currentTimeMillis();
        Long aLong = requestCountMap.get(sendWxMessage.getToUserName());
        if (aLong != null && (now - aLong) < 15000){
            SendWxMessage.TextMessage textMessage = new SendWxMessage.TextMessage();
            BeanUtils.copyProperties(sendWxMessage,textMessage);
            sendWxMessage = textMessage.setContent("请求频繁，请等待" + (15 - Math.round((float) (now - aLong) / 1000)) + "秒").setMsgType(MessageEnum.TEXT.name().toLowerCase());
            return sendWxMessage;
        }
        requestCountMap.put(sendWxMessage.getToUserName(), now);
        //分类
        MessageEnum messageEnum = MessageEnum.getMessageEnum(getWxMessage.getMsgType());
        switch (messageEnum){
            case TEXT:
                String content = JSON.parseObject(JSON.toJSONString(getWxMessage), GetWxMessage.TextMessage.class).getContent();
                log.info("收到来自{}的消息:{}",sendWxMessage.getToUserName(),content);
                SendWxMessage.TextMessage textMessage = new SendWxMessage.TextMessage();
                BeanUtils.copyProperties(sendWxMessage,textMessage);
                Object velue = redisUtil.get(sendWxMessage.getToUserName() + "##" + content);
                String data;
                if (ObjectUtils.isEmpty(velue)){
                    data = getData(sendWxMessage.getToUserName(), content);
                }else {
                    data = velue.toString();
                    log.info("正常返回数据,删除提问:{}",content);
                    redisUtil.deleteString(sendWxMessage.getToUserName() + "##" + content);
                }
                log.info("即将返回{}的消息:{}",sendWxMessage.getToUserName(),data);
                textMessage.setContent("提问:" + content + "\r回答:" + data);
                textMessage.setMsgType(MessageEnum.TEXT.name().toLowerCase());
                sendWxMessage = textMessage;
                break;
        }
        return sendWxMessage;
    }

    /**
     * 获取数据
     *
     * @param fromUserName 发送方帐号
     * @param prompt       提示
     * @return {@link String}
     */
    private String getData(String fromUserName, String prompt){
        Message message = Message.builder().role(Message.Role.USER).content(prompt).build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Collections.singletonList(message)).build();
        chatCompletion.setUser(fromUserName);
        String[] answer = new String[1];
        threadPoolTaskExecutor.execute(() -> {
            answer[0] = openAiClient.chatCompletion(chatCompletion).getChoices().stream().map(ChatChoice::getMessage).map(Message::getContent).collect(Collectors.joining());
            log.info("即将存储到redis,时间3600秒,数据:{}",answer[0]);
            redisUtil.set(fromUserName + "##" + prompt,answer[0],3600);
        });
        try {
            Thread.sleep(4500);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        if (StringUtils.hasText(answer[0])){
            log.info("正常返回数据,删除提问:{}",prompt);
            redisUtil.deleteString(fromUserName + "##" + prompt);
        }else {
            return "连接超时，请等待几秒再次尝试相同问题";
        }
        return answer[0];
    }
}
