package com.sky.mapper;

import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 新增下单详细
     * @param orderDetail
     */
    void insertBatch(OrderDetail orderDetail);
}
