package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.core.pojo.template.TypeTemplate;
import com.fmjava.service.TemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temp")
public class TemplateController {

    @Reference
    TemplateService templateService;
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer pageSize, @RequestBody TypeTemplate template){
       PageResult pageResult =  templateService.findPage(page,pageSize,template);
       return  pageResult;
    }

    //添加模板
    @RequestMapping("/add")
    public Result add(@RequestBody TypeTemplate template){
        try{
            templateService.add(template);
            return new Result(true,"添加成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    //根据id查询模板数据进行回显
    @RequestMapping("/findTempById")
    public TypeTemplate findTempById(Long id){
        return templateService.findTempById(id);
    }

    //更新模板
    @RequestMapping("/update")
    public Result update(@RequestBody TypeTemplate template){
        System.out.println("laileam"+template.getName());
        try{

            System.out.println(template);
            templateService.update(template);
            return new Result(true,"更新成功");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        if (ids==null){
            return new Result(false,"请选中要删除的模板");
        }
        try{
            templateService.delete(ids);
           return new Result(true,"删除成功");
        }catch (Exception e){
            return new Result(false,"删除失败");
        }
    }
}
