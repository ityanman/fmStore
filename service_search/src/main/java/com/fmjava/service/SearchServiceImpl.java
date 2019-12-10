package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.pojo.item.Item;
import com.fmjava.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.SolrDataQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.ScoredPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SearchServiceImpl implements SearchService{
    @Autowired
    SolrTemplate solrTemplate;
    @Autowired
    RedisTemplate redisTemplate;
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
        List categoryList = this.findCategory(paramMap);
        resMap.put("categoryList",categoryList);
        //根据分类名称查询品牌与规格
        //条件当中有分类名称
        String cagegory = String.valueOf(paramMap.get("cagegory"));
        if (cagegory!=null && "".equals(cagegory)){
            Map BrandAndMap = this.findBrandAndSpecWithCategory(cagegory);
            resMap.putAll(BrandAndMap);
        }else {
             cagegory = (String)categoryList.get(0);
            Map BrandAndMap = this.findBrandAndSpecWithCategory(cagegory);
            resMap.putAll(BrandAndMap);
        }
        return resMap;
    }

    //根据分类名称获取规格与品牌
    private Map findBrandAndSpecWithCategory(String categoryName){
        //根据名称取出分类模板ID
        Long typeID  = (Long)redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).get(categoryName);
        //根据模板ID查询出品牌与规格
        if (typeID!=null){
            List<Map> BrandList = ( List<Map>)redisTemplate.boundHashOps(Constants.BRAND_LIST_REDIS).get(typeID);
            List<Map> SpecList = ( List<Map>)redisTemplate.boundHashOps(Constants.SPEC_LIST_REDIS).get(typeID);
            Map map = new HashMap();
            map.put("BrandList",BrandList);
            map.put("SpecList",SpecList);
            return map;
        }
        return null;
    }
    private List findCategory(Map paramMap){
        ArrayList<String> categoryList = new ArrayList<>();
        //分类查询
        //获取当前关键字
        String keyWords = String.valueOf(paramMap.get("keywords"));
        //创建查询对象
        SimpleQuery query = new SimpleQuery();
        Criteria criteria = new Criteria("item_keyword").is(keyWords);
        query.addCriteria(criteria);
        //查询分类时要进行分组操作(重复的分类)
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);
        //分组查询
        GroupPage<Item> items = solrTemplate.queryForGroupPage(query, Item.class);
        GroupResult<Item> item_category = items.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = item_category.getGroupEntries();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue();
            categoryList.add(groupValue);
        }
        return categoryList;
    }
}
