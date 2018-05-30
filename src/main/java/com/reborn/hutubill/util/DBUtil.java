package com.reborn.hutubill.util;

import java.sql.*;

public class DBUtil {
    
    private static final String ip = "localhost";
    private static final String port = "3306";
    private static final String database = "hutubill";
    private static final String encoding = "UTF-8";
    private static final String username = "root";
    private static final String password = "123456";//Linux上为123456，macos上为123456
    private static final String url = String.format("jdbc:mysql://%s:%s/%s?characterEncoding=%s&useSSL=false", ip, port, database, encoding);
    static {
        try {
            //加载mysql驱动
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) {
        try {
            Connection c = DBUtil.getConnection();
            Statement s = c.createStatement();
            String sql = "insert into user(username, password) values('test1', 'test12345')";
//            String sql = "select * from user;";
//            ResultSet result = s.executeQuery(sql);
            int result = s.executeUpdate(sql);
            System.out.println(result);
//            while (result.next()) {
//                System.out.println(result.getInt(1));
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
