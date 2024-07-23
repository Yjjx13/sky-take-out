package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Slf4j
public class ShopController {
    public final String KEY_SHOP_STATUS = "SHOP_STATUS";
    @Autowired
    private RedisTemplate redisTemplate;
    @PutMapping("/{status}")
    public Result status(@PathVariable Integer status) {
        log.info("修改店铺营业状态:{}", status);
        redisTemplate.opsForValue().set(KEY_SHOP_STATUS,status);
        return Result.success();
    }
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        log.info("查询店铺营业状态");
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY_SHOP_STATUS);
        log.info("店铺营业状态为:{}", status);
        return Result.success(status);
    }
}
