package com.reborn.hutubill.entity;

import java.util.Date;

public class Record {
    
    private int id;
    private int spend;
    private int cid;
    private String comment;
    private Date date;
    private int uid;
    
    public int getUid() {
        return uid;
    }
    
    public void setUid(int uid) {
        this.uid = uid;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getSpend() {
        return spend;
    }
    
    public void setSpend(int spend) {
        this.spend = spend;
    }
    
    public int getCid() {
        return cid;
    }
    
    public void setCid(int cid) {
        this.cid = cid;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    @Override
    public String toString() {
        return "{" +
                "\"id\":\"" + id +
                "\", \"spend\":\"" + spend +
                "\", \"cid\":\"" + cid +
                "\", \"comment\":\"" + comment + '\"' +
                ", \"date\":\"" + date +
                "\", \"uid\":\"" + uid +
                "\"}";
    }
}
