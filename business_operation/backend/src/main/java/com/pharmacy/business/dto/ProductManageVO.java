package com.pharmacy.business.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品管理视图对象
 */
@Data
public class ProductManageVO {
    
    private Long id;
    private String productId;
    private String productName;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private String specification;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer isPrescription;
    private String mainImage;
    private Integer salesVolume;
    private Integer availableStock;
    private Integer status;
    private String statusDesc;
    private LocalDateTime createTime;
}

