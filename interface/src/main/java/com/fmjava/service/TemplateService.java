package com.fmjava.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.template.TypeTemplate;

public interface TemplateService {
    PageResult findPage(Integer page, Integer pageSize, TypeTemplate template);

    void add(TypeTemplate template);

    //根据模板id查询数据
    TypeTemplate findTempById(Long id);

    //更新模板
    void update(TypeTemplate template);

    //删除模板
    void delete(Long[] ids);
}
