package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@EnableScheduling
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    @Scheduled(cron = "1/5 * * * * ?" )
    public void processTimeoutOrders() {
        log.info("处理超时订单");
        List<Orders> byStatusAndOrderTime = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        if (byStatusAndOrderTime != null && byStatusAndOrderTime.size() > 0) {
            for (Orders order : byStatusAndOrderTime) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，已取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }
//    @Scheduled(cron = "0 0 1 * * ?" )
    @Scheduled(cron = "0/5 * * * * ?" )
    public void processDeliveryOrders(){
        log.info("处理派送订单:{}",LocalDateTime.now());
        List<Orders> byStatusAndOrderTime = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
        if (byStatusAndOrderTime != null && byStatusAndOrderTime.size() > 0) {
            for (Orders order : byStatusAndOrderTime) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
