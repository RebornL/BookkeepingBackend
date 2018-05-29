package com.reborn.hutubill.service;

import com.reborn.hutubill.dao.UserDao;
import com.reborn.hutubill.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserService extends HttpServlet {
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserDao userDao = new UserDao();
        /** 设置允许响应头允许跨域访问，返回的数据格式为json格式，且设置数据格式为UTF-8 **/
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
    
        String uri = req.getRequestURI();
        System.out.println(uri.substring(uri.lastIndexOf("/")+1));
        //save已经被测试成，当用户已经存在进行登录的时候，会返回user的json的对应的数据格式
        if ("save".equals(uri.substring(uri.lastIndexOf("/")+1))) {
            //注册用户或者登录
            User user = new User();
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            //首先判断数据库中存在此用户
            user = userDao.getByUsername(username, password);
            if (user == null) {
                //数据库中不存在该用户，因此进行注册操作
                user.setUsername(req.getParameter("username"));
                user.setPassword(req.getParameter("password"));
                user = userDao.add(user);
            }

//            user.setId(2);
            if (user.getId() > 0) {
                //用户登录或者注册成功
                //添加Cookie
                Cookie cookie = new Cookie("uid", String.valueOf(user.getId()));
                resp.addCookie(cookie);
//                resp.sendRedirect("localhost:8081/home");
                resp.getWriter().write("["+user.toString()+"]");
            } else {
                //注册失败
                resp.getWriter().write("{\"error\":\"1\"}");
            }
        }
    }
}
