package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加购物车请求DTO
 */
@Data
public class CartAddRequest {
    
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
    @Max(value = 99, message = "数量不能大于99")
    private Integer quantity;
}

