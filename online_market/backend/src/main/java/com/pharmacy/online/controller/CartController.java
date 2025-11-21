package com.pharmacy.online.controller;

import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.CartAddRequest;
import com.pharmacy.online.dto.CartSummaryVO;
import com.pharmacy.online.dto.CartUpdateRequest;
import com.pharmacy.online.dto.CartVO;
import com.pharmacy.online.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 购物车控制器
 */
@Slf4j
@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * 获取购物车列表
     */
    @GetMapping("/list")
    public Result<List<CartVO>> getCartList(@RequestAttribute("userId") String userId) {
        log.info("获取购物车列表: userId={}", userId);
        List<CartVO> cartList = cartService.getCartList(userId);
        return Result.success(cartList);
    }
    
    /**
     * 添加商品到购物车
     */
    @PostMapping
    public Result<Void> addToCart(@RequestAttribute("userId") String userId,
                                   @Valid @RequestBody CartAddRequest request) {
        log.info("添加商品到购物车: userId={}, request={}", userId, request);
        cartService.addToCart(userId, request);
        return Result.success("添加成功");
    }
    
    /**
     * 更新购物车商品
     */
    @PutMapping("/{id}")
    public Result<Void> updateCart(@RequestAttribute("userId") String userId,
                                    @PathVariable Long id,
                                    @Valid @RequestBody CartUpdateRequest request) {
        log.info("更新购物车: userId={}, id={}, request={}", userId, id, request);
        cartService.updateCart(userId, id, request);
        return Result.success("更新成功");
    }
    
    /**
     * 删除购物车商品
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteCart(@RequestAttribute("userId") String userId,
                                    @PathVariable Long id) {
        log.info("删除购物车商品: userId={}, id={}", userId, id);
        cartService.deleteCart(userId, id);
        return Result.success("删除成功");
    }
    
    /**
     * 批量删除购物车商品
     */
    @PostMapping("/batch-delete")
    public Result<Void> batchDelete(@RequestAttribute("userId") String userId,
                                     @RequestBody List<Long> ids) {
        log.info("批量删除购物车商品: userId={}, ids={}", userId, ids);
        cartService.batchDelete(userId, ids);
        return Result.success("删除成功");
    }
    
    /**
     * 选中/取消选中商品
     */
    @PutMapping("/{id}/selected")
    public Result<Void> toggleSelected(@RequestAttribute("userId") String userId,
                                        @PathVariable Long id,
                                        @RequestParam Integer selected) {
        log.info("切换选中状态: userId={}, id={}, selected={}", userId, id, selected);
        cartService.toggleSelected(userId, id, selected);
        return Result.success("操作成功");
    }
    
    /**
     * 全选/取消全选
     */
    @PutMapping("/select-all")
    public Result<Void> selectAll(@RequestAttribute("userId") String userId,
                                   @RequestParam Integer selected) {
        log.info("全选/取消全选: userId={}, selected={}", userId, selected);
        cartService.selectAll(userId, selected);
        return Result.success("操作成功");
    }
    
    /**
     * 获取购物车汇总信息
     */
    @GetMapping("/summary")
    public Result<CartSummaryVO> getCartSummary(@RequestAttribute("userId") String userId) {
        log.info("获取购物车汇总: userId={}", userId);
        CartSummaryVO summary = cartService.getCartSummary(userId);
        return Result.success(summary);
    }
    
    /**
     * 获取购物车商品数量
     */
    @GetMapping("/count")
    public Result<Integer> getCartCount(@RequestAttribute("userId") String userId) {
        log.info("获取购物车数量: userId={}", userId);
        Integer count = cartService.getCartCount(userId);
        return Result.success(count);
    }
}

