package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Api(tags = "数据统计相关接口")
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计接口")
    public Result<TurnoverReportVO> getTurnoverStatistic(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){
        TurnoverReportVO  turnoverReportVO = reportService.getTurnoverStatistic(begin, end);
        return Result.success(turnoverReportVO);
    }

    @GetMapping("/userStatistics")
    @ApiOperation("用户统计接口")
    public Result<UserReportVO> getUserStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){
        UserReportVO userReportVO = reportService.getUserStatistic(begin, end);
        return Result.success(userReportVO);
    }

    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计接口")
    public Result<OrderReportVO> getOrdersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end) {
        OrderReportVO orderReportVO = reportService.getOrdersStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    @GetMapping("/top10")
    @ApiOperation("查询销量排名top10接口")
    public Result<SalesTop10ReportVO> getTop10(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
           LocalDate begin,
           @DateTimeFormat(pattern = "yyyy-MM-dd")
           LocalDate end){
        SalesTop10ReportVO salesTop10ReportVO = reportService.getTop10(begin, end);
        return Result.success(salesTop10ReportVO);
    }
}

