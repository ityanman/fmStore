package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.core.pojo.seller.Seller;
import com.fmjava.service.SellerService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController{
    @Reference
    SellerService sellerService;
    @RequestMapping("/findPage")
    public PageResult findPage(Integer page, Integer pageSize, @RequestBody Seller seller){
        System.out.println(seller);
        System.out.println(page+"和"+pageSize);
        PageResult pageResult =  sellerService.findPage(page,pageSize,seller);
        return  pageResult;
    }
    //详情信息
    @RequestMapping("/findSellerByID")
    public Seller findSellerByID(String sellerId){
        Seller seller = sellerService.findSellerByID(sellerId);
        return seller;
    }
    //审核商家
    @RequestMapping("/statusSeller")
    public Result statusSeller(String sellerId,String status){
        try {
            sellerService.statusSeller(sellerId,status);
            return new Result(true,"审核成功！");
        }catch (Exception e) {
          e.printStackTrace();
            return new Result(false,"审核失败！");
        }
    }
}
