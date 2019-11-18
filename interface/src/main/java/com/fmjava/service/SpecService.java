package com.fmjava.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.SpecEntity;
import com.fmjava.core.pojo.specification.Specification;

public interface SpecService {
    PageResult findPage(Integer page, Integer pageSize, Specification specification);

    void saveSpec(SpecEntity specEntity);

    SpecEntity findSpecById(Long id);

    void updateSpec(SpecEntity specEntity);

    void deleteBrand(Long[] ids);
}
