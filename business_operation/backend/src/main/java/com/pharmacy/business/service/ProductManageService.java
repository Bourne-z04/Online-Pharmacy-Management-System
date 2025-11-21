package com.pharmacy.business.service;

import com.pharmacy.business.dto.ProductManageRequest;
import com.pharmacy.business.dto.ProductManageVO;

import java.util.List;

/**
 * B端商品管理服务接口
 */
public interface ProductManageService {
    
    /**
     * 获取商品列表
     */
    List<ProductManageVO> getProductList(String keyword, Integer status);
    
    /**
     * 获取商品详情
     */
    ProductManageVO getProduct(Long id);
    
    /**
     * 添加商品
     */
    Long addProduct(ProductManageRequest request);
    
    /**
     * 更新商品
     */
    void updateProduct(Long id, ProductManageRequest request);
    
    /**
     * 删除商品
     */
    void deleteProduct(Long id);
    
    /**
     * 上架商品
     */
    void onlineProduct(Long id);
    
    /**
     * 下架商品
     */
    void offlineProduct(Long id);
}

