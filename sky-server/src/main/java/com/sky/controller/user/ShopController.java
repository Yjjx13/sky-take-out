package com.sky.controller.user;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {
    public final String KEY_SHOP_STATUS = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        log.info("查询店铺营业状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY_SHOP_STATUS);
        return Result.success(status);
    }
}
