package com.fmjava.service;

import com.fmjava.core.pojo.seller.Seller;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class UserDetailServiceImpl implements UserDetailsService {

    @Setter
    private SellerService sellerService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //定义一个权限集合
        ArrayList<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //1.判断用户名是否为空，如果为空直接返回
        if(username==null){
            return null;
        }
        //2根据用户名到数据库中查询是否有相应的对象
        Seller seller = sellerService.findSellerByID(username);
        //3如果用户对象查询不到就返回null
        System.out.println("密码市"+seller.getPassword());
        if (seller!=null){
            //3判断是否已经审核通过，如果未通过那就返回空
            if("1".equals(seller.getStatus())){
                //5返回springSecuity的User对象，将这个用户的用户名，密码，所具有的权限集合返回
                return new User(username,seller.getPassword(),authList);
            }
        }
        System.out.println(seller);
        return null;
    }
}
