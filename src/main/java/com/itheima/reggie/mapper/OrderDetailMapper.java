package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.OrderDetail;

import java.util.List;

public interface OrderDetailMapper {
    void batchSave(List<OrderDetail> orderDetails);
}
