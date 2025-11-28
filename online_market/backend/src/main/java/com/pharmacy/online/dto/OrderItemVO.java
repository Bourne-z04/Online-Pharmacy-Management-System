package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 订单商品视图对象
 */
@Data
public class OrderItemVO {
    
    /**
     * 商品编号
     */
    private String productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 商品图片
     */
    private String productImage;
    
    /**
     * 规格
     */
    private String specification;
    
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 小计
     */
    private BigDecimal subtotal;
    
    /**
     * 是否处方药：0-否，1-是
     */
    private Integer isPrescription;
}

