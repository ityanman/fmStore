package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.service.CmsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodController {
    /**
     * 测试生成静态页面
     * @param goodsId   商品id
     * @return
     */
  @Reference
  CmsService cmsService;
    @RequestMapping("/testPage")
    public Boolean testCreatePage(Long goodsId) {
        try {
            cmsService.CreateStaticPage(goodsId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
