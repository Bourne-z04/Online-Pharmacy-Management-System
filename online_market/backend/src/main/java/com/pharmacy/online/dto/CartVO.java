package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 购物车视图对象
 */
@Data
public class CartVO {
    
    /**
     * 购物车ID
     */
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 商品编号
     */
    private String productCode;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 规格
     */
    private String specification;
    
    /**
     * 主图
     */
    private String mainImage;
    
    /**
     * 单价
     */
    private BigDecimal price;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 小计
     */
    private BigDecimal subtotal;
    
    /**
     * 是否选中：0-否，1-是
     */
    private Integer selected;
    
    /**
     * 是否处方药：0-否，1-是
     */
    private Integer isPrescription;
    
    /**
     * 库存状态：0-无货，1-有货，2-库存不足
     */
    private Integer stockStatus;
    
    /**
     * 可用库存
     */
    private Integer availableStock;
    
    /**
     * 商品状态：0-已下架，1-正常
     */
    private Integer productStatus;
}

