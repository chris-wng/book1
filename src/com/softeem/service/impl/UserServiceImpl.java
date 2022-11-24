package com.softeem.service.impl;

import com.softeem.bean.User;
import com.softeem.dao.UserDao;
import com.softeem.dao.impl.UserDaoImpl;
import com.softeem.service.UserService;

import java.sql.SQLException;
//业务层
public class UserServiceImpl implements UserService {

    private UserDao userDao =  new UserDaoImpl();
    @Override
    public void registUser(User user) throws SQLException {
        userDao.save(user);
    }

    @Override
    public User login(User user) throws SQLException {
        return userDao.queryUserByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    /**
     *
     * @param username
     * @return 返回true表示用户已存在
     * @throws SQLException
     */
    @Override
    public boolean existsUsername(String username) throws SQLException {

        return userDao.queryUserByUsername(username)!=null;
    }
}
