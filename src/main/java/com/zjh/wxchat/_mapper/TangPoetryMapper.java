package com.zjh.wxchat._mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjh.wxchat._bean._poetry.TangPoetry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 唐诗映射器
 *
 * @author 张俊虎
 * @date 2023/05/15
 */
@Mapper
public interface TangPoetryMapper extends BaseMapper<TangPoetry> {
    /**
     * 批量添加
     *
     * @param tangPoetryList 唐诗列表
     * @return boolean
     */
    boolean insertBatch(List<TangPoetry> tangPoetryList);
}
