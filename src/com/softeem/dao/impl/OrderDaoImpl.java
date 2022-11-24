package com.softeem.dao.impl;

import com.softeem.bean.Order;
import com.softeem.dao.OrderDao;
import com.softeem.utils.BaseDao;
import com.softeem.utils.JdbcUtils;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDaoImpl extends BaseDao implements OrderDao {
    @Override
    public List<Order> findAll() throws SQLException {
       return null;
    }

    @Override
    public void save(Order order) throws SQLException {
        Connection connection = JdbcUtils.getConnection();

        queryRunner.update( connection,"insert into t_order(`order_id`,`create_time`,`price`,`status`,`user_id`) values(?,?,?,?,?)",order.getOrderId(),order.getCreateTime()
        ,order.getPrice(),order.getStatus(),order.getUserId());
    }

    @Override
    public void updateById(Order order) throws SQLException {

    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }

    @Override
    public Order findById(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<Order> page(Integer pageNumber) throws SQLException {
        //开启下划线->驼峰转换所用 - start
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        //开启下划线->驼峰转换所用 - end
        String sql = "select * from t_order order by create_time desc limit ? , ?";
     return queryRunner.query(sql, new BeanListHandler<Order>(Order.class, processor), (pageNumber - 1) * pageSize, pageSize);


    }

    @Override
    public Integer pageRecord() throws SQLException {
        String sql ="select count(*) from t_order";
        ScalarHandler<Long> handler = new ScalarHandler<>();
        Long query = queryRunner.query(sql, handler);
        return query.intValue();
    }
}
