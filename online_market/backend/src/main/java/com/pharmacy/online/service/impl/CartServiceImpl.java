package com.pharmacy.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pharmacy.online.dto.CartAddRequest;
import com.pharmacy.online.dto.CartSummaryVO;
import com.pharmacy.online.dto.CartUpdateRequest;
import com.pharmacy.online.dto.CartVO;
import com.pharmacy.online.entity.Cart;
import com.pharmacy.online.entity.Inventory;
import com.pharmacy.online.entity.Product;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.CartMapper;
import com.pharmacy.online.mapper.InventoryMapper;
import com.pharmacy.online.mapper.ProductMapper;
import com.pharmacy.online.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartMapper cartMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Override
    public List<CartVO> getCartList(String userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.orderByDesc(Cart::getCreateTime);
        
        List<Cart> carts = cartMapper.selectList(wrapper);
        
        return carts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(String userId, CartAddRequest request) {
        // 1. 检查商品是否存在且上架
        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(Product::getProductId, request.getProductId());
        Product product = productMapper.selectOne(productWrapper);
        
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        if (product.getStatus() == 0) {
            throw new BusinessException("商品已下架");
        }
        
        // 2. 检查库存是否充足
        LambdaQueryWrapper<Inventory> invWrapper = new LambdaQueryWrapper<>();
        invWrapper.eq(Inventory::getProductId, request.getProductId());
        Inventory inventory = inventoryMapper.selectOne(invWrapper);
        
        if (inventory == null || inventory.getAvailableStock() < request.getQuantity()) {
            throw new BusinessException("库存不足");
        }
        
        // 3. 查询购物车是否已有该商品
        LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
        cartWrapper.eq(Cart::getUserId, userId);
        cartWrapper.eq(Cart::getProductId, request.getProductId());
        Cart existCart = cartMapper.selectOne(cartWrapper);
        
        if (existCart != null) {
            // 已存在，累加数量
            int newQuantity = existCart.getQuantity() + request.getQuantity();
            if (newQuantity > 99) {
                newQuantity = 99; // 限制最大数量
            }
            if (newQuantity > inventory.getAvailableStock()) {
                throw new BusinessException("添加数量超过库存");
            }
            existCart.setQuantity(newQuantity);
            cartMapper.updateById(existCart);
            log.info("更新购物车数量: userId={}, productId={}, quantity={}", 
                    userId, request.getProductId(), newQuantity);
        } else {
            // 不存在，新增
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(request.getProductId());
            cart.setQuantity(request.getQuantity());
            cart.setSelected(1);
            cartMapper.insert(cart);
            log.info("添加商品到购物车: userId={}, productId={}, quantity={}", 
                    userId, request.getProductId(), request.getQuantity());
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCart(String userId, Long id, CartUpdateRequest request) {
        // 1. 查询购物车商品
        Cart cart = getCartByIdAndUserId(id, userId);
        
        // 2. 检查库存
        if (request.getQuantity() != null) {
            LambdaQueryWrapper<Inventory> invWrapper = new LambdaQueryWrapper<>();
            invWrapper.eq(Inventory::getProductId, cart.getProductId());
            Inventory inventory = inventoryMapper.selectOne(invWrapper);
            
            if (inventory == null || inventory.getAvailableStock() < request.getQuantity()) {
                throw new BusinessException("库存不足");
            }
            
            cart.setQuantity(request.getQuantity());
        }
        
        // 3. 更新选中状态
        if (request.getSelected() != null) {
            cart.setSelected(request.getSelected());
        }
        
        cartMapper.updateById(cart);
        log.info("更新购物车成功: userId={}, id={}", userId, id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCart(String userId, Long id) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getId, id);
        wrapper.eq(Cart::getUserId, userId);
        
        int rows = cartMapper.delete(wrapper);
        if (rows == 0) {
            throw new BusinessException("购物车商品不存在");
        }
        
        log.info("删除购物车商品成功: userId={}, id={}", userId, id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(String userId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的商品");
        }
        
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.in(Cart::getId, ids);
        
        cartMapper.delete(wrapper);
        log.info("批量删除购物车商品成功: userId={}, count={}", userId, ids.size());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleSelected(String userId, Long id, Integer selected) {
        Cart cart = getCartByIdAndUserId(id, userId);
        cart.setSelected(selected);
        cartMapper.updateById(cart);
        
        log.info("切换选中状态成功: userId={}, id={}, selected={}", userId, id, selected);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectAll(String userId, Integer selected) {
        LambdaUpdateWrapper<Cart> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        wrapper.set(Cart::getSelected, selected);
        
        cartMapper.update(null, wrapper);
        log.info("全选/取消全选成功: userId={}, selected={}", userId, selected);
    }
    
    @Override
    public CartSummaryVO getCartSummary(String userId) {
        List<CartVO> cartList = getCartList(userId);
        
        CartSummaryVO summary = new CartSummaryVO();
        summary.setTotalCount(cartList.size());
        
        // 统计已选中商品
        List<CartVO> selectedItems = cartList.stream()
                .filter(item -> item.getSelected() == 1)
                .collect(Collectors.toList());
        
        summary.setSelectedCount(selectedItems.size());
        
        // 计算总价
        BigDecimal totalAmount = selectedItems.stream()
                .map(CartVO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        summary.setTotalAmount(totalAmount);
        
        // 检查是否包含处方药
        boolean hasPrescription = selectedItems.stream()
                .anyMatch(item -> item.getIsPrescription() == 1);
        summary.setHasPrescription(hasPrescription);
        
        // 是否全选
        summary.setAllSelected(cartList.size() > 0 && cartList.size() == selectedItems.size());
        
        return summary;
    }
    
    @Override
    public Integer getCartCount(String userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);
        
        Long count = cartMapper.selectCount(wrapper);
        return count.intValue();
    }
    
    /**
     * 根据ID和用户ID获取购物车商品
     */
    private Cart getCartByIdAndUserId(Long id, String userId) {
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getId, id);
        wrapper.eq(Cart::getUserId, userId);
        
        Cart cart = cartMapper.selectOne(wrapper);
        if (cart == null) {
            throw new BusinessException("购物车商品不存在");
        }
        return cart;
    }
    
    /**
     * 转换为VO
     */
    private CartVO convertToVO(Cart cart) {
        CartVO vo = new CartVO();
        vo.setId(cart.getId());
        vo.setQuantity(cart.getQuantity());
        vo.setSelected(cart.getSelected());
        
        // 查询商品信息
        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(Product::getProductId, cart.getProductId());
        Product product = productMapper.selectOne(productWrapper);
        
        if (product != null) {
            vo.setProductId(product.getId());
            vo.setProductCode(product.getProductId());
            vo.setProductName(product.getProductName());
            vo.setSpecification(product.getSpecification());
            vo.setMainImage(product.getMainImage());
            vo.setPrice(product.getPrice());
            vo.setIsPrescription(product.getIsPrescription());
            vo.setProductStatus(product.getStatus());
            
            // 计算小计
            vo.setSubtotal(product.getPrice().multiply(new BigDecimal(cart.getQuantity())));
            
            // 查询库存信息
            LambdaQueryWrapper<Inventory> invWrapper = new LambdaQueryWrapper<>();
            invWrapper.eq(Inventory::getProductId, cart.getProductId());
            Inventory inventory = inventoryMapper.selectOne(invWrapper);
            
            if (inventory != null) {
                vo.setAvailableStock(inventory.getAvailableStock());
                
                if (inventory.getAvailableStock() == 0) {
                    vo.setStockStatus(0); // 无货
                } else if (inventory.getAvailableStock() < cart.getQuantity()) {
                    vo.setStockStatus(2); // 库存不足
                } else {
                    vo.setStockStatus(1); // 有货
                }
            } else {
                vo.setAvailableStock(0);
                vo.setStockStatus(0);
            }
        }
        
        return vo;
    }
}

