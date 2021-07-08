package com.example.security.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.security.entry.Users;
import com.example.security.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UsersMapper usersMapper;

    public Users userLogin(String mobileNo, String password) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",mobileNo).eq("password",password);
        return usersMapper.selectOne(queryWrapper);
    }
}
