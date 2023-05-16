package com.zjh.wxchat._service._impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjh.wxchat._bean._poetry.TangPoetry;
import com.zjh.wxchat._mapper.TangPoetryMapper;
import com.zjh.wxchat._service._interface.TangPoetryService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 唐诗服务
 *
 * @author 张俊虎
 * @date 2023/05/15
 */
@Service
public class TangPoetryServiceImpl extends ServiceImpl<TangPoetryMapper, TangPoetry> implements TangPoetryService {
    /**
     * 唐诗映射器
     */
    @Resource
    private TangPoetryMapper tangPoetryMapper;

    /**
     * 查询
     *
     * @param tangPoetry 唐诗
     * @return {@link List}<{@link TangPoetry}>
     */
    @Override
    public List<TangPoetry> list(TangPoetry tangPoetry) {
        return tangPoetryMapper.selectList(getQueryWrapper(tangPoetry));
    }

    @Override
    public List<TangPoetry> list(TangPoetry tangPoetry,Long page,Long number) {
        return tangPoetryMapper.selectList(getQueryWrapper(tangPoetry,page,number));
    }

    /**
     * 数
     *
     * @param tangPoetry 唐诗
     * @return {@link Long}
     */
    @Override
    public Long count(TangPoetry tangPoetry) {
        return tangPoetryMapper.selectCount(getQueryWrapper(tangPoetry));
    }

    /**
     * 批量添加
     *
     * @param tangPoetryList 唐诗列表
     * @return boolean
     */
    @Override
    public boolean saveBatch(List<TangPoetry> tangPoetryList) {
        return tangPoetryMapper.insertBatch(tangPoetryList);
    }

    /**
     * 得到查询包装
     *
     * @param tangPoetry 唐诗
     * @param page       页面
     * @param number     数量
     * @return {@link LambdaQueryWrapper}<{@link TangPoetry}>
     */
    private LambdaQueryWrapper<TangPoetry> getQueryWrapper(TangPoetry tangPoetry,Long page,Long number){
        LambdaQueryWrapper<TangPoetry> queryWrapper = getQueryWrapper(tangPoetry);
        queryWrapper.last("limit " + page + "," + number);
        return queryWrapper;
    }

    /**
     * 得到查询包装
     *
     * @param tangPoetry 唐诗
     * @return {@link LambdaQueryWrapper}<{@link TangPoetry}>
     */
    private LambdaQueryWrapper<TangPoetry> getQueryWrapper(TangPoetry tangPoetry){
        LambdaQueryWrapper<TangPoetry> tangPoetryQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(tangPoetry.getId())){
            tangPoetryQueryWrapper.eq(TangPoetry::getId,tangPoetry.getId());
        }
        if (StringUtils.hasText(tangPoetry.getAuthor())){
            tangPoetryQueryWrapper.eq(TangPoetry::getAuthor,tangPoetry.getAuthor());
        }
        if (StringUtils.hasText(tangPoetry.getTitle())){
            tangPoetryQueryWrapper.eq(TangPoetry::getTitle,tangPoetry.getTitle());
        }
        if (!ObjectUtils.isEmpty(tangPoetry.getContent())){
            tangPoetryQueryWrapper.like(TangPoetry::getContents,tangPoetry.getContent().toString());
        }
        if (StringUtils.hasText(tangPoetry.getPrologue())){
            tangPoetryQueryWrapper.like(TangPoetry::getPrologue,tangPoetry.getPrologue());
        }
        //根据 id 排序
        tangPoetryQueryWrapper.orderBy(true,true,TangPoetry::getId);
        return tangPoetryQueryWrapper;
    }
}
