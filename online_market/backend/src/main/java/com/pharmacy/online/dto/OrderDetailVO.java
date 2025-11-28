package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情视图对象
 */
@Data
public class OrderDetailVO {
    
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
     * 商品列表
     */
    private List<OrderItemVO> items;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人手机（脱敏）
     */
    private String receiverPhone;
    
    /**
     * 收货地址
     */
    private String receiverAddress;
    
    /**
     * 配送方式
     */
    private String deliveryMethod;
    
    /**
     * 配送方式描述
     */
    private String deliveryMethodDesc;
    
    /**
     * 配送时段
     */
    private String deliveryTimeSlot;
    
    /**
     * 商品总价
     */
    private BigDecimal totalAmount;
    
    /**
     * 运费
     */
    private BigDecimal freight;
    
    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;
    
    /**
     * 应付金额
     */
    private BigDecimal payableAmount;
    
    /**
     * 实付金额
     */
    private BigDecimal actualAmount;
    
    /**
     * 支付方式
     */
    private String paymentMethod;
    
    /**
     * 支付流水号
     */
    private String paymentNo;
    
    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;
    
    /**
     * 处方ID
     */
    private String prescriptionId;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 取消原因
     */
    private String cancelReason;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 支付超时时间
     */
    private LocalDateTime paymentTimeout;
    
    /**
     * 确认收货时间
     */
    private LocalDateTime confirmTime;
    
    /**
     * 完成时间
     */
    private LocalDateTime completeTime;
}

