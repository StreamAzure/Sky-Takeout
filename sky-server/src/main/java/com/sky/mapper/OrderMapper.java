package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页条件查询并按下单时间排序
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * 根据订单状态和下单时间查询订单
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 统计待接单数量
     * @return
     */
    @Select("SELECT COUNT(*) as orderCount from orders where status = 2")
    int toBeConfirmedCount();

    /**
     * 统计已接单数量
     * @return
     */
    @Select("SELECT COUNT(*) as orderCount from orders where status = 3")
    int confirmedCount();

    /**
     * 统计派送中数量
     * @return
     */
    @Select("SELECT COUNT(*) as orderCount from orders where status = 4")
    int deliveryInProgressCount();

    Integer countByMap(Map map);

    @Select("select SUM(amount) as turnover FROM orders WHERE order_time BETWEEN #{begin} AND #{end}")
    Double sumByMap(Map map);
}
