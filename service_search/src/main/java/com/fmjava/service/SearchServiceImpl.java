package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.pojo.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.HashMap;
import java.util.Map;
@Service
public class SearchServiceImpl implements SearchService{
    @Autowired
    SolrTemplate solrTemplate;
    @Override
    public Map<String, Object> search(Map paramMap) {
        //处理搜索业务
        //获取关键字、当前页、每页多少条数据
        String keywords = String.valueOf(paramMap.get("keywords"));
        Integer pageNo = Integer.parseInt(String.valueOf(paramMap.get("pageNo")));
        Integer pageSize = Integer.parseInt(String.valueOf(paramMap.get("pageSize")));
        //条件查询
        Criteria criteria = new Criteria("item_keywords").is(keywords);
        //创建查询对象
        SimpleQuery simpleQuery = new SimpleQuery();
        //添加条件
        simpleQuery.addCriteria(criteria);
        //分页查询，每次差多少，从第几条开始查
        if (pageNo==null || pageNo<=0){
            pageNo = 1;
        }
        //计算出当前的查询位置
      Integer statr = (pageNo-1)*pageSize;
        //设置从第几条数据开始查询
        simpleQuery.setOffset(statr);
        //查询多少条数据
        simpleQuery.setRows(pageSize);
        //查询  返回结果
        ScoredPage<Item> items = solrTemplate.queryForPage(simpleQuery, Item.class);
        //封装结果集
        HashMap<String, Object> resMap = new HashMap<>();
        resMap.put("rows",items.getContent());
        resMap.put("total",items.getTotalElements());
        resMap.put("totalPages",items.getTotalPages());
        return resMap;
    }
}
