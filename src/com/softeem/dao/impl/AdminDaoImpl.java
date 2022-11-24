package com.softeem.dao.impl;

import com.softeem.bean.Admin;
import com.softeem.bean.User;
import com.softeem.dao.AdminDao;
import com.softeem.dao.UserDao;
import com.softeem.utils.BaseDao;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Chris
 */
public class AdminDaoImpl extends BaseDao implements AdminDao {
    @Override
    public Admin queryAdminByUsername(String username) throws SQLException{
        String sql = "select * from  t_admin where username = ?";

        return queryRunner.query(sql,new BeanHandler<>(Admin.class),username);
    }

    @SneakyThrows
    @Override
    public Admin queryAdminByUsernameAndPassword(String username, String password) {
        String sql = "select * from t_admin where username = ? and password = ?";

        return queryRunner.query(sql,new BeanHandler<>(Admin.class),username,password);
    }

    @Override
    public List<Admin> findAll() throws SQLException {
        return queryRunner.query("select * from  t_admin ", new BeanListHandler<>(Admin.class));

    }

    @Override
    public void save(Admin user) throws SQLException {
        String sql = "insert into t_admin values(null,?,?,?)";

        //queryRunner.update(sql, user.getUsername(),user.getPassword(),user.getEmail());

       Long id =queryRunner.insert(sql,new ScalarHandler<Long>(),user.getUsername(),user.getPassword(),user.getEmail());
        user.setId(id.intValue());
    }

    @Override
    public void updateById(Admin user) throws SQLException {
       queryRunner.update("updatet_admin set username = ? , password = ? , email = ? where id = ? ",
                user.getUsername(),user.getPassword(),user.getEmail(),user.getId());
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        queryRunner.update("delete  from t_admin where id = ?",id);

    }

    @Override
    public Admin findById(Integer id) throws SQLException {
         return queryRunner.query("select * from t_admin where id = ?", new BeanHandler<>(Admin.class),id);
    }

    @Override
    public List<Admin> page(Integer pageNumber) throws SQLException {
        return queryRunner.query("select * from t_admin limit ? , ?", new BeanListHandler<>(Admin.class), (pageNumber - 1) * pageSize, pageSize);


    }

    @Override
    public Integer pageRecord() throws SQLException {
         return queryRunner.query("select count(*) from t_admin", new ScalarHandler<Long>()).intValue();

    }
}
