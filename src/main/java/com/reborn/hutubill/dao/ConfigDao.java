package com.reborn.hutubill.dao;

import com.reborn.hutubill.entity.Config;
import com.reborn.hutubill.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConfigDao {
    
//    public int getTotal() {
//        int total = 0;
//        try (Connection c = DBUtil.getConnection(); Statement s = c
//                .createStatement();) {
//            String sql = "select count(*) from config";
//            ResultSet rs = s.executeQuery(sql);
//            while (rs.next()) {
//                //JDBC读取SQL语句返回结果
//                total = rs.getInt(1);
//            }
//
//            System.out.println("total of config: " + total);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return total;
//    }
    
    public Config add(Config config) {
        String sql = "insert into config values(null, ?, ?, ?)";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, config.getKey());
            ps.setString(2, config.getValue());
            ps.setInt(3, config.getUid());
            ps.execute();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                config.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return config;
    }
    
    public int update(Config config) {
        
        String sql = "update config set key_ = ?, value = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)) {
            ps.setString(1, config.getKey());
            ps.setString(2, config.getValue());
            ps.setInt(3, config.getId());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c
                .createStatement();) {
            String sql = "delete from config where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Config get(int uid) {
        Config config = new Config();
        
        try(Connection c = DBUtil.getConnection(); Statement s = c
                .createStatement();) {
        
            String sql = "select * from config where uid = "+uid;
            
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                config.setKey(rs.getString("key_"));
                config.setValue(rs.getString("value"));
                config.setId(rs.getInt(1));
                config.setUid(uid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return config;
    }

    public Config getByKey(int uid, String key) {
        Config config = new Config();

        String sql = "select * from config where uid = ? and key_ = ?";

        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql);) {

            ps.setInt(1, uid);
            ps.setString(2, key);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                config.setKey(rs.getString("key_"));
                config.setValue(rs.getString("value"));
                config.setId(rs.getInt(1));
                config.setUid(uid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return config;
    }
    
    public List<Config> list() {
        return list(0, Short.MAX_VALUE);//查询所有
    }
    //分页查询
    public List<Config> list(int start, int count) {
        List<Config> configs = new ArrayList<>();
        
        String sql = "select * from config order by id desc limit ?, ?";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)){
            ps.setInt(1, start);
            ps.setInt(2, count);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Config config = new Config();
                config.setId(rs.getInt(1));
                config.setKey(rs.getString("key_"));
                config.setValue(rs.getString("value"));
                configs.add(config);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return configs;
    }
    
    //获取备份设置
    public Config getByKey(String key) {
        Config config = null;
        String sql = "select * from config where key_ = ?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql);) {
            ps.setString(1, key);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                config.setId(rs.getInt(1));
                config.setKey(rs.getString("key_"));
                config.setValue(rs.getString("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return config;
    }
}
