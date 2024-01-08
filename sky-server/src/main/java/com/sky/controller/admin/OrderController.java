package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/order")
@Api(tags = "订单管理接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result getOrderDetails(@PathVariable("id") Long id){
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> orderConditionSearch (OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索： {}", ordersPageQueryDTO);
        PageResult pageResult = orderService.pageQuery4Admin(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result countStatistics(){
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.countStatistics();
        return Result.success(orderStatisticsVO);
    }

    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirmOrder(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单: {}",ordersConfirmDTO);
        orderService.confirmOrder(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejectOrder(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单：{}，理由:{}", ordersRejectionDTO.getId(), ordersRejectionDTO.getRejectionReason() );
        orderService.rejectOrder(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("取消订单：{}",ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result deliveryOrder(@PathVariable("id") Long id){
        log.info("派送订单：{}", id);
        orderService.deliveryOrder(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }

}
