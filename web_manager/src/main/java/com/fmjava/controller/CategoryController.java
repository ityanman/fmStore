package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.ad.ContentCategory;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.service.CategoreService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/contentCategory")
public class CategoryController {
    @Reference
    CategoreService  categoryService;
    //广告列表
    @RequestMapping("/search")
    public PageResult findPage(Integer page, Integer rows, @RequestBody ContentCategory category){
        return categoryService.findPage(page,rows,category);
    }
    //添加
    @RequestMapping("/add")
    public Result addCategory(@RequestBody ContentCategory category){
        try{
            categoryService.add(category);
            return new Result(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    //编辑--回显数据
  @RequestMapping("/findOne")
    public ContentCategory findOne(Long id){
        return categoryService.findOne(id);
  }
  //更新数据
    @RequestMapping("/update")
    public Result updateCategory(@RequestBody ContentCategory category){
        try{
            categoryService.update(category);
            return new Result(true,"更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    //删除数据
    @RequestMapping("/delete")
    public Result deleteCategory(Long[] ids){
        try{
            categoryService.delete(ids);
            return new Result(true,"删除成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"删除失败");
        }
    }
    //查询所有分类
    @RequestMapping("/findAll")
    public List<ContentCategory> findAll(){
        try{
            return categoryService.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
