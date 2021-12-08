package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImp;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.JedisUtil;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class UserServiceImp implements UserService {
    /*
    登录
     */
    @Override
    public ResultInfo UserLogin(String username, String password) {
        User user = new UserDaoImp().login(username);
        ResultInfo resultInfo = new ResultInfo();
        if (user != null){
            //用户已注册
            String s = user.getPassword();
            if (s.equals(password)){
                //密码正确，登录成功
                resultInfo.setFlag(true);
                resultInfo.setErrorMsg("密码正确");
            }else {
                //密码错误，提示重新输入
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("密码错误");
            }
        }else {
            //用户未注册,提示去注册
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户未注册");
        }
        return resultInfo;
    }
    /*
    注册
     */
    @Override
    public ResultInfo register(User user) {
        //邮件激活，设置激活码
        user.setCode(UuidUtil.getUuid());
        //设置激活状态
        user.setStatus("N");

        //邮件正文
        String body = "<a href='http://localhost:80/travel/actionUserServlet?code="+user.getCode()+"'>点击激活</a>";
        MailUtils.sendMail(user.getEmail(),body,"激活邮件");

        return new UserDaoImp().register(user);
    }
    /*
    邮箱验证
     */
    @Override
    public boolean action(String code) {
        UserDao userDao = new UserDaoImp();
        User user = userDao.findUserByCode(code);
        if (user != null){
            userDao.UpdateUserStatus(user);
            return true;
        }else {
            return false;
        }
    }
    /*
    分类查询
     */
    @Override
    public List<Category> findAll() {
        Jedis jedis = JedisUtil.getJedis();
        Set<Tuple> category = jedis.zrangeWithScores("category",0,-1);
        List<Category> List = null;
        if (category == null || category.size()==0){
            List = new UserDaoImp().findAll();
            for (Category category1 : List) {
                jedis.zadd("category",category1.getCid(),category1.getCname());
            }

        }else {
            List = new ArrayList<Category>();
            for (Tuple tuple : category) {
                Category category1 = new Category();
                category1.setCid((int) tuple.getScore());
                category1.setCname(tuple.getElement());
                List.add(category1);
            }
        }

        return List;
    }

    @Override
    public PageBean findRoute(PageBean pageBean) {
        UserDao userDao = new UserDaoImp();

        PageBean route = userDao.findRoute(pageBean);
        int totalCount = route.getTotalCount();
        int pageSize = route.getPageSize();
        int totalPage;
        if (totalCount%pageSize != 0){
            double floor = Math.floor(totalCount / pageSize);
            totalPage = (int) (floor+1);
        }else {
            totalPage = totalCount/pageSize;
        }
        route.setTotalPage(totalPage);
        return route;
    }

    @Override
    public Route findOne(int rid) {
        UserDao userDao = new UserDaoImp();
        List<Route> one = userDao.findOne(rid);
        if (one != null && one.size()>0){
            return one.get(0);
        }
        return null;
    }

}
