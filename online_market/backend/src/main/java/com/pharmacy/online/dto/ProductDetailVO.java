package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情视图对象
 */
@Data
public class ProductDetailVO {
    
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
     * 品牌
     */
    private String brand;
    
    /**
     * 规格
     */
    private String specification;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 批准文号
     */
    private String approvalNumber;
    
    /**
     * 生产厂家
     */
    private String manufacturer;
    
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
     * 商品图片列表
     */
    private List<String> images;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 适应症
     */
    private String indication;
    
    /**
     * 用法用量
     */
    private String usage;
    
    /**
     * 禁忌
     */
    private String taboo;
    
    /**
     * 是否处方药：0-否，1-是
     */
    private Integer isPrescription;
    
    /**
     * 销量
     */
    private Integer salesVolume;
    
    /**
     * 可用库存
     */
    private Integer availableStock;
    
    /**
     * 库存状态：0-无货，1-有货
     */
    private Integer stockStatus;
}

