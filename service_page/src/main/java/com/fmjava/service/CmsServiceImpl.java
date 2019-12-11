package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.good.GoodsDao;
import com.fmjava.core.dao.good.GoodsDescDao;
import com.fmjava.core.dao.item.ItemCatDao;
import com.fmjava.core.dao.item.ItemDao;
import com.fmjava.core.pojo.good.Goods;
import com.fmjava.core.pojo.good.GoodsDesc;
import com.fmjava.core.pojo.item.Item;
import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import sun.rmi.runtime.Log;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class CmsServiceImpl implements CmsService, ServletContextAware {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao descDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao catDao;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    @Override
    public void CreateStaticPage(Long goodsId) throws Exception{
        //获取商品数据
        Map<String, Object> goods = this.getGoodsDataWithID(goodsId);
        //获取配置对象
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        //获取模板对象
        Template template = configuration.getTemplate("item.ftl");
        //设置路径
        String fileName = goodsId+".html";
        String realPath = this.servletContext.getRealPath(fileName);
        System.out.println(realPath);
        //创建输出流
        Writer out = new OutputStreamWriter(new FileOutputStream(new File(realPath)),"utf-8");
        //生成静态页面
        template.process(goods,out);
        //关闭输出流
        out.close();
    }

    //根据id获取商品数据
    public Map<String,Object> getGoodsDataWithID(Long goodsId) {
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
        if (goods!=null){
            ItemCat category1 = catDao.selectByPrimaryKey(goods.getCategory1Id());
            ItemCat category2 = catDao.selectByPrimaryKey(goods.getCategory2Id());
            ItemCat category3 = catDao.selectByPrimaryKey(goods.getCategory3Id());
            resMap.put("category1",category1);
            resMap.put("category2",category2);
            resMap.put("category3",category3);
        }
        //5.将商品所有数据封装成map返回
        resMap.put("goods",goods);
        resMap.put("goodsDesc",goodsDesc);
        resMap.put("itemList",itemList);
        return resMap;
    }

}
