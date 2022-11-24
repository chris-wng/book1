package com.softeem.service.impl;

import com.softeem.bean.Book;
import com.softeem.bean.CartItem;
import com.softeem.bean.Order;
import com.softeem.bean.OrderItem;
import com.softeem.dao.BookDao;
import com.softeem.dao.OrderDao;
import com.softeem.dao.OrderItemDao;
import com.softeem.dao.impl.BookDaoImpl;
import com.softeem.dao.impl.OrderDaoImpl;
import com.softeem.dao.impl.OrderItemDaoImpl;
import com.softeem.service.Cart;
import com.softeem.service.OrderService;
import com.softeem.utils.Page;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

public class OrderServiceImpl implements OrderService {


    private OrderDao orderDao = new OrderDaoImpl();
    private OrderItemDao orderItemDao = new OrderItemDaoImpl();
    private BookDao bookDao = new BookDaoImpl();

    @Override
    public String createOrder(Cart cart, Integer userId) throws SQLException {
        // 订单号===唯一性
        String orderId = "" + System.currentTimeMillis() + userId;
        // 创建一个订单对象

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        order.setPrice(cart.getTotalPrice());
        order.setStatus(0);
        order.setUserId(userId);
        // 保存订单
        orderDao.save(order);

        Map<Integer, CartItem> items = cart.getItems();
        for (Map.Entry<Integer, CartItem> entry : items.entrySet()) {
            OrderItem orderItem = new OrderItem();
            //设置订单项的名字
            orderItem.setName(entry.getValue().getName());
            //设置订单项的数量
            orderItem.setCount(entry.getValue().getCount());
            //设置订单项的单价
            orderItem.setPrice(entry.getValue().getPrice());
            //设置订单项的总价
            orderItem.setTotalPrice(entry.getValue().getTotalPrice());
            //设置订单项的外键id订单编号
            orderItem.setOrderId(orderId);
            orderItemDao.save(orderItem);
            Book book = bookDao.findById(entry.getValue().getId());
            //设置销量
            book.setSales(book.getSales() + entry.getValue().getCount());
            //设置库存
            book.setStock(book.getStock() - entry.getValue().getCount());
            //修改book的销量与库存
            bookDao.updateById(book);
        }
        // 清空购物车
        cart.clear();

        return orderId;

    }

    @Override
    public Page<Order> page(Integer pageNo,Integer pageSize) throws SQLException {
        Page<Order> page = new Page<>();
        Integer count = orderDao.pageRecord();
        page.setPageTotalCount(count);
        page.setPageTotal((count+pageSize-1)/pageSize);
        page.setPageNo(pageNo);
        page.setItems(orderDao.page(page.getPageNo()));
        return page;
    }

    @Override
    public OrderItem findByOrderId(String order_id) throws SQLException {
        return orderItemDao.findByOrderId(order_id);
    }
}
