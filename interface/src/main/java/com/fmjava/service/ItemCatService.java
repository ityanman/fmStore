package com.fmjava.service;

import com.fmjava.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {
    public List<ItemCat> findByParentId(Long parentId);

    Long findParentIdById(Long id);

    ItemCat findOneCategory(Long id);
}

