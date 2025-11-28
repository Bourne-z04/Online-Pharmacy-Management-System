package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 创建订单结果VO
 */
@Data
public class OrderCreateResultVO {
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 应付金额
     */
    private BigDecimal payableAmount;
    
    /**
     * 支付超时时间（时间戳，毫秒）
     */
    private Long paymentTimeout;
}

