package com.zjh.wxchat._service;

import com.alibaba.fastjson2.JSON;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.zjh.wxchat._bean._ai.OpenApiData;
import com.zjh.wxchat._bean._wx.GetWxMessage;
import com.zjh.wxchat._bean._wx.SendWxMessage;
import com.zjh.wxchat._enum.MessageEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import retrofit2.HttpException;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.*;
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
     * 开放人工智能客户端
     */
    @Resource
    private OpenAiClient openAiClient;

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
        //分类
        MessageEnum messageEnum = MessageEnum.getMessageEnum(getWxMessage.getMsgType());
        switch (messageEnum){
            case TEXT:
                String content = JSON.parseObject(JSON.toJSONString(getWxMessage), GetWxMessage.TextMessage.class).getContent();
                log.info("收到来自{}的消息:{}",sendWxMessage.getToUserName(),content);
                SendWxMessage.TextMessage textMessage = new SendWxMessage.TextMessage();
                BeanUtils.copyProperties(sendWxMessage,textMessage);
                String data = getData(sendWxMessage.getToUserName(), content);
                log.info("即将返回{}的消息:{}",sendWxMessage.getToUserName(),data);
                textMessage.setContent(data);
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
        FutureTask<ChatCompletionResponse> future = new FutureTask<>(
                new Callable<ChatCompletionResponse>() {
                    @Override
                    public ChatCompletionResponse call() throws Exception {
                        Message message = Message.builder().role(Message.Role.USER).content(prompt).build();
                        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Collections.singletonList(message)).build();
                        chatCompletion.setUser(fromUserName);
                        return openAiClient.chatCompletion(chatCompletion);
                    }
                }
        );
        ExecutorService service = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        service.execute(future);
        ChatCompletionResponse chatCompletionResponse;
        try {
            chatCompletionResponse = future.get(4500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return "数据获取超时";
        } catch (HttpException httpException){
            return "请稍后重试";
        }
        return chatCompletionResponse.getChoices().stream().map(ChatChoice::getMessage).map(Message::getContent).collect(Collectors.joining());
    }
}
