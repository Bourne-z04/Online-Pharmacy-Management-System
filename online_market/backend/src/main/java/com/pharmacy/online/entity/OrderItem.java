package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细实体类
 */
@Data
@TableName("order_item")
public class OrderItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 明细ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
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
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

