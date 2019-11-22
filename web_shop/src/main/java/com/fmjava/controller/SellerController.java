package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.core.pojo.seller.Seller;
import com.fmjava.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Reference
    private SellerService sellerService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @RequestMapping("/add")
    public Result add(@RequestBody Seller seller){
        try {
            //获取明文密码
            String password = seller.getPassword();
            //对明文密码进行加密
            String securityPassword = passwordEncoder.encode(password);
            //把加密后的密码存储到seller对象中
            seller.setPassword(securityPassword);
            sellerService.register(seller);
            return new Result(true, "注册成功,等待审核");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败!");
        }
    }
}