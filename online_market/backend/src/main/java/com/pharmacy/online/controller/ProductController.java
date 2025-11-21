package com.pharmacy.online.controller;

import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.ProductDetailVO;
import com.pharmacy.online.dto.ProductListRequest;
import com.pharmacy.online.dto.ProductVO;
import com.pharmacy.online.dto.SearchRequest;
import com.pharmacy.online.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 商品控制器
 */
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * 商品列表（分页）
     */
    @GetMapping("/list")
    public Result<PageResult<ProductVO>> getProductList(
            @Valid ProductListRequest request) {
        log.info("获取商品列表: request={}", request);
        PageResult<ProductVO> result = productService.getProductList(request);
        return Result.success(result);
    }
    
    /**
     * 商品详情
     */
    @GetMapping("/{id}")
    public Result<ProductDetailVO> getProductDetail(@PathVariable Long id) {
        log.info("获取商品详情: id={}", id);
        ProductDetailVO detail = productService.getProductDetail(id);
        return Result.success(detail);
    }
    
    /**
     * 搜索商品
     */
    @PostMapping("/search")
    public Result<List<ProductVO>> searchProduct(@Valid @RequestBody SearchRequest request,
                                                  @RequestAttribute(required = false) String userId) {
        log.info("搜索商品: keyword={}, userId={}", request.getKeyword(), userId);
        List<ProductVO> products = productService.searchProduct(request.getKeyword(), userId);
        return Result.success(products);
    }
    
    /**
     * 热销商品
     */
    @GetMapping("/hot")
    public Result<List<ProductVO>> getHotProducts(
            @RequestParam(defaultValue = "6") Integer size) {
        log.info("获取热销商品: size={}", size);
        List<ProductVO> products = productService.getHotProducts(size);
        return Result.success(products);
    }
    
    /**
     * 推荐商品
     */
    @GetMapping("/recommend")
    public Result<List<ProductVO>> getRecommendProducts(
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取推荐商品: size={}", size);
        List<ProductVO> products = productService.getRecommendProducts(size);
        return Result.success(products);
    }
    
    /**
     * 获取搜索历史
     */
    @GetMapping("/search/history")
    public Result<List<String>> getSearchHistory(@RequestAttribute String userId) {
        log.info("获取搜索历史: userId={}", userId);
        List<String> history = productService.getSearchHistory(userId);
        return Result.success(history);
    }
    
    /**
     * 清空搜索历史
     */
    @DeleteMapping("/search/history")
    public Result<Void> clearSearchHistory(@RequestAttribute String userId) {
        log.info("清空搜索历史: userId={}", userId);
        productService.clearSearchHistory(userId);
        return Result.success("清空成功");
    }
}

