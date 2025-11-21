package com.pharmacy.online.service;

import com.pharmacy.online.dto.CategoryVO;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    
    /**
     * 获取分类列表
     */
    List<CategoryVO> getCategoryList();
    
    /**
     * 获取分类树
     */
    List<CategoryVO> getCategoryTree();
}

