package com.entropy.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserAuthorityMapper {
    //给用户添加权限
    @Insert("insert into t_user_authority(user_id,authority_id) values (#{uid},#{authorityId})")
    public void addAuthority(@Param("uid") Integer uid, @Param("authorityId") Integer authorityId);
}
