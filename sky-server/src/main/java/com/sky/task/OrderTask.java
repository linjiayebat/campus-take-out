package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理支付超时订单
     */
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一遍
    public void processTimeOut() {
        log.info("处理支付超时订单：{}", LocalDateTime.now());
        // select * from orders where status = ? and order_time < localdatetime - 15
        LocalDateTime now = LocalDateTime.now().plusMinutes(-15);

        List<Orders> timeOutOrder = orderMapper.getTimeOutOrder(Orders.PENDING_PAYMENT, now);
        if (timeOutOrder != null && timeOutOrder.size() > 0) {
            for (Orders order : timeOutOrder) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("支付超时，订单取消");
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理一直在派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨一点处理任务
    public void processDeliveryOrder() {
        log.info("处理一直处理派送中订单：{}", LocalDateTime.now());
        // select * from orders where status = ? and order_time < localdatetime - 1h
        LocalDateTime now = LocalDateTime.now().plusHours(-1);

        List<Orders> timeOutOrder = orderMapper.getTimeOutOrder(Orders.DELIVERY_IN_PROGRESS, now);
        if (timeOutOrder != null && timeOutOrder.size() > 0) {
            for (Orders order : timeOutOrder) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
