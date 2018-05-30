package com.reborn.hutubill.service;

import com.reborn.hutubill.dao.RecordDao;
import com.reborn.hutubill.entity.Record;
import com.reborn.hutubill.util.DateUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordService extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        //response设置
        req.setCharacterEncoding("utf-8");
        /** 设置响应头允许跨域访问 **/
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
        resp.setContentType("text/html;charset=UTF-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        
        String url = req.getRequestURI();
        String action = url.substring(url.lastIndexOf("/")+1);
        
        RecordDao recordDao = new RecordDao();
//        System.out.println(action);
        //获取本月的消费
        if ("getConsume".equals(action)) {
            int uid = Integer.parseInt(req.getParameter("uid"));
            //根据用户的uid获取用户本月的消费
            int monthConsume = recordDao.getMonthConsume(uid);
            if (monthConsume != 0) {
                resp.getWriter().write("{\"uid\":\""+uid+"\", \"monthConsume\": " +
                        "\""+monthConsume+"\"}");
            } else {
                //当用户本月没有任何记录的时候返回error为1.
                resp.getWriter().write("{\"error\": \"1\"}");
            }
        }
        if ("getMonthRecord".equals(action)) {
//            System.out.println(req.getParameter("uid"));
            //获取一定时间内的记录
            List<Record> records = recordDao.list(DateUtil.monthBegin(),
                    DateUtil.monthEnd(), Integer.valueOf(req.getParameter("uid")));
            //遍历循环所有的记录，暂时先将其全部返回到客户端
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("[");
            
            for (Record record: records) {
//                System.out.println(record.toString());
                strBuffer.append(record.toString());
                strBuffer.append(", ");
            }
            System.out.println(strBuffer.lastIndexOf(","));
            if (strBuffer.length() == 1) {
                strBuffer.append("]");
            } else {
                strBuffer.replace(strBuffer.lastIndexOf(","), strBuffer
                        .lastIndexOf(",") + 1, "]");
            }
            //返回本月的消费的所有记录
            resp.getWriter().write(strBuffer.toString());
        }
    
//        if ("getConsume".equals(action)) {
//            //获取用户本月的总消费
//            int uid = Integer.parseInt(req.getParameter("uid"));
//            int result = recordDao.getMonthConsume(uid);
//            resp.getWriter().write("{\"uid\":\""+uid+"\", \"monthConsume\": " +
//                    "\""+result+"\"}");
//        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
//        super.doPost(req, resp);
        req.setCharacterEncoding("utf-8");

        RecordDao recordDao = new RecordDao();
        /** 设置响应头允许跨域访问 **/
        resp.setHeader("Access-Control-Allow-Origin", "*");
        /* 星号表示所有的异域请求都可以接受， */
        resp.setHeader("Access-Control-Allow-Methods", "GET,POST");
    
        String url = req.getRequestURI();
        String action = url.substring(url.lastIndexOf("/")+1);
        if("addrecord".equals(action)) {
            //新增新的条目
            Record record = new Record();
            record.setSpend(Integer.parseInt(req.getParameter("spend")));
            record.setCid(Integer.parseInt(req.getParameter("cid")));
            record.setComment(req.getParameter("comment"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            try {
                record.setDate(simpleDateFormat.parse(req.getParameter
                        ("date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
    
            record = recordDao.add(record);
            if (record.getId() > 0) {
                resp.setContentType("text/html;charset=UTF-8");
                resp.setCharacterEncoding("utf-8");
                resp.setContentType("application/json;charset=utf-8");
                //新增成功
                resp.getWriter().write(record.toString());
            }
        
        }
    
    }
}
