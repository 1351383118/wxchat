package com.zjh.wxchat._service;

import com.alibaba.fastjson2.JSON;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.zjh.wxchat._bean._ai.OpenApiData;
import com.zjh.wxchat._bean._poetry.TangPoetry;
import com.zjh.wxchat._bean._wx.GetWxMessage;
import com.zjh.wxchat._bean._wx.SendWxMessage;
import com.zjh.wxchat._enum.ChinesePoetryEnum;
import com.zjh.wxchat._enum.MessageEnum;
import com.zjh.wxchat._service._interface.TangPoetryService;
import com.zjh.wxchat._utils.RedisUtil;
import com.zjh.wxchat._utils.TangPoetryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
     * 唐诗服务
     */
    @Resource
    private TangPoetryService tangPoetryService;
    /**
     * 存储请求计数
     */
    private static ConcurrentHashMap<String, Long> requestCountMap = new ConcurrentHashMap<>();

    /**
     * 聊天
     * 普通消息缓冲 接收方##提问内容 , 回答
     * 诗词缓冲 接收方#提问内容 , 回答
     * @param getWxMessage 得到消息
     * @return {@link String}
     */
    public SendWxMessage chat(GetWxMessage getWxMessage){
        SendWxMessage sendWxMessage = new SendWxMessage();
        sendWxMessage.setToUserName(getWxMessage.getFromUserName());
        sendWxMessage.setFromUserName(getWxMessage.getToUserName());
        sendWxMessage.setCreateTime(new Date().getTime());
        //分类
        MessageEnum messageEnum = MessageEnum.getMessageEnum(getWxMessage.getMsgType());
        switch (messageEnum){
            case TEXT:
                String content = JSON.parseObject(JSON.toJSONString(getWxMessage), GetWxMessage.TextMessage.class).getContent();
                log.info("收到来自{}的消息:{}",sendWxMessage.getToUserName(),content);
                int timeout = duplicateMessagesShortTimeFiltering(sendWxMessage.getToUserName(), content, 15);
                String data = null;
                if (timeout >= 0){
                    data = ("请求频繁，请等待" + timeout + "秒");
                }else {
                    //剩余数量
                    long numberRemaining = 0;
                    //总数
                    long count = 0L;
                    //从第几条开始
                    long numberOfPages = 0;
                    if (content.trim().equals("继续")){
                        Object poetryBuffer = redisUtil.get(sendWxMessage.getToUserName());
                        if (!ObjectUtils.isEmpty(poetryBuffer)){
                            content = poetryBuffer.toString().split(",")[0];
                            count = Long.parseLong(poetryBuffer.toString().split(",")[1]);
                            numberRemaining = Long.parseLong(poetryBuffer.toString().split(",")[2]);
                            numberOfPages = Long.parseLong(poetryBuffer.toString().split(",")[3]);
                        }
                    }
                    //诗歌 格式 唐诗# 宋词# 元曲#
                    ChinesePoetryEnum chinesePoetryEnum = ChinesePoetryEnum.getChinesePoetryEnum(content);
                    if (!ObjectUtils.isEmpty(chinesePoetryEnum) || numberRemaining != 0){
                        switch (chinesePoetryEnum){
                            case TANG_POETRY:
                                TangPoetry tangPoetry = new TangPoetry().setTitle(content.replace(ChinesePoetryEnum.TANG_POETRY.getValue() + "#", ""));
                                if (numberRemaining != 0){
                                    numberOfPages += 5;
                                }else {
                                    count = tangPoetryService.count(tangPoetry);
                                }
                                if (count == 0){
                                    break;
                                }else if (count > 5){
                                    if (numberRemaining == 0){
                                        numberRemaining = 5;
                                    }else {
                                        numberRemaining = (count - (numberOfPages + 4));
                                    }
                                    if (numberRemaining >= 5){
                                        redisUtil.set(sendWxMessage.getToUserName(),content + "," + count + "," + numberRemaining + "," + numberOfPages,120);
                                    }else {
                                        redisUtil.deleteString(sendWxMessage.getToUserName());
                                    }
                                    List<TangPoetry> tangPoetryList = tangPoetryService.list(tangPoetry,numberOfPages,numberRemaining > 5 ? 5 : numberRemaining > 0 ? numberRemaining : 1);
                                    data = TangPoetryUtils.returnsFormatted(count,numberOfPages/5 + 1,tangPoetryList);
                                }else {
                                    List<TangPoetry> tangPoetryList = tangPoetryService.list(tangPoetry);
                                    data = TangPoetryUtils.returnsFormatted(count,tangPoetryList);
                                }
                                break;
                        }
                    }
                    if (!StringUtils.hasText(data)){
                        Object velue = redisUtil.get(sendWxMessage.getToUserName() + "##" + content);
                        if (ObjectUtils.isEmpty(velue)){
                            data = getData(sendWxMessage.getToUserName(), content);
                        }else {
                            data = velue.toString();
                            log.info("正常返回数据,删除提问:{}",content);
                            redisUtil.deleteString(sendWxMessage.getToUserName() + "##" + content);
                        }
                    }
                }
                SendWxMessage.TextMessage textMessage = new SendWxMessage.TextMessage();
                BeanUtils.copyProperties(sendWxMessage,textMessage);
                log.info("即将返回{}的消息:{}",sendWxMessage.getToUserName(),data);
                textMessage.setContent("提问:" + content + "\r回答:" + data);
                textMessage.setMsgType(MessageEnum.TEXT.name().toLowerCase());
                sendWxMessage = textMessage;
                break;
        }
        return sendWxMessage;
    }

    /**
     * 短时间内过滤重复消息
     *
     * @param toUserName 用户名字
     * @param other      其他
     * @param seconds    秒
     * @return int
     */
    private int duplicateMessagesShortTimeFiltering(String toUserName, String other, Integer seconds){
        long now = System.currentTimeMillis();
        Long aLong = requestCountMap.get(toUserName + other);
        if (aLong != null && (now - aLong) < (seconds * 1000)){
            return seconds - Math.round((float) (now - aLong) / 1000);
        }
        requestCountMap.put(toUserName + other, now);
        return -1;
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
            return "连接超时\r因为微信被动回复机制,已在后台查询,可以将问题复制下来,稍等再次查询\r避免频繁查询,相同问题15秒内只会查询一次";
        }
        return answer[0];
    }
}
