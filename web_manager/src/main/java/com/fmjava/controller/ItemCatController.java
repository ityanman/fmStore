package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.service.ItemCatService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {
    @Reference
    private ItemCatService itemCatService;
    @RequestMapping("/findByParentId")
    public List<ItemCat> findByParentId(Long parentId){
        List<ItemCat> itemCatList =  itemCatService.findByParentId(parentId);
        return  itemCatList;
    }
    //根据id查询父id
    @RequestMapping("/findParentIdById")
    public Long findParentIdById(Long id){
        return itemCatService.findParentIdById(id);
    }
}
