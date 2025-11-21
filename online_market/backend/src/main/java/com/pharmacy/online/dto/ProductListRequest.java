package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.Min;

/**
 * 商品列表请求DTO
 */
@Data
public class ProductListRequest {
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 当前页码
     */
    @Min(value = 1, message = "页码必须大于0")
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于0")
    private Integer size = 20;
    
    /**
     * 排序方式：sales-销量，price_asc-价格升序，price_desc-价格降序
     */
    private String orderBy = "sales";
}

