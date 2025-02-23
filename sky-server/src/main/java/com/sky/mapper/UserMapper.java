package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据openid查询user
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User selectUserByOpenId(String openid);

    /**
     * 新增user
     * @param user
     */
    void addUser(User user);

    /**
     * 按userId查询
     * @param userId
     * @return
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);


    /**
     * 查询新增用户数量
     * @param begin
     * @param endTime
     */
    @Select("select COUNT(id) from user where create_time >= #{begin} and create_time < #{endTime}")
    Long selectNewUser(LocalDateTime begin, LocalDateTime endTime);


    /**
     * 查询用户总量
     * @param endTime
     * @return
     */
    @Select("select COUNT(id) from user where create_time < #{endTime}")
    Long selectSumUser(LocalDateTime endTime);

    /**
     * 根据动态条件统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
