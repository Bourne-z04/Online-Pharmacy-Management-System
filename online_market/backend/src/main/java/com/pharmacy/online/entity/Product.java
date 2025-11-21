package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
@TableName("product")
public class Product implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 商品ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 分类ID
     */
    private Long categoryId;
    
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
     * 销售价格
     */
    private BigDecimal price;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    
    /**
     * 是否处方药：0-否，1-是
     */
    private Integer isPrescription;
    
    /**
     * 主图
     */
    private String mainImage;
    
    /**
     * 商品图片，JSON数组
     */
    private String images;
    
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
     * 销量
     */
    private Integer salesVolume;
    
    /**
     * 状态：0-下架，1-上架
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * 逻辑删除：0-未删除，1-已删除
     */
    @TableLogic
    private Integer deleted;
}

