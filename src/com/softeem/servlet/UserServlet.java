package com.softeem.servlet;

import com.softeem.bean.User;
import com.softeem.service.UserService;
import com.softeem.service.impl.UserServiceImpl;
import com.softeem.utils.BaseServlet;
import com.softeem.utils.CookieUtils;
import com.softeem.utils.WebUtils;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

/**
 *
 */
@WebServlet(name = "UserServlet", value = "/UserServlet")
public class UserServlet extends BaseServlet {
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        WebUtils.copyParamToBean(parameterMap, user);

        UserService userService = new UserServiceImpl();
        try {
            User myuser = userService.login(user);
            if (myuser != null) {
                HttpSession session = request.getSession();
                Cookie namecookie = new Cookie("username", myuser.getUsername());
                Cookie passcookie = new Cookie("password", myuser.getPassword());
                namecookie.setMaxAge(60 * 60 * 24 * 7);
                passcookie.setMaxAge(60 * 60 * 24 * 7);
                //当前Cookie 一周内有效
                response.addCookie(namecookie);
                response.addCookie(passcookie);
                session.setAttribute("user", myuser);
                request.setAttribute("msg", "欢迎回来");
                if (request.getParameter("peiurl")!=null&&!request.getParameter("peiurl").equals("")){
                    request.getRequestDispatcher(request.getParameter("peiurl")).forward(request, response);
                }else{

                    request.getRequestDispatcher("/pages/user/success.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("msg", "账号名或登录密码error");
                request.setAttribute("username", user.getUsername());
                request.setAttribute("password", user.getPassword());
                request.getRequestDispatcher("/pages/user/login.jsp").forward(request, response);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    protected void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        // 获取Session 中的验证码
        String token = (String) session.getAttribute(KAPTCHA_SESSION_KEY);
        // 删除 Session 中的验证码
        session.removeAttribute(KAPTCHA_SESSION_KEY);


        String code = request.getParameter("code");

        System.out.println("code = " + code);

        System.out.println("token = " + token);

        Map<String, String[]> parameterMap = request.getParameterMap();

        User user = new User();

        WebUtils.copyParamToBean(parameterMap, user);

        request.setAttribute("user", user);

        UserService userService = new UserServiceImpl();

        try {
            request.setCharacterEncoding("UTF-8");

            if (token.equalsIgnoreCase(code)) {

                if (!userService.existsUsername(user.getUsername())) {

                    userService.registUser(user);

                    session.setAttribute("user", user);

                    request.setAttribute("msg", "注册成功!");

                    request.getRequestDispatcher("pages/user/success.jsp").forward(request, response);

                    session.invalidate();
                } else {
                    request.setAttribute("msg", "用户名已存在");
                    request.getRequestDispatcher("pages/user/regist.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("msg", "验证码不正确");
                request.getRequestDispatcher("pages/user/regist.jsp").forward(request, response);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    protected void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        session.invalidate();
        Cookie nameCookie = CookieUtils.findCookie("username", request.getCookies());
        Cookie passCookie = CookieUtils.findCookie("password", request.getCookies());
       if(nameCookie !=null){
           //立刻失效
           nameCookie.setMaxAge(0);
           response.addCookie(nameCookie);
       }

        if(passCookie !=null){
            passCookie.setMaxAge(0);
            response.addCookie(passCookie);
        }

        response.sendRedirect("index.jsp");
    }
}
