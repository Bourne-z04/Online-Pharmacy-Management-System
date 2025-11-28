package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 订单商品请求DTO
 */
@Data
public class OrderItemRequest {
    
    /**
     * 商品编号
     */
    @NotBlank(message = "商品编号不能为空")
    private String productId;
    
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    private Integer quantity;
}

