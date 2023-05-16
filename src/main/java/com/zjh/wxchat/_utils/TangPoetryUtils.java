package com.zjh.wxchat._utils;

import com.zjh.wxchat._bean._poetry.TangPoetry;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 唐诗工具
 *
 * @author 张俊虎
 * @date 2023/05/15
 */
public class TangPoetryUtils {
    /**
     * 存储格式
     *
     * @param tangPoetryList 唐诗列表
     */
    public static void storageFormat(List<TangPoetry> tangPoetryList){
        if (!ObjectUtils.isEmpty(tangPoetryList)){
            for (TangPoetry tangPoetry : tangPoetryList) {
                tangPoetry.setTitle(StringUtils.hasText(tangPoetry.getTitle()) ? tangPoetry.getTitle() : null);
                tangPoetry.setAuthor(StringUtils.hasText(tangPoetry.getAuthor()) ? tangPoetry.getAuthor() : null);
                tangPoetry.setPrologue(StringUtils.hasText(tangPoetry.getPrologue()) ? tangPoetry.getPrologue() : null);
                List<String> content = tangPoetry.getContent();
                if (!ObjectUtils.isEmpty(content)){
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : tangPoetry.getContent()) {
                        stringBuilder.append(s).append("\r");
                    }
                    tangPoetry.setContents(stringBuilder.toString());
                }
            }
        }
    }

    /**
     * 返回格式化
     *
     * @param tangPoetryList 唐诗列表
     * @param count          数
     * @return {@link String}
     */
    public static String returnsFormatted(Long count, List<TangPoetry> tangPoetryList){
        StringBuilder stringBuilder = new StringBuilder();
        if (!ObjectUtils.isEmpty(tangPoetryList)){
            stringBuilder.append("共").append(count).append("首");
            int i = 1;
            for (TangPoetry tangPoetry : tangPoetryList) {
                stringBuilder.append("\r\r第").append(i).append("首:");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getTitle()) ? "\r标题:" + tangPoetry.getTitle().trim() : "");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getAuthor()) ? "\r作者:" + tangPoetry.getAuthor().trim() : "");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getPrologue()) ? "\r序言:\r" + tangPoetry.getPrologue().trim() : "");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getContents()) ? "\r内容:\r" + tangPoetry.getContents().trim() : "");
                i++;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 返回格式化
     *
     * @param tangPoetryList 唐诗列表
     * @param count          数
     * @return {@link String}
     */
    public static String returnsFormatted(Long count, Long numberOfPages, List<TangPoetry> tangPoetryList){
        StringBuilder stringBuilder = new StringBuilder();
        if (!ObjectUtils.isEmpty(tangPoetryList)){
            stringBuilder.append("共").append(count).append("首").append("\t第").append(numberOfPages).append("页");
            int i = 1;
            for (TangPoetry tangPoetry : tangPoetryList) {
                stringBuilder.append("\r\r第").append(i).append("首:");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getTitle()) ? "\r标题:" + tangPoetry.getTitle().trim() : "");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getAuthor()) ? "\r作者:" + tangPoetry.getAuthor().trim() : "");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getPrologue()) ? "\r序言:\r" + tangPoetry.getPrologue().trim() : "");
                stringBuilder.append(StringUtils.hasText(tangPoetry.getContents()) ? "\r内容:\r" + tangPoetry.getContents().trim() : "");
                i++;
            }
        }
        return stringBuilder.toString();
    }
}
