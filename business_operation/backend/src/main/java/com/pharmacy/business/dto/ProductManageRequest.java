package com.pharmacy.business.dto;

import lombok.Data;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品管理请求DTO
 */
@Data
public class ProductManageRequest {
    
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String productName;
    
    /**
     * 分类ID
     */
    @NotNull(message = "分类不能为空")
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
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
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
}

