package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
<<<<<<< HEAD
 * 售后信息视图对象
=======
 * 售后视图对象
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
 */
@Data
public class AfterSaleVO {
    
    /**
     * 售后单号
     */
    private String afterSaleNo;
    
    /**
     * 订单号
     */
    private String orderNo;
    
    /**
<<<<<<< HEAD
     * 类型：REFUND-退款，RETURN-退货退款
=======
     * 类型
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private String type;
    
    /**
     * 类型描述
     */
    private String typeDesc;
    
    /**
     * 原因
     */
    private String reason;
    
    /**
<<<<<<< HEAD
     * 凭证图片列表
=======
     * 凭证图片
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private List<String> evidenceImages;
    
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    
    /**
<<<<<<< HEAD
     * 状态：PENDING-待审核，APPROVED-已同意，REJECTED-已拒绝，REFUNDED-已退款
=======
     * 状态
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     */
    private String status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
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
    private LocalDateTime createTime;
}

