package com.pharmacy.online.dto;

import lombok.Data;
import java.util.List;

/**
 * 分类视图对象
 */
@Data
public class CategoryVO {
    
    /**
     * 分类ID
     */
    private Long id;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 父分类ID
     */
    private Long parentId;
    
    /**
     * 分类图标
     */
    private String icon;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 子分类列表
     */
    private List<CategoryVO> children;
}

