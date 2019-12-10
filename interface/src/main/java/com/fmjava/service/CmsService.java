package com.fmjava.service;


import java.util.Map;

public interface CmsService {
    //根据ID创建静态化页面
    public Map<String,Object> CreateStaticPage(Long goodsId);
}
