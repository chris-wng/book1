package com.softeem.service.impl;

import com.softeem.bean.Admin;
import com.softeem.bean.User;
import com.softeem.dao.AdminDao;
import com.softeem.dao.UserDao;
import com.softeem.dao.impl.AdminDaoImpl;
import com.softeem.dao.impl.UserDaoImpl;
import com.softeem.service.AdminService;
import com.softeem.service.UserService;

import java.sql.SQLException;

//业务层
public class AdminServiceImpl implements AdminService {

    private AdminDao userDao =  new AdminDaoImpl();
    @Override
    public void registUser(Admin user) throws SQLException {
        userDao.save(user);
    }

    @Override
    public Admin login(Admin user) throws SQLException {
        return userDao.queryAdminByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    /**
     *
     * @param username
     * @return 返回true表示用户已存在
     * @throws SQLException
     */
    @Override
    public boolean existsUsername(String username) throws SQLException {

        return userDao.queryAdminByUsername(username)!=null;
    }
}
