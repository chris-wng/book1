package com.softeem.servlet;

import com.softeem.bean.Book;
import com.softeem.bean.CartItem;
import com.softeem.service.BookService;
import com.softeem.service.Cart;
import com.softeem.service.impl.BookServiceImpl;
import com.softeem.utils.BaseServlet;
import com.softeem.utils.WebUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CartServlet", value = "/CartServlet")
public class CartServlet extends BaseServlet {
    /**
     * 加入购物车
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void addItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookService bookService = new BookServiceImpl();
        HttpSession session = req.getSession();
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        try {
            Book book = bookService.queryBookById(id);
            CartItem cartItem = new CartItem(book.getId(), book.getName(), 1, book.getPrice(), book.getPrice());

            //session作用会话域中取值cart 如果为null 则表示购物车中无商品信息
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart == null) {
                cart = new Cart();
                session.setAttribute("cart", cart);
            }
            //添加商品项
            cart.addItem(cartItem);

            System.out.println(cart);
            System.out.println("请求头Referer的值" + req.getHeader("Referer"));

            req.getSession().setAttribute("lastName", cartItem.getName());
            // 重定向回原来商品所在的地址页面
            resp.sendRedirect(req.getHeader("Referer"));


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * 删除商品项
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void deleteItem(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 获取商品编号
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        // 获取购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");

        if (cart != null) {
            // 删除 了购物车商品项
            cart.deleteItem(id);
            // 重定向回原来购物车展示页面
            resp.sendRedirect(req.getHeader("Referer"));
        }

    }


    /**
     * 清空购物车
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void clear(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1 获取购物车对象
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart != null) {
            // 清空购物车
            cart.clear();
            // 重定向回原来购物车展示页面
            resp.sendRedirect(req.getHeader("Referer"));
        }
    }

    protected void updateCount(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = WebUtils.parseInt(req.getParameter("id"), 0);
        int count=WebUtils.parseInt(req.getParameter("count"), 0);
        HttpSession session = req.getSession();
        Cart cart = (Cart) req.getSession().getAttribute("cart");
        if (cart != null) {
            cart.updateCount(id,count);
        }
        resp.sendRedirect(req.getHeader("Referer"));
    }


}
