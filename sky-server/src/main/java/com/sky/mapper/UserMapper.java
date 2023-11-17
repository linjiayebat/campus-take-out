package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
