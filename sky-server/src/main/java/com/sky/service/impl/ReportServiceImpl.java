package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public TurnoverReportVO turnoverReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String s = StringUtil.join(",", dateList);
        List<Double> turnoverList = new ArrayList<>();
        for(LocalDate date : dateList){
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Double amount = orderMapper.getAmount(5, startTime, endTime);
            amount = amount == null ? 0.0 : amount;
            turnoverList.add(amount);

            //select sum(amount) from orders where order_time > startTime and order_time < endTime and status = 5
        }
        return TurnoverReportVO.builder()
                .dateList(s)
                .turnoverList(StringUtil.join(",", turnoverList))
                .build();
    }

    @Override
    public UserReportVO userReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String s = StringUtil.join(",", dateList);
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList){
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            newUserList.add(userMapper.countNewUsers(startTime,endTime));
            totalUserList.add(userMapper.countTotalUsers(endTime));
        }
        return UserReportVO.builder()
                .dateList(s)
                .newUserList(StringUtil.join(",", newUserList))
                .totalUserList(StringUtil.join(",", totalUserList))
                .build();
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        String s = StringUtil.join(",", dateList);
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date : dateList){
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Integer orderCount = getOrderCount(startTime, endTime, null);
            Integer validOrderCount = getOrderCount(startTime, endTime, 5);
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        Integer orderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate = 0.0;
        if( orderCount != 0){
            orderCompletionRate = validOrderCount.doubleValue() / orderCount;
        }

        return OrderReportVO.builder()
                .dateList(s)
                .orderCountList(StringUtil.join(",", orderCountList))
                .validOrderCountList(StringUtil.join(",", validOrderCountList))
                .totalOrderCount(orderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        LocalDateTime startTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> mapList = orderMapper.getSalesTop10(startTime, endTime);
        List<String> collect = mapList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> collect1 = mapList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String s = StringUtil.join(",", collect);
        String s1 = StringUtil.join(",", collect1);


        return SalesTop10ReportVO.builder()
                .nameList(s)
                .numberList(s1)
                .build();
    }

    private Integer getOrderCount(LocalDateTime begin,LocalDateTime end,Integer status) {
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);
        return orderMapper.countByMap(map);
    }
}
