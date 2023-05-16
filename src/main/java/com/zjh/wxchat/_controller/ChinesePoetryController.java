package com.zjh.wxchat._controller;

import com.zjh.wxchat._bean._poetry.TangPoetry;
import com.zjh.wxchat._service._interface.TangPoetryService;
import com.zjh.wxchat._utils.TangPoetryUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 中国诗歌控制器
 *
 * @author 张俊虎
 * @date 2023/05/15
 */
@RestController
@RequestMapping({"/chinesePoetry/"})
public class ChinesePoetryController {
    /**
     * 唐诗服务
     */
    @Resource
    private TangPoetryService tangPoetryService;
    /**
     * 保存唐诗
     *
     * @param tangPoetryList 唐诗列表
     * @return {@link String}
     */
    @PostMapping("saveDataFromTangDynasty")
    public Boolean saveDataFromTangDynasty(@RequestBody List<TangPoetry> tangPoetryList){
        TangPoetryUtils.storageFormat(tangPoetryList);
        return tangPoetryService.saveBatch(tangPoetryList);
    }
}
