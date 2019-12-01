package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.ad.ContentDao;
import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.ad.ContentQuery;
import com.fmjava.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
@Service
public class ContentServiceImpl implements ContentService{
    @Autowired
    ContentDao contentDao;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public  List<Content> findByCategoryId(long categoryId) {
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Content> list = contentDao.selectByExample(query);
        return list;
    }
    @Override
    public List<Content> findByCategoryIdFromRedis(Long categoryId) {
        List<Content> contentList = ( List<Content>)redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).get(categoryId);
        if (contentList==null){
            //redis没有数据，从数据库中取出存入redis
            Content content = contentDao.selectByPrimaryKey(categoryId);
             contentList = this.findByCategoryId(categoryId);
            redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).put(categoryId,contentList);
        }
        return contentList;
    }
}
