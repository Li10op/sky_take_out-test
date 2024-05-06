package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {


    /**
     * 根据用户openid查询信息
     * @param id
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String id);

    /**
     * 添加新用户信息
     * @param user
     */

    void insert(User user);
}
