package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.core.pojo.good.Brand;
import com.fmjava.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    BrandService brandService;

    @RequestMapping("/findAllBrand")
    public List<Brand> findAllBrand(){
        List<Brand> allBrand = brandService.findAllBrand();
        return allBrand;
    }

    //定义分页查找数据接口
    @RequestMapping("/findPage")
    public PageResult findPage(Integer page,Integer pageSize,@RequestBody Brand brand){
        PageResult PageResult = brandService.findPage(page, pageSize,brand);
        return PageResult;
    }

    //添加品牌
    @RequestMapping("/add")
    public Result add(@RequestBody Brand brand){
       try {
           brandService.add(brand);
           return new Result(true,"添加成功！");
       }catch (Exception e){
         return  new Result(false,"添加失败！");
       }
    }
    //编辑数据回显
    @RequestMapping("/findById")
    public Brand findById(Long id){
        Brand brand = brandService.findById(id);
        return brand;
    }
    //更新品牌
    @RequestMapping("/update")
    public Result update(@RequestBody Brand brand){
        try {
            brandService.updateBrand(brand);
            return new Result(true,"更新成功");
        }catch (Exception e){
            return new Result(false,"更新失败");
        }
    }
    //删除品牌
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        if (ids==null){
            return new Result(false,"请选则要删除的内容");
        }
        try {
            System.out.println("来了："+ids);
            brandService.deleteBrand(ids);
            return new Result(true,"删除成功");
        }catch (Exception e){
            return new Result(false,"删除失败");
        }
    }

    //查询品牌选项
    @RequestMapping("/selectOptionList")
    public List<Map> selectOptionList(){
        return brandService.selectOptionList();
    }



}
