package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.specification.SpecificationDao;
import com.fmjava.core.dao.specification.SpecificationOptionDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.SpecEntity;
import com.fmjava.core.pojo.specification.Specification;
import com.fmjava.core.pojo.specification.SpecificationOption;
import com.fmjava.core.pojo.specification.SpecificationOptionQuery;
import com.fmjava.core.pojo.specification.SpecificationQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Transactional
@Service
public class SpecServiceImpl implements SpecService{
    @Autowired
    private SpecificationDao specDao;
    @Autowired
    private SpecificationOptionDao specOptionDao;
    @Override
    public PageResult findPage(Integer page, Integer pageSize, Specification specification) {
        PageHelper.startPage(page,pageSize);
        SpecificationQuery Query = new SpecificationQuery();
        if (specification!=null) {
            SpecificationQuery.Criteria criteria = Query.createCriteria();
            if (specification.getSpecName()!=null && !"".equals(specification.getSpecName())){
                criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
            }
        }
        Page<Specification> specList =(Page<Specification>)specDao.selectByExample(Query);
        System.out.println("来到了dao");
        return new PageResult(specList.getResult(),specList.getTotal());
    }

    //save
    @Override
    public void saveSpec(SpecEntity specEntity) {
        //1添加规格对象
        specDao.insertSelective(specEntity.getSpecification());
        if (specEntity.getSpecification()!=null) {
            //2添加规格选项
            for (SpecificationOption option : specEntity.getSpecOptionList()) {
                //设置规格选项外键
                option.setSpecId(specEntity.getSpecification().getId());
                specOptionDao.insertSelective(option);
            }
        }
    }

    @Override
    public SpecEntity findSpecById(Long id) {
        //查询出规格
        Specification specification = specDao.selectByPrimaryKey(id);
        //根据规格id查询出规格选项
        SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = optionQuery.createCriteria();
        criteria.andSpecIdEqualTo(specification.getId());
        List<SpecificationOption> specificationOptions = specOptionDao.selectByExample(optionQuery);
        //将查询出的内容封装成SpecEntity返回
        SpecEntity specEntity = new SpecEntity();
        specEntity.setSpecification(specification);
        specEntity.setSpecOptionList(specificationOptions);
        return specEntity;
    }

    //更新
    @Override
    public void updateSpec(SpecEntity specEntity) {
        //更新规格名称
        specDao.updateByPrimaryKeySelective(specEntity.getSpecification());
        //打破关系
        SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
        SpecificationOptionQuery.Criteria criteria = optionQuery.createCriteria();
        criteria.andSpecIdEqualTo(specEntity.getSpecification().getId());
        specOptionDao.deleteByExample(optionQuery);
        //在建立关系
        for (SpecificationOption option : specEntity.getSpecOptionList()) {
            //设置每一个规格选项对应规格的id
            option.setSpecId(specEntity.getSpecification().getId());
            //保存每一个规格选项
            specOptionDao.insertSelective(option);
        }

    }

    //删除
    @Override
    public void deleteBrand(Long[] ids) {
        if (ids!=null){
            //先删除规格
            for (Long id : ids) {
                specDao.deleteByPrimaryKey(id);
                //删除规格选项
                SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = optionQuery.createCriteria();
                criteria.andSpecIdEqualTo(id);
                specOptionDao.deleteByExample(optionQuery);
            }

        }
    }
}
