package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 物流信息实体类
 */
@Data
@TableName("logistics")
public class Logistics implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 物流ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 配送平台
     */
    private String deliveryPlatform;
    
    /**
     * 配送单号
     */
    private String deliveryNo;
    
    /**
     * 骑手姓名
     */
    private String courierName;
    
    /**
     * 骑手电话
     */
    private String courierPhone;
    
    /**
     * 状态：ACCEPTED-已接单，PICKED-已取货，DELIVERING-配送中，DELIVERED-已送达
     */
    private String status;
    
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

