package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.ad.ContentDao;
import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.ad.ContentQuery;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.utils.Constants;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class ContentServiceImpl implements ContentService{
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private ContentDao contentDao;
    @Override
    public List<Content> findByCategoryId(Long categoryId) {
        ContentQuery contentQuery = new ContentQuery();
        ContentQuery.Criteria criteria = contentQuery.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<Content> contentList = contentDao.selectByExample(contentQuery);
        return contentList;
    }

    //从redis中读取数据
    @Override
    public List<Content> findByCategoryIdFromRedis(Long categoryId) {
        List<Content> contentList = (List<Content>)redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).get(categoryId);
        if (contentList==null){
             contentList = this.findByCategoryId(categoryId);
             redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).put(categoryId,contentList);
            System.out.println("数据库");
        }
        System.out.println("从redis中取数据");
        return contentList;
    }


    @Override
    public PageResult findPage(Integer page, Integer rows,Content content) {
        PageHelper.startPage(page, rows);
        ContentQuery query = new ContentQuery();
        ContentQuery.Criteria criteria = query.createCriteria();
        if (content != null) {
            if (content.getTitle() != null && !"".equals(content.getTitle())) {
                criteria.andTitleLike("%"+content.getTitle()+"%");
            }
        }
        Page<Content> contentList = (Page<Content>)contentDao.selectByExample(query);
        return new PageResult(contentList.getResult(),contentList.getTotal());
    }

    //添加
    @Override
    public void add(Content content) {
        //更新redis
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
        contentDao.insertSelective(content);
    }

    //回显
    @Override
    public Content findOne(Long id) {
        return contentDao.selectByPrimaryKey(id);
    }

    //更新
    @Override
    public void update(Content content) {
        //根据id查询原来的广告对象
        Content oldContent = contentDao.selectByPrimaryKey(content.getId());
        //删除redis中的数据
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(oldContent.getCategoryId());
        redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
        contentDao.updateByPrimaryKeySelective(content);
    }

    //删除
    @Override
    public void deleteContent(Long[] id) {
        for (Long ContentId : id) {
            //根据id到数据库中查寻对象
            Content content = contentDao.selectByPrimaryKey(ContentId);
            //更新redis
            redisTemplate.boundHashOps(Constants.CONTENT_LIST_REDIS).delete(content.getCategoryId());
            contentDao.deleteByPrimaryKey(ContentId);
        }
    }
}