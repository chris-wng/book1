package com.softeem.servlet;

import com.softeem.bean.Book;
import com.softeem.bean.Order;
import com.softeem.bean.OrderItem;
import com.softeem.bean.User;
import com.softeem.service.Cart;
import com.softeem.service.OrderService;
import com.softeem.service.impl.OrderServiceImpl;
import com.softeem.utils.BaseServlet;
import com.softeem.utils.Page;
import com.softeem.utils.WebUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "OrderServlet", value = "/OrderServlet")
public class OrderServlet extends BaseServlet {
    protected void searchOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("orderid");
        String pageNo = request.getParameter("pageNo");
        OrderServiceImpl orderService = new OrderServiceImpl();
        try {
            OrderItem byOrderId = orderService.findByOrderId(id);
            request.setAttribute("byOrderId", byOrderId);
            request.setAttribute("pageNo",pageNo);
            request.getRequestDispatcher("/pages/order/item.jsp").forward(request, response);


        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    protected void listOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderServiceImpl orderService = new OrderServiceImpl();
        int pageNo = WebUtils.parseInt(request.getParameter("pageNo"), 1);
        int pageSize = WebUtils.parseInt(request.getParameter("pageSize"), 4);
        try {

            Page<Order> page = orderService.page(pageNo, pageSize);

            page.setUrl("OrderServlet?action=listOrder&");
            request.setAttribute("page",page);
            request.getRequestDispatcher("/pages/order/order.jsp").forward(request,response);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

        protected void createOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        User user =  (User)session.getAttribute("user");


        if(user == null){
            request.setAttribute("msg", "登录超时请重新登陆");
            request.getRequestDispatcher("/pages/user/login.jsp").forward(request,response);

        }

        OrderServiceImpl orderService = new OrderServiceImpl();
        try {
            String orderId = orderService.createOrder(cart, user.getId());

           session.setAttribute("orderId",orderId);
            response.sendRedirect(request.getContextPath()+"/pages/cart/checkout.jsp");



        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }


}
