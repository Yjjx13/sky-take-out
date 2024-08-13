package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping(value = "/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,

            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end ) {
        TurnoverReportVO turnoverReportVO = reportService.turnoverReport(begin, end);
        log.info("营业额统计：{}", turnoverReportVO);

        return Result.success(turnoverReportVO);
    }
    @GetMapping(value = "/userStatistics")
    public Result<UserReportVO> userReport(
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end ) {
        UserReportVO userReportVO = reportService.userReport(begin, end);
        log.info("用户统计：{},{}",begin,end);
        return Result.success(userReportVO);
    }
    @GetMapping(value = "/ordersStatistics")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end ) {
        OrderReportVO orderReportVO = reportService.ordersStatistics(begin, end);
        log.info("订单统计：{},{}",begin,end);
        return Result.success(orderReportVO);
    }
    @GetMapping(value = "/top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end ) {
        SalesTop10ReportVO SalesTop10ReportVO = reportService.top10(begin, end);
        log.info("top10：{}",SalesTop10ReportVO);
        return Result.success(SalesTop10ReportVO);
    }
    /**
     * 导出运营数据报表
     * @param response
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse response){
        reportService.exportBusinessData(response);
    }
}
