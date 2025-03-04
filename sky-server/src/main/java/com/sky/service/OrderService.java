package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    PageResult pageQuery4User(int page, int pageSize, Integer status);

    OrderVO details(Long id);

    void cancel(Long id) throws Exception;

    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    void repetition(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void complete(Long id);

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    void delivery(Long id);

    void reminder(Long id);
}
