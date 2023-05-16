package com.zjh.wxchat._enum;

import org.springframework.util.StringUtils;

public enum ChinesePoetryEnum {
    /**
     * 唐诗
     */
    TANG_POETRY("唐诗"),
    /**
     * 宋词
     */
    SONG_LYRICS("宋词"),
    /**
     * 元曲
     */
    YUAN_QU("元曲"),
    AUTHOR("作者"),
    CONTENT("内容");
    /**
     * 价值
     */
    private final String value;

    /**
     * 中国诗歌枚举
     *
     * @param value 价值
     */
    ChinesePoetryEnum(String value){
        this.value = value;
    }

    /**
     * 获得价值
     *
     * @return {@link String}
     */
    public String getValue() {
        return value;
    }

    /**
     * 得到中国诗歌枚举
     *
     * @param msgType msg类型
     * @return {@link ChinesePoetryEnum}
     */
    public static ChinesePoetryEnum getChinesePoetryEnum(String msgType){
        if (StringUtils.hasText(msgType)){
            for (ChinesePoetryEnum chinesePoetryEnum : ChinesePoetryEnum.values()) {
                if (msgType.contains(chinesePoetryEnum.value + "#")){
                    return chinesePoetryEnum;
                }
            }
        }
        return null;
    }
}
