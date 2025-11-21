package com.pharmacy.business.controller;

import com.pharmacy.business.common.Result;
import com.pharmacy.business.dto.ProductManageRequest;
import com.pharmacy.business.dto.ProductManageVO;
import com.pharmacy.business.service.ProductManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * B端商品管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductManageController {
    
    @Autowired
    private ProductManageService productService;
    
    /**
     * 获取商品列表
     */
    @GetMapping("/list")
    public Result<List<ProductManageVO>> getProductList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status) {
        log.info("获取商品列表: keyword={}, status={}", keyword, status);
        List<ProductManageVO> list = productService.getProductList(keyword, status);
        return Result.success(list);
    }
    
    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    public Result<ProductManageVO> getProduct(@PathVariable Long id) {
        log.info("获取商品详情: id={}", id);
        ProductManageVO product = productService.getProduct(id);
        return Result.success(product);
    }
    
    /**
     * 添加商品
     */
    @PostMapping
    public Result<Long> addProduct(@RequestAttribute("adminId") Long adminId,
                                    @Valid @RequestBody ProductManageRequest request) {
        log.info("添加商品: adminId={}, request={}", adminId, request);
        Long productId = productService.addProduct(request);
        return Result.success("添加成功", productId);
    }
    
    /**
     * 更新商品
     */
    @PutMapping("/{id}")
    public Result<Void> updateProduct(@RequestAttribute("adminId") Long adminId,
                                       @PathVariable Long id,
                                       @Valid @RequestBody ProductManageRequest request) {
        log.info("更新商品: adminId={}, id={}, request={}", adminId, id, request);
        productService.updateProduct(id, request);
        return Result.success("更新成功");
    }
    
    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteProduct(@RequestAttribute("adminId") Long adminId,
                                       @PathVariable Long id) {
        log.info("删除商品: adminId={}, id={}", adminId, id);
        productService.deleteProduct(id);
        return Result.success("删除成功");
    }
    
    /**
     * 上架商品
     */
    @PutMapping("/{id}/online")
    public Result<Void> onlineProduct(@RequestAttribute("adminId") Long adminId,
                                       @PathVariable Long id) {
        log.info("上架商品: adminId={}, id={}", adminId, id);
        productService.onlineProduct(id);
        return Result.success("上架成功");
    }
    
    /**
     * 下架商品
     */
    @PutMapping("/{id}/offline")
    public Result<Void> offlineProduct(@RequestAttribute("adminId") Long adminId,
                                        @PathVariable Long id) {
        log.info("下架商品: adminId={}, id={}", adminId, id);
        productService.offlineProduct(id);
        return Result.success("下架成功");
    }
}

