package com.reborn.hutubill.dao;

import com.mysql.cj.api.xdevapi.Result;
import com.reborn.hutubill.entity.User;
import com.reborn.hutubill.util.DBUtil;

import java.sql.*;

public class UserDao {
    
    public User add(User user) {
        String sql = "insert into user value (null, ?, ?)";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            
            ps.execute();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return user;
    }
    
    public User getByUsername(String username, String password) {
        String sql = "select * from user where username = ? and password = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql);) {
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;//返回null，表示并没有此用户
    }
}
