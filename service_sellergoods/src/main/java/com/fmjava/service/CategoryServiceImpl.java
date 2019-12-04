package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.ad.ContentCategoryDao;
import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.ad.ContentCategory;
import com.fmjava.core.pojo.ad.ContentCategoryQuery;
import com.fmjava.core.pojo.entity.PageResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CategoryServiceImpl implements CategoreService {
    @Autowired
    ContentCategoryDao contentCategoryDao;
    //广告列表
    @Override
    public PageResult findPage(Integer page, Integer rows, ContentCategory category) {
        PageHelper.startPage(page,rows);
        ContentCategoryQuery categoryQuery = new ContentCategoryQuery();
        if (category!=null){
            ContentCategoryQuery.Criteria criteria = categoryQuery.createCriteria();
            if (category.getName() != null && !"".equals(category.getName())) {
                criteria.andNameLike("%"+category.getName()+"%");
            }
        }
        Page<ContentCategory> contentList = ( Page<ContentCategory>)contentCategoryDao.selectByExample(categoryQuery);
        return new PageResult(contentList.getResult(),contentList.getTotal());
    }

    //添加
    @Override
    public void add(ContentCategory category) {
        contentCategoryDao.insertSelective(category);
    }

    //编辑--数据回显
    @Override
    public ContentCategory findOne(Long id) {
        return contentCategoryDao.selectByPrimaryKey(id);
    }

    //更新分类
    @Override
    public void update(ContentCategory category) {
        contentCategoryDao.updateByPrimaryKey(category);
    }

    //删除分类
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            contentCategoryDao.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<ContentCategory> findAll() {
        List<ContentCategory> contentCategories = contentCategoryDao.selectByExample(null);
        return contentCategories;
    }
}
