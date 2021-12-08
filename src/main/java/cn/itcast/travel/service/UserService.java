package cn.itcast.travel.service;

import cn.itcast.travel.domain.*;

import java.util.List;

public interface UserService {
    ResultInfo UserLogin(String username, String password);
    ResultInfo register(User user);

    boolean action(String code);
    List<Category> findAll();   //分类查询

    PageBean findRoute(PageBean pageBean);    //分类数据查询
    Route findOne(int rid);
}
