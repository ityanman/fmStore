package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.GoodsEntity;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.service.GoodsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodController {
    @Reference
    GoodsService goodsService;
    @RequestMapping("/add")
    public Result add(@RequestBody GoodsEntity goodsEntity){
         try{
             goodsService.add(goodsEntity);
             return new Result(true,"添加成功");
         }catch (Exception e){
             return new Result(false,"添加失败");
         }
    }
}
