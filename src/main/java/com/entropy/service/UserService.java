package com.entropy.service;

import com.entropy.entity.User;

public interface UserService {
    //判断手机号是否存在
    boolean isExist(String tel);
    //注册用户
    void registerUser(User user);

}
