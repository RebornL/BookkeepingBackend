package com.reborn.hutubill.dao;

import com.reborn.hutubill.entity.Category;
import com.reborn.hutubill.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c
                .createStatement();) {
            String sql = "select count(*) from category";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                //JDBC读取SQL语句返回结果
                total = rs.getInt(1);
            }
    
            System.out.println("total of category: " + total);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return total;
    }
    
    //新增新的条目
    public Category add(Category category) {
        String sql = "insert into category(id, name, uid) values(null, ?, ?)";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getUid());
            ps.execute();

            ResultSet rs = ps.getGeneratedKeys();
//            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                category.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
    
    public int update(Category category) {
        
        String sql = "update category set name = ? where id = ?";
        int result = 0;
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)) {
            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());
            
            result = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int delete(int id, int uid) {
        String sql = "delete from category where id = ? and uid = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql);) {
            ps.setInt(1, id);
            ps.setInt(2, uid);
            return ps.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public Category get(int id) {
        Category category = new Category();
        String sql = "select * from category where id = ?";
        try(Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql);) {
        
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                category.setName(rs.getString("name"));
                category.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return category;
    }
    
    public List<Category> list(int uid) {
        return list(0, Short.MAX_VALUE, uid);//查询所有
    }
    //分页查询
    public List<Category> list(int start, int count, int uid) {
        List<Category> categories = new ArrayList<>();
        
        String sql = "select * from category where uid = ? order by id desc " +
                "limit ?, ?";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)){
            ps.setInt(1, uid);
            ps.setInt(2, start);
            ps.setInt(3, count);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt(1));
                category.setName(rs.getString("name"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return categories;
    }
    
}
