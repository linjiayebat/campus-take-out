package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.vo.UserLoginVO;
import org.apache.ibatis.annotations.Insert;

public interface UserService {

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */

    User login(UserLoginDTO userLoginDTO);
}
