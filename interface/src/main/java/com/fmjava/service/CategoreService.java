package com.fmjava.service;

import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.ad.ContentCategory;
import com.fmjava.core.pojo.entity.PageResult;

import java.util.List;

public interface CategoreService {
    PageResult findPage(Integer page, Integer rows, ContentCategory category);

    void add(ContentCategory category);

    ContentCategory findOne(Long id);

    void update(ContentCategory category);

    void delete(Long[] ids);

    List<ContentCategory> findAll();
}
