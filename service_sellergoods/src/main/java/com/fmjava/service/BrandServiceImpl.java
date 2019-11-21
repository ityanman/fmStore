package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.good.BrandDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.good.Brand;
import com.fmjava.core.pojo.good.BrandQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandDao brandDao;

    @Override
    public List<Brand> findAllBrand() {
       return  brandDao.selectByExample(null);
    }

    @Override
    public PageResult findPage(Integer page, Integer pageSize,Brand brand) {
        //利用分页助手实现分页, 第一个参数:当前页, 第二个参数: 每页展示数据条数
        PageHelper.startPage(page,pageSize);
        BrandQuery brandQuery = new BrandQuery();
        brandQuery.setOrderByClause("id desc");
        if (brand!=null){
            BrandQuery.Criteria criteria = brandQuery.createCriteria();
            if (brand.getName()!=null && !"".equals(brand.getName())){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar()!=null && !"".equals(brand.getFirstChar())){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }
        Page<Brand> brandList = (Page<Brand>)brandDao.selectByExample(brandQuery);
        return new PageResult(brandList.getResult(),brandList.getTotal());
    }

    @Override
    public void add(Brand brand) {
         brandDao.insertSelective(brand);
    }

    //根据id查找brand
    @Override
    public Brand findById(Long id) {
        Brand brand = brandDao.selectByPrimaryKey(id);
        return brand;
    }

    //更新品牌
    @Override
    public void updateBrand(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    //删除品牌
    @Override
    public void deleteBrand(Long[] ids) {
        if (ids!=null){
            BrandQuery brandQuery = new BrandQuery();
            BrandQuery.Criteria criteria = brandQuery.createCriteria();
            criteria.andIdIn(Arrays.asList(ids));
            brandDao.deleteByExample(brandQuery);
        }

    }

    //查询品牌选项
    @Override
    public List<Map> selectOptionList() {
        return brandDao.selectOptionList();
    }
}
