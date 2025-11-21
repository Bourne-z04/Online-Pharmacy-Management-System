package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 购物车实体类
 */
@Data
@TableName("cart")
public class Cart implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 购物车ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 商品编号
     */
    private String productId;
    
    /**
     * 数量
     */
    private Integer quantity;
    
    /**
     * 是否选中：0-否，1-是
     */
    private Integer selected;
    
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
}

