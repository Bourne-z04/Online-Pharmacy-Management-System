package com.pharmacy.online.service;

import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.dto.*;

/**
 * 订单服务接口
 */
public interface OrderService {
    
    /**
     * 创建订单
     */
    OrderCreateResultVO createOrder(String userId, OrderCreateRequest request);
    
    /**
     * 获取订单列表
     */
    PageResult<OrderVO> getOrderList(String userId, String status, Integer page, Integer size);
    
    /**
     * 获取订单详情
     */
    OrderDetailVO getOrderDetail(String userId, String orderNo);
    
    /**
     * 取消订单
     */
    void cancelOrder(String userId, String orderNo, String reason);
    
    /**
     * 确认收货
     */
    void confirmOrder(String userId, String orderNo);
    
    /**
     * 创建支付
     */
    PaymentVO createPayment(String userId, String orderNo);
    
    /**
     * 获取支付状态
     */
    String getPaymentStatus(String userId, String orderNo);
}

