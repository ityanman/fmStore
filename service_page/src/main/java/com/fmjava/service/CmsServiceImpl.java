package com.fmjava.service;

import com.fmjava.core.dao.good.GoodsDao;
import com.fmjava.core.dao.good.GoodsDescDao;
import com.fmjava.core.dao.item.ItemCatDao;
import com.fmjava.core.dao.item.ItemDao;
import com.fmjava.core.pojo.good.Goods;
import com.fmjava.core.pojo.good.GoodsDesc;
import com.fmjava.core.pojo.item.Item;
import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.core.pojo.item.ItemCatQuery;
import com.fmjava.core.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmsServiceImpl implements CmsService{
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao descDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao catDao;
    @Autowired
    private FreeMarkerConfigurer freemarkerConfig;
    @Override
    public Map<String,Object> CreateStaticPage(Long goodsId) {
        HashMap<String, Object> resMap = new HashMap<>();
         //1.获取商品数据
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        //2.获取商品详情数据
        GoodsDesc goodsDesc = descDao.selectByPrimaryKey(goodsId);
        //3.获取库存集合数据
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        //4.获取对应的分类数据
        ItemCat category1 = catDao.selectByPrimaryKey(goods.getCategory1Id());
        ItemCat category2 = catDao.selectByPrimaryKey(goods.getCategory2Id());
        ItemCat category3 = catDao.selectByPrimaryKey(goods.getCategory3Id());
        resMap.put("category1",category1);
        resMap.put("category2",category2);
        resMap.put("category3",category3);
        //5.将商品所有数据封装成map返回
        resMap.put("goods",goods);
        resMap.put("goodsDesc",goodsDesc);
        resMap.put("itemList",itemList);
        return resMap;
    }
}
