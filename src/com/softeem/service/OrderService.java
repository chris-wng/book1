package com.softeem.service;

import com.softeem.bean.Book;
import com.softeem.bean.Order;
import com.softeem.bean.OrderItem;
import com.softeem.utils.Page;

import java.sql.SQLException;

public interface OrderService {
    public String createOrder(Cart cart,Integer userId) throws SQLException;


    public Page<Order> page(Integer pageNo,Integer pageSize)  throws SQLException;
    public OrderItem findByOrderId(String order_id)  throws SQLException;
}
