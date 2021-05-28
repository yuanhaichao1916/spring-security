package com.example.security.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "users")
public class Users{
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;

    public Users(Integer id, String userName, String password) {
        this.id = id;
        this.username = userName;
        this.password = password;
    }

    public Users() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
