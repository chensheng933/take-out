package com.itheima.reggie.mapper;

import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Insert;

public interface OrdersMapper {
    @Insert("insert into orders values(#{id},#{number},#{status},#{userId},#{addressBookId},#{orderTime},#{checkoutTime},#{payMethod},#{amount},#{remark},#{phone},#{address},#{userName},#{consignee})")
    void save(Orders order);
}
