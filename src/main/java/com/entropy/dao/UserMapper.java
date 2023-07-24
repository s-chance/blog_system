package com.entropy.dao;

import com.entropy.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    //根据用户名查询用户
    @Select("select * from t_user where username=#{username}")
    public User findByUsername(String name);

    //添加用户
    @Insert("insert into t_user(username,password,email,created,valid) values(#{username},#{password},#{email},#{created},#{valid})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addUser(User user);

}
