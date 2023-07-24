package com.entropy.service.impl;

import com.entropy.dao.UserAuthorityMapper;
import com.entropy.dao.UserMapper;
import com.entropy.entity.User;
import com.entropy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthorityMapper userAuthorityMapper;
    //判断手机号是否存在
    @Override
    public boolean isExist(String tel) {
        User user = userMapper.findByUsername(tel);
        if (user != null) {
            return true; //用户已存在
        }
        return false;
    }

    //注册用户
    @Override
    @Transactional
    public void registerUser(User user) {
        //user对象数据初始化  用户信息完善
        user.setCreated(new Date());
        //密码加密
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode(user.getPassword());
        user.setPassword(password);
        //添加用户
        userMapper.addUser(user);
        //给用户添加权限
        Integer uid = user.getId();
        //普通用户
        Integer authorityId = 2;
        userAuthorityMapper.addAuthority(uid, authorityId);
    }
}
