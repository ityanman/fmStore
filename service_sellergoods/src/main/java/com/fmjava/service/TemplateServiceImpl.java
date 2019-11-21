package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.template.TypeTemplateDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.template.TypeTemplate;
import com.fmjava.core.pojo.template.TypeTemplateQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TypeTemplateDao typeTemplateDao;
    @Override
    public PageResult findPage(Integer page, Integer pageSize, TypeTemplate template) {
        PageHelper.startPage(page,pageSize);
        TypeTemplateQuery query = new TypeTemplateQuery();
        if (template!=null){
            TypeTemplateQuery.Criteria criteria = query.createCriteria();
            if (template.getName()!=null && !"".equals(template.getName())){
                criteria.andNameEqualTo("%"+template.getName()+"%");
            }
        }
        Page<TypeTemplate> typeTemplates = (Page<TypeTemplate>)typeTemplateDao.selectByExample(query);
        return new PageResult(typeTemplates.getResult(),typeTemplates.getTotal());
    }

    //添加模板
    @Override
    public void add(TypeTemplate template) {
        typeTemplateDao.insertSelective(template);
    }

    //根据模板id回显数据
    @Override
    public TypeTemplate findTempById(Long id) {
        return typeTemplateDao.selectByPrimaryKey(id);
    }

    //更新模板
    @Override
    public void update(TypeTemplate template) {

        typeTemplateDao.updateByPrimaryKeySelective(template);
    }

    //删除模板
    @Override
    public void delete(Long[] ids) {
        if (ids!=null){
            TypeTemplateQuery query = new TypeTemplateQuery();
            TypeTemplateQuery.Criteria criteria = query.createCriteria();
            criteria.andIdIn(Arrays.asList(ids));
            typeTemplateDao.deleteByExample(query);
        }
    }
}
