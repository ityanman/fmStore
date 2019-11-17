package com.fmjava.service;

import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.good.Brand;

import java.util.List;

public interface BrandService {
    public List<Brand> findAllBrand();

    PageResult findPage(Integer page, Integer pageSize,Brand brand);

    void add(Brand brand);

    //根据id查找brand
    Brand findById(Long id);

    void updateBrand(Brand brand);

    //根据id删除brand
    void deleteBrand(Long[] ids);
}
