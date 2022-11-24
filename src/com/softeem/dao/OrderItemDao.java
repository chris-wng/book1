package com.softeem.dao;

import com.softeem.bean.OrderItem;
import com.softeem.utils.BaseInterface;

import java.sql.SQLException;

public interface OrderItemDao extends BaseInterface<OrderItem> {
    public OrderItem findByOrderId(String order_id) throws SQLException;
}
