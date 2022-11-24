package com.softeem.dao.impl;

import com.softeem.bean.Book;
import com.softeem.bean.OrderItem;
import com.softeem.dao.OrderItemDao;
import com.softeem.utils.BaseDao;
import com.softeem.utils.JdbcUtils;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderItemDaoImpl extends BaseDao implements OrderItemDao {


    @Override
    public List<OrderItem> findAll() throws SQLException {
        return null;
    }

    @Override
    public void save(OrderItem orderItem) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        String sql = "insert into t_order_item(`name`,`count`,`price`,`total_price`,`order_id`) values(?,?,?,?,?)";
        queryRunner.update(connection,sql,orderItem.getName(),orderItem.getCount(),orderItem.getPrice(),orderItem.getTotalPrice(),orderItem.getOrderId());
    }

    @Override
    public void updateById(OrderItem orderItem) throws SQLException {

    }

    @Override
    public void deleteById(Integer id) throws SQLException {

    }

    @Override
    public OrderItem findById(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<OrderItem> page(Integer pageNumber) throws SQLException {
        return null;
    }

    @Override
    public Integer pageRecord() throws SQLException {
        return null;
    }

    @Override
    public OrderItem findByOrderId(String order_id) throws SQLException {
        //开启下划线->驼峰转换所用 - start
        BeanProcessor bean = new GenerousBeanProcessor();
        RowProcessor processor = new BasicRowProcessor(bean);
        //开启下划线->驼峰转换所用 - end
        BeanHandler<OrderItem> handler = new BeanHandler<>(OrderItem.class,processor);
        String sql = "SELECT * FROM `t_order_item` where order_id = ?;";
        return queryRunner.query(sql,handler,order_id);
    }
}
