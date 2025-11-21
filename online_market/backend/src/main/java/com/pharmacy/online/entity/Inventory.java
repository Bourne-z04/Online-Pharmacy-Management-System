package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存实体类
 */
@Data
@TableName("inventory")
public class Inventory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 库存ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品编号
     */
    private String productId;
    
    /**
     * 可用库存
     */
    private Integer availableStock;
    
    /**
     * 冻结库存
     */
    private Integer frozenStock;
    
    /**
     * 总库存
     */
    private Integer totalStock;
    
    /**
     * 预警库存
     */
    private Integer warningStock;
    
    /**
     * 版本号（乐观锁）
     */
    @Version
    private Integer version;
    
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

