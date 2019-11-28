package com.fmjava.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.entity.Result;
import com.fmjava.core.pojo.template.TypeTemplate;
import com.fmjava.service.TemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/temp")
public class TemplateController {

    @Reference
    TemplateService templateService;
    @RequestMapping("/findOne")
public TypeTemplate findOne(Long id){
        return templateService.findTempById(id);
    }
    @RequestMapping("/findSpecOption")
    public List<Map> findSpecOption(Long id){
        List<Map> specOption = templateService.findSpecOption(id);
        for (Map map : specOption) {
            System.out.println(map);
        }
        return specOption;
    }

}
