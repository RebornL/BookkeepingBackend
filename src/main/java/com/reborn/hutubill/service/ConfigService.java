package com.reborn.hutubill.service;

import com.reborn.hutubill.dao.ConfigDao;
import com.reborn.hutubill.entity.Config;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConfigService extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        ConfigDao configDao = new ConfigDao();
        //获取访问config的操作
        String url = req.getRequestURI();
        String action = url.substring(url.lastIndexOf("/")+1);
        //通过action操作预判浏览器端的发送请求
        if("getConfig".equals(action)) {
            //获取用户的配置（也就是预算，初始版本配置文件只存储预算，因此getConfig操作等同于getBuget操作）
            // 获取发送的参数
            //{uid: xxx, buget: xxxx}
            Config config = new Config();
            int uid = Integer.parseInt(req.getParameter("uid"));//获取用户的id编号
            config.setUid(uid);
            config = configDao.get(uid);
            //保存用户的预算
            resp.getWriter().write("["+config.toString()+"]");
        }
        
        
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        super.doPost(req, resp);
        //设置response, 手动操作返回json数据格式
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        
        ConfigDao configDao = new ConfigDao();
        //获取访问config的操作
        String url = req.getRequestURI();
        String action = url.substring(url.lastIndexOf("/")+1);
        //通过action操作预判浏览器端的发送请求
        if("save".equals(action)) {
            //若用户发送保存预算的操作
            // 获取发送的参数
            //{uid: xxx, buget: xxxx}
            Config config = new Config();
            int uid = Integer.parseInt(req.getParameter("uid"));//获取用户的id编号
            String buget = req.getParameter("buget");//获取用户设置的预算
            config.setUid(uid);
            config.setKey("buget");
            config.setValue(buget);
            //保存用户当前设置的预算
            config = configDao.add(config);
            resp.getWriter().write("["+config.toString()+"]");
        }
    }
}
