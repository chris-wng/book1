package com.softeem.service;

import com.softeem.bean.Admin;
import com.softeem.bean.User;

import java.sql.SQLException;

public interface AdminService {


    public void registUser(Admin user) throws SQLException;


    public Admin login(Admin user) throws SQLException;

    /**
     *	检查 用户名是否可用
     *	@param username
     *	@return 返回true 表示用户名已存在，返回false 表示用户名可用
     */
    public boolean existsUsername(String username) throws SQLException;

}
