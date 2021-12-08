package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.*;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.List;

public class UserDaoImp implements UserDao {
    @Override
    public User login(String username) {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        String sql = "select * from tab_user where username = "+"'"+username+"'";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        for (User user : users) {
            return user;
        }

        return null;
    }

    @Override
    public ResultInfo register(User user) {
        String username = user.getUsername();
        ResultInfo resultInfo = new ResultInfo();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        String sql = "select uid from tab_user where username = "+"'"+username+"'";
        try {
            connection = JDBCUtils.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()){
                //用户名已存在
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("用户名已存在");
                return resultInfo;
            }else {
                //用户名不存在，可以注册
                String password = user.getPassword();
                String name = user.getName();
                String birthday = user.getBirthday();
                String sex = user.getSex();
                String telephone = user.getTelephone();
                String email = user.getEmail();
                String status = user.getStatus();
                String code = user.getCode();
                sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values ("
                        +"'"+username+"',"+"'"+password+"',"+"'"+name+"',"+"'"+birthday+"',"+"'"+sex+"',"+"'"+telephone+"',"
                        +"'"+email+"',"+"'"+status+"',"+"'"+code+"'"+")";
                statement.executeUpdate(sql);
                resultInfo.setFlag(true);
                resultInfo.setErrorMsg("注册成功");
                return resultInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(connection,statement,resultSet);
        }

        return null;
    }
    /*
    查找激活码是否存在
     */
    @Override
    public User findUserByCode(String code) {
        String sql = "select * from tab_user where code = ?";
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        User query = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);

        return query;
    }

    @Override
    public void UpdateUserStatus(User user) {
        String sql = "update tab_user set status = 'Y' where uid = ?";
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        template.update(sql,user.getUid());
    }

    @Override
    public List<Category> findAll() {
        String sql = "select * from tab_category";
        JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
        List<Category> categoryList = template.query(sql, new BeanPropertyRowMapper<>(Category.class));
        return categoryList;
    }

    @Override
    public PageBean findRoute(PageBean pageBean) {
        int cid = pageBean.getCid();
        int pageSize = pageBean.getPageSize();
        int currentPage = pageBean.getCurrentPage();
        int count = 0;
        String sql = "select * from tab_route where cid=? limit ?,?";
        String sql2 = "select count(rid) from tab_route where cid=? ";
        List<Route> routeList = null;
        int index = (currentPage-1) * pageSize;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setInt(1,cid);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                count = resultSet.getInt(1);
                pageBean.setTotalCount(count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(connection,preparedStatement,resultSet);
        }
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        if (count<currentPage*pageSize){
            int i = count -index;
            routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), cid, index, i);
        }else {
            routeList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), cid, index, pageSize);
        }
        pageBean.setList(routeList);

        return pageBean;
    }

    @Override
    public List<Route> findOne(int rid) {
        String sql = "select * from tab_route where rid=?";
        JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());
        List<Route> query = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Route.class), rid);
        return query;
    }
}
