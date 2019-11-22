package com.fmjava.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.seller.Seller;

public interface SellerService {
    //商家注册
    void register(Seller seller);

    //查找未审核商家
    PageResult findPage(Integer page, Integer pageSize, Seller seller);

    //根据id查询商家
    Seller findSellerByID(String sellerId);

    //审核商家
    void statusSeller(String sellerId, String status);
}
