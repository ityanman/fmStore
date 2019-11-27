package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.item.ItemCatDao;
import com.fmjava.core.pojo.item.ItemCat;
import com.fmjava.core.pojo.item.ItemCatQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class ItemCatServiceImpl implements ItemCatService{
    @Autowired
    ItemCatDao itemCatDao;
    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        ItemCatQuery itemCatQuery = new ItemCatQuery();
        ItemCatQuery.Criteria criteria = itemCatQuery.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<ItemCat> itemCatList = itemCatDao.selectByExample(itemCatQuery);
        return itemCatList;
}

//根据id查询ParentID
    @Override
    public Long findParentIdById(Long id) {
        return itemCatDao.findParentIdById(id);
    }

    //根据id查询一条分类
    @Override
    public ItemCat findOneCategory(Long id) {
        return itemCatDao.selectByPrimaryKey(id);
    }
}
