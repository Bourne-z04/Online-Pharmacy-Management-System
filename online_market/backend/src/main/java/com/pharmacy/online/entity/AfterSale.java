package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 售后实体类
 */
@Data
@TableName("after_sale")
public class AfterSale implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 售后ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 售后单号
     */
    private String afterSaleNo;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 类型：REFUND-退款，RETURN-退货退款
     */
    private String type;
    
    /**
     * 原因
     */
    private String reason;
    
    /**
     * 凭证图片，JSON数组
     */
    private String evidenceImages;
    
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    
    /**
     * 状态：PENDING-待审核，APPROVED-已同意，REJECTED-已拒绝，REFUNDED-已退款
     */
    private String status;
    
    /**
     * 审核时间
     */
    private LocalDateTime auditTime;
    
    /**
     * 拒绝原因
     */
    private String rejectReason;
    
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;
    
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

