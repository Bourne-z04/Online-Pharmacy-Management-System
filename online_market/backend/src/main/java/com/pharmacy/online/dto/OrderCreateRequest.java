package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单请求DTO
 */
@Data
public class OrderCreateRequest {
    
    /**
     * 收货地址ID
     */
    @NotNull(message = "收货地址不能为空")
    private Long addressId;
    
    /**
     * 配送方式：COURIER-骑手配送，PICKUP-到店自提
     */
    @NotBlank(message = "配送方式不能为空")
    private String deliveryMethod;
    
    /**
     * 配送时段（骑手配送时必填）
     */
    private String deliveryTimeSlot;
    
    /**
     * 自提门店ID（到店自提时必填）
     */
    private Long storeId;
    
    /**
     * 购物车商品ID列表（从购物车下单）
     */
    private List<Long> cartIds;
    
    /**
     * 直接购买商品列表（立即购买）
     */
    private List<OrderItemRequest> items;
    
    /**
     * 备注
     */
    private String remark;
}

