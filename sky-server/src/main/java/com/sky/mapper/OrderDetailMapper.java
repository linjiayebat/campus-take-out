package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 新增下单详细
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 查询订单详情根据订单id
     * @param id
     * @return
     */
    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);

    /**
     * 查询销量排名top10
     * @param beginTime
     * @param endTime
     * @return
     */
    @Select("SELECT od.name, SUM(od.number) number FROM order_detail od JOIN orders o ON od.order_id = o.id WHERE o.order_time > #{beginTime} and o.order_time < #{endTime} and o.status = 5 GROUP BY od.name ORDER BY SUM(od.number) DESC LIMIT 10")
    List<GoodsSalesDTO> top10(LocalDateTime beginTime, LocalDateTime endTime);
}
