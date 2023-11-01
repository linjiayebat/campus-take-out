package com.sky.controller.user;

import com.sky.result.PageResult;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

// 两个controller名字一样的话，就自己命名Bean对象的名称，防止名字冲突
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {

    public static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/status")
    public Result<Integer> getShopStatus() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Integer shop_status = (Integer) valueOperations.get(KEY);
        return Result.success(shop_status);
    }



}
