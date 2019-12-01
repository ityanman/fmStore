package com.fmjava.service;

import com.fmjava.core.pojo.ad.Content;

import java.util.List;

public interface ContentService {
    List<Content> findByCategoryId(long categoryId);
    public List<Content> findByCategoryIdFromRedis(Long categoryId);
}
