package com.itheima.reggie.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import com.itheima.reggie.entity.*;
import com.itheima.reggie.mapper.*;
import com.itheima.reggie.service.OrdersService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    AddressBookMapper addressBookMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    ShoppingCartMapper shoppingCartMapper;

    @Autowired
    OrdersMapper ordersMapper;

    @Autowired
    OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(Orders order) {
        //1.准备初始数据
        //1.0获取用户id
        Long userId = order.getUserId();

        //1.1使用雪花算法生成订单id
        Long orderId = IdUtil.getSnowflake().nextId();

        //1.2设置totalAmount(总金额)的初始化值
        BigDecimal totalAmount = new BigDecimal("0");

        //1.3根据addredd_book_id获取地址薄信息
        AddressBook addressBook = addressBookMapper.getById(order.getAddressBookId());

        //1.4根据用户id查询用户信息
        User user = userMapper.findById(userId);

        //1.5根据用户id查询购物项列表
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.findAllByUserId(userId);

        //2.封装orderDetail,批量保存orderDetail
        //2.0声明集合用来封装订单详情(订单项)
        List<OrderDetail> orderDetails = new ArrayList<>();
        //2.1遍历购物项列表,
        if (CollUtil.isNotEmpty(shoppingCartList)) {

            for (ShoppingCart sc : shoppingCartList) {
                //2.2将每个购物项封装orderDetail,别忘记设置订单id
                OrderDetail od = new OrderDetail();
                BeanUtils.copyProperties(sc,od);
                od.setOrderId(orderId);

                //2.3将每个购物项的小计进行累加,
                totalAmount=totalAmount.add(od.getAmount().multiply(new BigDecimal(od.getNumber())));

                //2.4将每个orderDetail存入集合中
                orderDetails.add(od);
            }
            //2.5往order_detail表中新增多条记录
            orderDetailMapper.batchSave(orderDetails);
        }

        //3.封装order,保存order
        //3.1封装order对象
        order.setId(orderId);
        order.setNumber(String.valueOf(orderId));
        order.setStatus(1);

        //俩时间
        order.setOrderTime(LocalDateTime.now());
        order.setCheckoutTime(order.getOrderTime());

        //总金额
        order.setAmount(totalAmount);

        //设置收货人信息
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());

        //设置订单所属人姓名
        order.setUserName(user.getName());

        //3.2往orders表中新增一条记录
        ordersMapper.save(order);

        //4.清空购物车
        shoppingCartMapper.deleteByUserId(userId);
    }
}
