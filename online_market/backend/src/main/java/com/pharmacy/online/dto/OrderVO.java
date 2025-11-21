package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单视图对象
 */
@Data
public class OrderVO {
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 订单类型：NORMAL-普通，PRESCRIPTION-处方
     */
    private String orderType;
    
    /**
     * 订单状态
     */
    private String status;
    
    /**
     * 订单状态描述
     */
    private String statusDesc;
    
    /**
     * 应付金额
     */
    private BigDecimal payableAmount;
    
    /**
     * 商品总数量
     */
    private Integer totalQuantity;
    
    /**
     * 商品主图（第一个商品）
     */
    private String mainImage;
    
    /**
     * 商品列表
     */
    private List<OrderItemVO> items;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 支付超时时间
     */
    private LocalDateTime paymentTimeout;
}

