package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.core.pojo.entity.SpecEntity;
import com.fmjava.core.pojo.specification.Specification;
import com.fmjava.service.SpecService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spec")
public class SpecController {
    @Reference
    private SpecService specService;
    @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer pageSize, @RequestBody Specification specification){
        PageResult pageResult = specService.findPage(page, pageSize, specification);
        return pageResult;
    }

    @RequestMapping("/save")
    public Result save(@RequestBody SpecEntity specEntity){
/*        System.out.println("来到了Controlleraa"+specEntity.getSpecification().getSpecName()+"ggg");
        System.out.println(specEntity.getSpecificationOptions().get(1));
        System.out.println("结束");*/
        try {
            specService.saveSpec(specEntity);
            return new Result(true,"保存成功");

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"保存失败");
        }
    }
    @RequestMapping("/update")
    public Result update(@RequestBody SpecEntity specEntity){
        try {
            specService.updateSpec(specEntity);
            return new Result(true,"更新成功");

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }
    @RequestMapping("/findSpecById")
    public SpecEntity findSpecById(Long id){
        System.out.println("来了"+id);
        SpecEntity specEntity = specService.findSpecById(id);
        return specEntity;
    }
    //删除
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        if (ids==null){
            return new Result(false,"请选则要删除的内容");
        }
        try {
            System.out.println("来了："+ids);
            specService.deleteBrand(ids);
            return new Result(true,"删除成功");
        }catch (Exception e){
            return new Result(false,"删除失败");
        }
    }


}
