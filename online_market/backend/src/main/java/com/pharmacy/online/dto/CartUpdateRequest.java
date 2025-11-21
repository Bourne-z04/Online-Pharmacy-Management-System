package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 更新购物车请求DTO
 */
@Data
public class CartUpdateRequest {
    
    /**
     * 数量
     */
    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量不能小于1")
    @Max(value = 99, message = "数量不能大于99")
    private Integer quantity;
    
    /**
     * 是否选中：0-否，1-是
     */
    private Integer selected;
}

