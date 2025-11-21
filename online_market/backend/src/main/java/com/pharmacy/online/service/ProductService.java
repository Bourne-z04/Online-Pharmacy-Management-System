package com.pharmacy.online.service;

import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.dto.ProductDetailVO;
import com.pharmacy.online.dto.ProductListRequest;
import com.pharmacy.online.dto.ProductVO;

import java.util.List;

/**
 * 商品服务接口
 */
public interface ProductService {
    
    /**
     * 获取商品列表
     */
    PageResult<ProductVO> getProductList(ProductListRequest request);
    
    /**
     * 获取商品详情
     */
    ProductDetailVO getProductDetail(Long id);
    
    /**
     * 搜索商品
     */
    List<ProductVO> searchProduct(String keyword, String userId);
    
    /**
     * 获取热销商品
     */
    List<ProductVO> getHotProducts(Integer size);
    
    /**
     * 获取推荐商品
     */
    List<ProductVO> getRecommendProducts(Integer size);
    
    /**
     * 获取搜索历史
     */
    List<String> getSearchHistory(String userId);
    
    /**
     * 清空搜索历史
     */
    void clearSearchHistory(String userId);
}

