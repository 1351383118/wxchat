package com.zjh.wxchat._enum;

import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 消息枚举
 *
 * @author 张俊虎
 * @date 2023/04/23
 */
public enum MessageEnum {
    /**
     * 文本
     */
    TEXT("text"),
    /**
     * 图像
     */
    IMAGE("image"),
    /**
     * 语音
     */
    VOICE("voice"),
    /**
     * 视频
     */
    VIDEO("video"),
    /**
     * 小视频
     */
    SHORTVIDEO("shortvideo"),
    /**
     * 地理位置
     */
    LOCATION("location"),
    /**
     * 链接
     */
    LINK("link"),
    /**
     * 音乐
     */
    MUSIC("music"),
    /**
     * 图文
     */
    NEWS("news");
    /**
     * 值
     */
    private final String value;

    /**
     * 消息枚举
     *
     * @param value 价值
     */
    MessageEnum(String value){
        this.value = value;
    }

    /**
     * 得到消息枚举
     *
     * @param msgType msg类型
     * @return {@link MessageEnum}
     */
    public static MessageEnum getMessageEnum(String msgType){
        if (StringUtils.hasText(msgType)){
            for (MessageEnum messageEnum : MessageEnum.values()) {
                if (Objects.equals(msgType,messageEnum.value)){
                    return messageEnum;
                }
            }
        }
        return null;
    }
}
