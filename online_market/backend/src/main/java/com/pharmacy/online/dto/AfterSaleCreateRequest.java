package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
<<<<<<< HEAD
import java.util.List;

/**
 * 创建售后申请请求DTO
=======

/**
 * 创建售后请求DTO
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
 */
@Data
public class AfterSaleCreateRequest {
    
    /**
     * 订单号
     */
    @NotBlank(message = "订单号不能为空")
    private String orderNo;
    
    /**
     * 类型：REFUND-退款，RETURN-退货退款
     */
    @NotBlank(message = "售后类型不能为空")
    private String type;
    
    /**
     * 原因
     */
    @NotBlank(message = "售后原因不能为空")
    private String reason;
    
    /**
<<<<<<< HEAD
     * 凭证图片列表
     */
    private List<String> evidenceImages;
    
    /**
=======
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
     * 退款金额
     */
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundAmount;
}

