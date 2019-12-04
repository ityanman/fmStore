package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.service.ContentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/content")
public class ContentController {

    @Reference
    private ContentService contentService;
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows,@RequestBody Content content) {
        PageResult result = contentService.findPage(page, rows,content);
        return result;
    }
    @RequestMapping("/add")
    public Result add(@RequestBody Content content){
        try{
            contentService.add(content);
            return new Result(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    @RequestMapping("/findOne")
    public Content findOne(Long id){
        return contentService.findOne(id);
    }
    //更新
    @RequestMapping("/update")
    public Result update(@RequestBody Content content){
        try{
            contentService.update(content);
            return new Result(true,"更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    //删除
    @RequestMapping("/deleteContent")
    public Result deleteContent(Long[] id){
        try{
            contentService.deleteContent(id);
            return new Result(true,"删除成功");
        }catch (Exception e){
            return new Result(false,"删除失败");
        }
    }
}

