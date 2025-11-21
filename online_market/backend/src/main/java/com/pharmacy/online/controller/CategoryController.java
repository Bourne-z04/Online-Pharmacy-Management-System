package com.pharmacy.online.controller;

import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.CategoryVO;
import com.pharmacy.online.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 商品分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * 获取分类列表
     */
    @GetMapping("/list")
    public Result<List<CategoryVO>> getCategoryList() {
        log.info("获取分类列表");
        List<CategoryVO> categories = categoryService.getCategoryList();
        return Result.success(categories);
    }
    
    /**
     * 获取分类树
     */
    @GetMapping("/tree")
    public Result<List<CategoryVO>> getCategoryTree() {
        log.info("获取分类树");
        List<CategoryVO> tree = categoryService.getCategoryTree();
        return Result.success(tree);
    }
}

