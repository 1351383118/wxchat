package com.zjh.wxchat._bean._poetry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 唐诗
 *
 * @author 张俊虎
 * @date 2023/05/15
 */
@TableName("tang_poetry")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TangPoetry {
    /**
     * id
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 作者
     */
    @TableField("author")
    private String author;
    /**
     * 标题
     */
    @TableField("title")
    private String title;
    /**
     * 内容
     */
    @TableField("contents")
    private String contents;
    /**
     * 内容 用来接收
     */
    @TableField(exist = false)
    private List<String> content;
    /**
     * 序言
     */
    @TableField("prologue")
    private String prologue;
}
