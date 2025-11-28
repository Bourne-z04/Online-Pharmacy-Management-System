package com.pharmacy.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pharmacy.online.entity.Inventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存Mapper接口
 */
@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {
    
    /**
     * 扣减库存（乐观锁）
     */
    int deductStock(@Param("productId") String productId, 
                    @Param("quantity") Integer quantity, 
                    @Param("version") Integer version);
    
    /**
     * 冻结库存
     */
    int freezeStock(@Param("productId") String productId, 
                    @Param("quantity") Integer quantity, 
                    @Param("version") Integer version);
    
    /**
     * 解冻库存
     */
    int unfreezeStock(@Param("productId") String productId, 
                      @Param("quantity") Integer quantity, 
                      @Param("version") Integer version);
}

