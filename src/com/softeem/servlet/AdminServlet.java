package com.softeem.servlet;

import com.softeem.bean.Admin;
import com.softeem.bean.User;
import com.softeem.service.AdminService;
import com.softeem.service.UserService;
import com.softeem.service.impl.AdminServiceImpl;
import com.softeem.service.impl.UserServiceImpl;
import com.softeem.utils.BaseServlet;
import com.softeem.utils.WebUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@WebServlet(name = "AdminServlet", value = "/AdminServlet")
public class AdminServlet extends BaseServlet {
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Admin admin = new Admin();
        WebUtils.copyParamToBean(parameterMap, admin);

        AdminService adminService = new AdminServiceImpl();
        try {
            Admin myadmin = adminService.login(admin);
            if (myadmin != null) {
                HttpSession session = request.getSession();
                Cookie namecookie = new Cookie("username", myadmin.getUsername());
                Cookie passcookie = new Cookie("password", myadmin.getPassword());
                namecookie.setMaxAge(60 * 60 * 24 * 7);
                passcookie.setMaxAge(60 * 60 * 24 * 7);
                //当前Cookie 一周内有效
                response.addCookie(namecookie);
                response.addCookie(passcookie);
                session.setAttribute("user", myadmin);
                request.setAttribute("msg", "欢迎回来");
                if (request.getParameter("peiurl")!=null&&!request.getParameter("peiurl").equals("")){
                    request.getRequestDispatcher(request.getParameter("peiurl")).forward(request, response);
                }else{

                    request.getRequestDispatcher("/pages/manager/manager.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("msg", "账号名或登录密码error");
                request.setAttribute("username", admin.getUsername());
                request.setAttribute("password", admin.getPassword());
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

}
