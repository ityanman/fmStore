package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.good.GoodsDao;
import com.fmjava.core.dao.good.GoodsDescDao;
import com.fmjava.core.pojo.entity.GoodsEntity;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsDao goodsDao;
    @Autowired
    GoodsDescDao goodsDescDao;
    @Override
    public void add(GoodsEntity goodsEntity) {
        //1保存商品对象
        //刚添加的商品未审核，默认状态为0
        goodsEntity.getGoods().setAuditStatus("0");
        //添加商品
        goodsDao.insertSelective(goodsEntity.getGoods());
        Long goodsId = goodsEntity.getGoods().getId();
        //2保存商品详情对象，
        //商品主键作为商品详情的主键
        goodsEntity.getGoodsDesc().setGoodsId(goodsId);
        //添加商品详情
        goodsDescDao.insertSelective(goodsEntity.getGoodsDesc());
        //3添加库存，商品规格
        insertItem(goodsEntity);
    }
    public void insertItem(GoodsEntity goodsEntity){

    }
}
