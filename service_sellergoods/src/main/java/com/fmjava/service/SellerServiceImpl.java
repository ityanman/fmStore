package com.fmjava.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmjava.core.dao.seller.SellerDao;
import com.fmjava.core.pojo.entity.PageResult;
import com.fmjava.core.pojo.seller.Seller;
import com.fmjava.core.pojo.seller.SellerQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerDao sellerDao;

    //商户注册
    @Override
    public void register(Seller seller) {
        seller.setCreateTime(new Date());
        //审核状态为0的时候未审核
        seller.setStatus("0");
        sellerDao.insertSelective(seller);
    }

    //查找未审核商家
    @Override
    public PageResult findPage(Integer page, Integer pageSize, Seller seller) {
        PageHelper.startPage(page,pageSize);
        SellerQuery query = new SellerQuery();
        SellerQuery.Criteria criteria = query.createCriteria();
        if (seller!=null){
            if (seller.getStatus()!=null && !"".equals(seller.getStatus())){
                criteria.andStatusEqualTo(seller.getStatus());
            }
            if (seller.getName() != null && !"".equals(seller.getName())){
                criteria.andNameLike("%"+seller.getName()+"%");
            }
            if (seller.getNickName()!=null && !"".equals(seller.getNickName())){
                criteria.andNickNameLike("%"+seller.getNickName()+"%");
            }
        }
        Page<Seller> sellerList = (Page<Seller>)sellerDao.selectByExample(query);
        return new PageResult(sellerList.getResult(),sellerList.getTotal());
    }

    //根据id查询商家
    @Override
    public Seller findSellerByID(String sellerId) {
        Seller seller = sellerDao.selectByPrimaryKey(sellerId);
        return seller;
    }

    //审核商家
    @Override
    public void statusSeller(String sellerId, String status) {
        Seller seller = new Seller();
        seller.setStatus(status);
        seller.setSellerId(sellerId);
        sellerDao.updateByPrimaryKeySelective(seller);
    }

}
