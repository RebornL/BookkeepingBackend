package com.reborn.hutubill.service;

import com.reborn.hutubill.dao.CategoryDao;
import com.reborn.hutubill.entity.Category;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryService extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
//        System.out.println(req.getRequestURI());
//        resp.getWriter().write("Category");
        CategoryDao categoryDao = new CategoryDao();
        /** 设置响应头允许跨域访问 **/
        resp.setHeader("Access-Control-Allow-Origin", "*");
        /* 星号表示所有的异域请求都可以接受， */
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
    
    
        String url = req.getRequestURI();
        String action = url.substring(url.lastIndexOf("/")+1);

        if ("getallcategory".equals(action)) {
            //获取所有用户的条目
            List<Category> categories = categoryDao.list(Integer.parseInt(req
                    .getParameter("uid")));
            if (categories.isEmpty()) {
                resp.getWriter().write("[]");
            } else {
    
                StringBuilder strBuilder = new StringBuilder();
                strBuilder.append("[");
                for (Category category : categories) {
                    strBuilder.append(category.toString());
                    strBuilder.append(", ");
                }
                strBuilder.replace(strBuilder.lastIndexOf(","), strBuilder
                        .lastIndexOf(",") + 1, "]");
    
    
                resp.getWriter().write(strBuilder.toString());
            }
        }
    
        if ("deletecategory".equals(action)) {
            int result = categoryDao.delete(Integer.parseInt(req.getParameter
                            ("id")), Integer.parseInt(req.getParameter("uid")));
            resp.getWriter().write("[{\"result\":\""+result+"\"}]");
        }
        
        if ("getConsume".equals(action)) {
            //获取用户本月的消费
            int uid = Integer.parseInt(req.getParameter("uid"));
            
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        super.doPost(req, resp);
        CategoryDao categoryDao = new CategoryDao();
        /** 设置响应头允许跨域访问 **/
        resp.setHeader("Access-Control-Allow-Origin", "*");
        /* 星号表示所有的异域请求都可以接受， */
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
        
        String url = req.getRequestURI();
        String action = url.substring(url.lastIndexOf("/")+1);
        if("addcategory".equals(action)) {
            //新增新的条目
            Category category = new Category();
            category.setName(req.getParameter("name"));
            category.setUid(Integer.parseInt(req.getParameter("uid")));
            
            category = categoryDao.add(category);
            if (category.getId() > 0) {
                resp.setContentType("text/html;charset=UTF-8");
                resp.setCharacterEncoding("utf-8");
                resp.setContentType("application/json;charset=utf-8");
                //新增成功
                resp.getWriter().write("["+category.toString()+"]");
            }
            
        }
        
        
    }
}
