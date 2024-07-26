package com.sky.mapper;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);
    @Select("select * from orders where number = #{outTradeNo}")
    Orders getByNumber(String outTradeNo);
    void update(Orders orders);
    @Update( "update orders set status = #{orderStatus}, pay_status = #{orderPaidStatus},checkout_time = #{checkOutTime} where id = #{orderId}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime checkOutTime, Long orderId);
}
