package com.zjh.wxchat._service._interface;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjh.wxchat._bean._poetry.TangPoetry;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 唐诗服务
 *
 * @author 张俊虎
 * @date 2023/05/15
 */
@Service
public interface TangPoetryService extends IService<TangPoetry> {

    List<TangPoetry> list(TangPoetry tangPoetry);
    List<TangPoetry> list(TangPoetry tangPoetry, Long page,Long number);

    Long count(TangPoetry tangPoetry);

    boolean saveBatch(List<TangPoetry> tangPoetryList);
}
