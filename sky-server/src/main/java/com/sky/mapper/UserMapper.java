package com.sky.mapper;


import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

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

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /**
     * 查询时间区间内用户总数
     * @param beginTime
     * @return
     */
    @Select("select count(id) from user where create_time < #{beginTime}")
    Integer countByLimitTime(LocalDateTime beginTime);

    /**
     * 查询时间区间内新增用户数
     * @param beginTime
     * @param endTime
     * @return
     */
    Integer countByTime(LocalDateTime beginTime, LocalDateTime endTime);
}
