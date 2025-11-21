package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品视图对象
 */
@Data
public class ProductVO {
    
    /**
     * 商品ID
     */
    private Long id;
    
    /**
     * 商品编号
     */
    private String productId;
    
    /**
     * 商品名称
     */
    private String productName;
    
    /**
     * 规格
     */
    private String specification;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 主图
     */
    private String mainImage;
    
    /**
     * 是否处方药：0-否，1-是
     */
    private Integer isPrescription;
    
    /**
     * 销量
     */
    private Integer salesVolume;
    
    /**
     * 库存状态：0-无货，1-有货
     */
    private Integer stockStatus;
}

