package com.fmjava.service;

import com.fmjava.core.pojo.ad.Content;
import com.fmjava.core.pojo.entity.PageResult;

import java.util.List;

public interface ContentService {
    List<Content> findByCategoryId(Long categoryId);
    public List<Content> findByCategoryIdFromRedis(Long categoryId);

    PageResult findPage(Integer page, Integer rows,Content content);

    void add(Content content);

    Content findOne(Long id);

    void update(Content content);

    void deleteContent(Long[] id);

}
