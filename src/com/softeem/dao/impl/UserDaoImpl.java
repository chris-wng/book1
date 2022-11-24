package com.softeem.dao.impl;

import com.softeem.bean.User;
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
public class UserDaoImpl extends BaseDao implements UserDao {
    @Override
    public User queryUserByUsername(String username) throws SQLException{
        String sql = "select * from t_user where username = ?";

        return queryRunner.query(sql,new BeanHandler<>(User.class),username);
    }

    @SneakyThrows
    @Override
    public User queryUserByUsernameAndPassword(String username, String password) {
        String sql = "select * from t_user where username = ? and password = ?";

        return queryRunner.query(sql,new BeanHandler<>(User.class),username,password);
    }

    @Override
    public List<User> findAll() throws SQLException {
        return queryRunner.query("select * from t_user ", new BeanListHandler<>(User.class));

    }

    @Override
    public void save(User user) throws SQLException {
        String sql = "insert into t_user values(null,?,?,?)";

        //queryRunner.update(sql, user.getUsername(),user.getPassword(),user.getEmail());

       Long id =queryRunner.insert(sql,new ScalarHandler<Long>(),user.getUsername(),user.getPassword(),user.getEmail());
        user.setId(id.intValue());
    }

    @Override
    public void updateById(User user) throws SQLException {
       queryRunner.update("update t_user set username = ? , password = ? , email = ? where id = ? ",
                user.getUsername(),user.getPassword(),user.getEmail(),user.getId());
    }

    @Override
    public void deleteById(Integer id) throws SQLException {
        queryRunner.update("delete  from t_user where id = ?",id);

    }

    @Override
    public User findById(Integer id) throws SQLException {
         return queryRunner.query("select * from t_user where id = ?", new BeanHandler<>(User.class),id);
    }

    @Override
    public List<User> page(Integer pageNumber) throws SQLException {
        return queryRunner.query("select * from t_user limit ? , ?", new BeanListHandler<>(User.class), (pageNumber - 1) * pageSize, pageSize);


    }

    @Override
    public Integer pageRecord() throws SQLException {
         return queryRunner.query("select count(*) from t_user", new ScalarHandler<Long>()).intValue();

    }
}
