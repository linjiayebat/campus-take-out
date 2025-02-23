package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        for (LocalDate date = begin; !date.equals(end); date = date.plusDays(1)) {
            dateList.add(date);
        }

        String dateListString = StringUtils.join(dateList, ",");

        // select sum(amount) from orders where order_time > ? and order_time < ?
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Double turnover = orderMapper.turnoverStatistics(beginTime, endTime);
            if (turnover == null) turnover = 0.0;

            turnoverList.add(turnover);
        }
        String turnoverListString = StringUtils.join(turnoverList, ",");

        return new TurnoverReportVO(dateListString, turnoverListString);
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        for (LocalDate date = begin; !date.equals(end); date = date.plusDays(1)) {
            dateList.add(date);
        }

        String dateListString = StringUtils.join(dateList, ",");


        List<Long> newUserList = new ArrayList<>();
        List<Long> totalUserList = new ArrayList<>();

        // select sum(id) from user
        for (LocalDate date = begin; !date.equals(end); date = date.plusDays(1)) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            newUserList.add(userMapper.selectNewUser(beginTime, endTime));
            totalUserList.add(userMapper.selectSumUser(endTime));
        }

        String newUserListString = StringUtils.join(newUserList, ",");
        String totalUserListString = StringUtils.join(totalUserList, ",");

        return new UserReportVO(dateListString, newUserListString, totalUserListString);
    }

    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        for (LocalDate date = begin; !date.equals(end); date = date.plusDays(1)) {
            dateList.add(date);
        }

        String dateListString = StringUtils.join(dateList, ",");

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate date = begin; !date.equals(end); date = date.plusDays(1)) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            orderCountList.add(orderMapper.selectOrderNumberWithStatus(beginTime, endTime, null));
            validOrderCountList.add(orderMapper.selectOrderNumberWithStatus(beginTime, endTime, 5));
        }

        String orderCountListString = StringUtils.join(orderCountList, ",");
        String validOrderCountListString = StringUtils.join(validOrderCountList, ",");

        LocalDateTime beginDay = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endDay = LocalDateTime.of(end, LocalTime.MIN);
        Integer totalOrderCount = orderMapper.selectOrderNumberWithStatus(beginDay, endDay, null);
        Integer validOrderCount = orderMapper.selectOrderNumberWithStatus(beginDay, endDay, 5);
        Double orderCompletionRate = validOrderCount * 1.0 / totalOrderCount;

        if (validOrderCount.equals(0)) orderCompletionRate = 0.0;

        return new OrderReportVO(dateListString, orderCountListString, validOrderCountListString, totalOrderCount, validOrderCount, orderCompletionRate);
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {



        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> goodsSalesDTOList = orderDetailMapper.top10(beginTime, endTime);
        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        for (GoodsSalesDTO goodsSalesDTO : goodsSalesDTOList) {
            nameList.add(goodsSalesDTO.getName());
            numberList.add(goodsSalesDTO.getNumber());
        }

        String nameListString = StringUtils.join(nameList, ",");
        String numberListString = StringUtils.join(numberList, ",");

        return new SalesTop10ReportVO(nameListString, numberListString);
    }
}
