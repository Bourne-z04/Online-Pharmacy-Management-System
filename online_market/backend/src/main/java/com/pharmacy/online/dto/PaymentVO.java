package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 支付信息视图对象
 */
@Data
public class PaymentVO {
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 支付金额
     */
    private BigDecimal amount;
    
    /**
     * 支付流水号
     */
    private String paymentNo;
    
    /**
     * 微信预支付ID（用于调起支付）
     */
    private String prepayId;
    
    /**
     * 时间戳
     */
    private String timeStamp;
    
    /**
     * 随机字符串
     */
    private String nonceStr;
    
    /**
     * 签名
     */
    private String paySign;
}

