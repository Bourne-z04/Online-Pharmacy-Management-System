package com.pharmacy.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pharmacy.online.dto.CategoryVO;
import com.pharmacy.online.entity.Category;
import com.pharmacy.online.mapper.CategoryMapper;
import com.pharmacy.online.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 分类服务实现类
 */
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CATEGORY_LIST_KEY = "category:list";
    private static final String CATEGORY_TREE_KEY = "category:tree";
    
    @Override
    public List<CategoryVO> getCategoryList() {
        // 先从缓存获取
        List<CategoryVO> cached = (List<CategoryVO>) redisTemplate.opsForValue().get(CATEGORY_LIST_KEY);
        if (cached != null) {
            return cached;
        }
        
        // 查询所有启用的分类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, 1);
        wrapper.orderByAsc(Category::getSortOrder);
        
        List<Category> categories = categoryMapper.selectList(wrapper);
        List<CategoryVO> voList = categories.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 缓存30分钟
        redisTemplate.opsForValue().set(CATEGORY_LIST_KEY, voList, 30, TimeUnit.MINUTES);
        
        return voList;
    }
    
    @Override
    public List<CategoryVO> getCategoryTree() {
        // 先从缓存获取
        List<CategoryVO> cached = (List<CategoryVO>) redisTemplate.opsForValue().get(CATEGORY_TREE_KEY);
        if (cached != null) {
            return cached;
        }
        
        // 查询所有分类
        List<CategoryVO> allCategories = getCategoryList();
        
        // 构建树形结构
        Map<Long, List<CategoryVO>> childrenMap = allCategories.stream()
                .filter(c -> c.getParentId() != 0)
                .collect(Collectors.grouping(CategoryVO::getParentId));
        
        // 设置子分类
        for (CategoryVO category : allCategories) {
            List<CategoryVO> children = childrenMap.get(category.getId());
            if (children != null) {
                category.setChildren(children);
            }
        }
        
        // 只返回一级分类
        List<CategoryVO> tree = allCategories.stream()
                .filter(c -> c.getParentId() == 0)
                .collect(Collectors.toList());
        
        // 缓存30分钟
        redisTemplate.opsForValue().set(CATEGORY_TREE_KEY, tree, 30, TimeUnit.MINUTES);
        
        return tree;
    }
    
    /**
     * 转换为VO
     */
    private CategoryVO convertToVO(Category category) {
        CategoryVO vo = new CategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}

