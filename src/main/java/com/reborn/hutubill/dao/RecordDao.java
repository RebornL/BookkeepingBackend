package com.reborn.hutubill.dao;


import com.reborn.hutubill.entity.Category;
import com.reborn.hutubill.entity.Record;
import com.reborn.hutubill.util.DBUtil;
import com.reborn.hutubill.util.DateUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordDao {
    
    public int getTotal() {
        int total = 0;
        try (Connection c = DBUtil.getConnection(); Statement s = c
                .createStatement();) {
            String sql = "select count(*) from record";
            ResultSet rs = s.executeQuery(sql);
            while (rs.next()) {
                //JDBC读取SQL语句返回结果
                total = rs.getInt(1);
            }
            
            System.out.println("total of record: " + total);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return total;
    }
    
    public Record add(Record record) {
        String sql = "insert into record(spend, cid, comment, date, uid) values(?, ?, ?, ?, ?)";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setInt(1, record.getSpend());
            ps.setInt(2, record.getCid());
            ps.setString(3, record.getComment());
            ps.setDate(4, DateUtil.util2Sql(record.getDate()));
            ps.setInt(5, record.getUid());
            ps.execute();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                record.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return record;
    }
    
    public void update(Record record) {
        
        String sql = "update record set spend = ?, cid = ?, comment = " +
                "?, date = ? where id = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)) {
            ps.setInt(1, record.getSpend());
            ps.setInt(2, record.getCid());
            ps.setString(3, record.getComment());
            ps.setDate(4, DateUtil.util2Sql(new Date()));
            ps.setInt(5, record.getId());
            
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void delete(int id) {
        try (Connection c = DBUtil.getConnection(); Statement s = c
                .createStatement();) {
            String sql = "delete from record where id = " + id;
            s.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Record get(int id, int uid) {
        Record record = null;
        
        try(Connection c = DBUtil.getConnection(); Statement s = c
                .createStatement();) {
            
            String sql = "select * from record where id = "+id+" and uid = "+
                    uid;
            
            ResultSet rs = s.executeQuery(sql);
            if (rs.next()) {
                record.setSpend(rs.getInt("spend"));
                record.setCid(rs.getInt("cid"));
                record.setComment(rs.getString("comment"));
                record.setDate(rs.getDate("date"));
                record.setId(id);
                record.setUid(rs.getInt("uid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return record;
    }
    
    public List<Record> list() {
        return list(0, Short.MAX_VALUE);//查询所有
    }
    //分页查询
    public List<Record> list(int start, int count) {
        List<Record> records = new ArrayList<>();
        
        String sql = "select * from record order by id desc limit ?, ?";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)){
            ps.setInt(1, start);
            ps.setInt(2, count);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt(1));
                record.setDate(rs.getDate("date"));
                record.setComment(rs.getString("comment"));
                record.setCid(rs.getInt("cid"));
                record.setSpend(rs.getInt("spend"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    //根据日期查询当天的记录
    public List<Record> list(Date date) {
        List<Record> records = new ArrayList<>();
        
        String sql = "select * from record where date = ?";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)){
            ps.setDate(1, DateUtil.util2Sql(date));
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt(1));
                record.setDate(rs.getDate("date"));
                record.setComment(rs.getString("comment"));
                record.setCid(rs.getInt("cid"));
                record.setSpend(rs.getInt("spend"));
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    //获取一定时间内的记录
    public List<Record> list(Date start, Date end, int uid) {
        List<Record> records = new ArrayList<>();
        //获取本月的消费记录，还得加上uid的限制，明确为不同的人
        String sql = "select * from record where date >= ? and date <= ? and " +
                "uid = ? order by date";
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql)){
            ps.setDate(1, DateUtil.util2Sql(start));
            ps.setDate(2, DateUtil.util2Sql(end));
            ps.setInt(3, uid);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Record record = new Record();
                record.setId(rs.getInt(1));
                record.setDate(rs.getDate("date"));
                record.setComment(rs.getString("comment"));
                record.setCid(rs.getInt("cid"));
                record.setSpend(rs.getInt("spend"));
                record.setUid(uid);
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return records;
    }
    
    //这个方法用来计算截止至本月某日的消费金额
    public static int getMonthConsume(int uid) {
        String sql = "select uid, sum(spend) as consume from record " +
                "where " +
                "uid = " +
                "? and" +
                " date > ? and date < ?";
        Date monthBegin = DateUtil.monthBegin();
        Date today = DateUtil.today();
        
        int result = 0;
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql);) {
            
            ps.setInt(1, uid);
            ps.setDate(2, DateUtil.util2Sql(monthBegin));
            ps.setDate(3, DateUtil.util2Sql(today));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("consume");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public static int getTodayConsume(int uid) {
        String sql = "select uid, sum(spend) as consume from record " +
                "where " +
                "uid = " +
                "? and" +
                " date = ?";
        Date today = DateUtil.today();
        
        int result = 0;
        
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c
                .prepareStatement(sql);) {
            
            ps.setInt(1, uid);
            ps.setDate(2, DateUtil.util2Sql(today));
            
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("consume");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}