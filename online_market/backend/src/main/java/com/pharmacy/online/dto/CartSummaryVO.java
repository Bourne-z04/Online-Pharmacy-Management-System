package com.pharmacy.online.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 购物车汇总视图对象
 */
@Data
public class CartSummaryVO {
    
    /**
     * 总商品数量
     */
    private Integer totalCount;
    
    /**
     * 已选中商品数量
     */
    private Integer selectedCount;
    
    /**
     * 已选中商品总价
     */
    private BigDecimal totalAmount;
    
    /**
     * 是否包含处方药
     */
    private Boolean hasPrescription;
    
    /**
     * 是否全选
     */
    private Boolean allSelected;
}

