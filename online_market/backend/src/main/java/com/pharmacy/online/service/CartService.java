package com.pharmacy.online.service;

import com.pharmacy.online.dto.CartAddRequest;
import com.pharmacy.online.dto.CartSummaryVO;
import com.pharmacy.online.dto.CartUpdateRequest;
import com.pharmacy.online.dto.CartVO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {
    
    /**
     * 获取购物车列表
     */
    List<CartVO> getCartList(String userId);
    
    /**
     * 添加商品到购物车
     */
    void addToCart(String userId, CartAddRequest request);
    
    /**
     * 更新购物车商品
     */
    void updateCart(String userId, Long id, CartUpdateRequest request);
    
    /**
     * 删除购物车商品
     */
    void deleteCart(String userId, Long id);
    
    /**
     * 批量删除购物车商品
     */
    void batchDelete(String userId, List<Long> ids);
    
    /**
     * 选中/取消选中商品
     */
    void toggleSelected(String userId, Long id, Integer selected);
    
    /**
     * 全选/取消全选
     */
    void selectAll(String userId, Integer selected);
    
    /**
     * 获取购物车汇总信息
     */
    CartSummaryVO getCartSummary(String userId);
    
    /**
     * 获取购物车商品数量
     */
    Integer getCartCount(String userId);
}

