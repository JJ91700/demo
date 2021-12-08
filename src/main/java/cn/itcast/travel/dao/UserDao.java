package cn.itcast.travel.dao;

import cn.itcast.travel.domain.*;

import java.util.List;

public interface UserDao {
    User login(String username);
    ResultInfo register(User user);

    User findUserByCode(String code);

    void UpdateUserStatus(User user);
    List<Category> findAll();

    PageBean findRoute(PageBean pageBean);
    List<Route> findOne(int rid);
}
