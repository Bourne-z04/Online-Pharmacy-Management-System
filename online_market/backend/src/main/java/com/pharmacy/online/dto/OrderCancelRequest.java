package com.pharmacy.online.dto;

import lombok.Data;

/**
 * 取消订单请求DTO
 */
@Data
public class OrderCancelRequest {
    
    /**
     * 取消原因
     */
    private String reason;
}

