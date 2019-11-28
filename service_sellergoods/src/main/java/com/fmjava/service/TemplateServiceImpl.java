package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fmjava.core.dao.specification.SpecificationOptionDao;
import com.fmjava.core.dao.template.TypeTemplateDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.specification.SpecificationOption;
import com.fmjava.core.pojo.specification.SpecificationOptionQuery;
import com.fmjava.core.pojo.template.TypeTemplate;
import com.fmjava.core.pojo.template.TypeTemplateQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mysql.cj.xdevapi.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TemplateServiceImpl implements TemplateService {
    @Autowired
    TypeTemplateDao typeTemplateDao;
    @Autowired
    SpecificationOptionDao specificationOptionDao;
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

    //查询规格
    @Override
    public List<Map> findSpecOption(Long id) {
        //根据id查询模板对象
        TypeTemplate template = typeTemplateDao.selectByPrimaryKey(id);
        if (id!=null){
            //1从模板对象中取规格集合数据，获取到的市json字符串
            String specIds = template.getSpecIds();
            //2将json转换为Java中的list对象
            List<Map> maps = JSON.parseArray(specIds,Map.class);
            //3遍历集合对象
            for (Map map : maps) {
                //4遍历过程中根据规格id获取对应规格选项集合
                Long specId = Long.parseLong(String.valueOf(map.get("id")));
                SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
                SpecificationOptionQuery.Criteria criteria = optionQuery.createCriteria();
                criteria.andSpecIdEqualTo(specId);
                //根据规格id获取到规格选项的数据
                List<SpecificationOption> specOptions = specificationOptionDao.selectByExample(optionQuery);
                //将规格选项封装到原来的map中
                map.put("spec",specOptions);
            }
            return maps;
        }
           return null;
    }
}
