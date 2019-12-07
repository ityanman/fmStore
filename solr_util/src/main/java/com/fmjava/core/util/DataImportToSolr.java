package com.fmjava.core.util;

import com.alibaba.fastjson.JSON;
import com.fmjava.core.dao.item.ItemDao;
import com.fmjava.core.pojo.item.Item;
import com.fmjava.core.pojo.item.ItemQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public class DataImportToSolr {
    @Autowired
    ItemDao itemDao;
    @Autowired
    SolrTemplate solrTemplate;
    public void importItemToSolr(){
        //查询所有库存数据（审核通过的）
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andStatusEqualTo("2");
        List<Item> items = itemDao.selectByExample(itemQuery);
        if (items!=null){
            //处理规格数据  str --> map
            for (Item item : items) {
                String spec = item.getSpec();
                Map map = JSON.parseObject(spec, Map.class);
                item.setSpecMap(map);
            }
        }
        //2把查询出的数据保存到索引库当中
        solrTemplate.saveBeans(items);
        solrTemplate.commit();
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        DataImportToSolr dataImportToSolr = (DataImportToSolr)context.getBean("dataImportToSolr");
        dataImportToSolr.importItemToSolr();
    }
}
