package com.reborn.hutubill.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    
    private static final String ip = "127.0.0.1";
    private static final String port = "3306";
    private static final String database = "hutubill";
    private static final String encoding = "UTF-8";
    private static final String username = "root";
    private static final String password = "123456";
    
    static {
        try {
            //加载mysql驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {
        String url = String.format
                ("jdbc:mysql://%s:%s/%s?characterEncoding=%s&useSSL=false", ip,
                        port,
                        database, encoding);
        return DriverManager.getConnection(url, username, password);
    }
}
