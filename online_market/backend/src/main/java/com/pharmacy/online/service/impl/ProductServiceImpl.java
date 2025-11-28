package com.pharmacy.online.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.dto.ProductDetailVO;
import com.pharmacy.online.dto.ProductListRequest;
import com.pharmacy.online.dto.ProductVO;
import com.pharmacy.online.entity.Inventory;
import com.pharmacy.online.entity.Product;
import com.pharmacy.online.entity.SearchHistory;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.InventoryMapper;
import com.pharmacy.online.mapper.ProductMapper;
import com.pharmacy.online.mapper.SearchHistoryMapper;
import com.pharmacy.online.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private SearchHistoryMapper searchHistoryMapper;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String PRODUCT_CACHE_KEY = "product:detail:";
    private static final String HOT_PRODUCT_KEY = "product:hot";
    
    @Override
    public PageResult<ProductVO> getProductList(ProductListRequest request) {
        Page<Product> pageParam = new Page<>(request.getPage(), request.getSize());
        
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1); // 只查上架商品
        
        // 分类筛选
        if (request.getCategoryId() != null) {
            wrapper.eq(Product::getCategoryId, request.getCategoryId());
        }
        
        // 排序
        String orderBy = request.getOrderBy();
        if ("sales".equals(orderBy)) {
            wrapper.orderByDesc(Product::getSalesVolume);
        } else if ("price_asc".equals(orderBy)) {
            wrapper.orderByAsc(Product::getPrice);
        } else if ("price_desc".equals(orderBy)) {
            wrapper.orderByDesc(Product::getPrice);
        } else {
            wrapper.orderByDesc(Product::getSalesVolume);
        }
        
        Page<Product> result = productMapper.selectPage(pageParam, wrapper);
        
        // 转换VO
        List<ProductVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageResult<>(result.getTotal(), voList, 
                result.getCurrent(), result.getSize());
    }
    
    @Override
    public ProductDetailVO getProductDetail(Long id) {
        // 先从缓存获取
        String cacheKey = PRODUCT_CACHE_KEY + id;
        ProductDetailVO cached = (ProductDetailVO) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }
        
        // 查询商品信息
        Product product = productMapper.selectById(id);
        if (product == null || product.getStatus() == 0) {
            throw new BusinessException(404, "商品不存在或已下架");
        }
        
        // 查询库存信息
        LambdaQueryWrapper<Inventory> invWrapper = new LambdaQueryWrapper<>();
        invWrapper.eq(Inventory::getProductId, product.getProductId());
        Inventory inventory = inventoryMapper.selectOne(invWrapper);
        
        // 转换VO
        ProductDetailVO vo = new ProductDetailVO();
        BeanUtils.copyProperties(product, vo);
        
        // 解析图片JSON
        if (StrUtil.isNotBlank(product.getImages())) {
            vo.setImages(JSON.parseArray(product.getImages(), String.class));
        }
        
        // 设置库存信息
        if (inventory != null) {
            vo.setAvailableStock(inventory.getAvailableStock());
            vo.setStockStatus(inventory.getAvailableStock() > 0 ? 1 : 0);
        } else {
            vo.setAvailableStock(0);
            vo.setStockStatus(0);
        }
        
        // 缓存5分钟
        redisTemplate.opsForValue().set(cacheKey, vo, 5, TimeUnit.MINUTES);
        
        return vo;
    }
    
    @Override
    public List<ProductVO> searchProduct(String keyword, String userId) {
        // 保存搜索历史
        if (StrUtil.isNotBlank(userId)) {
            saveSearchHistory(userId, keyword);
        }
        
        // 模糊查询商品名称
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        wrapper.like(Product::getProductName, keyword)
                .or()
                .like(Product::getIndication, keyword);
        wrapper.orderByDesc(Product::getSalesVolume);
        wrapper.last("LIMIT 50"); // 限制50条
        
        List<Product> products = productMapper.selectList(wrapper);
        
        // 如果没有结果，推荐热销商品
        if (products.isEmpty()) {
            log.info("搜索无结果，推荐热销商品: keyword={}", keyword);
            return getHotProducts(5);
        }
        
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductVO> getHotProducts(Integer size) {
        // 先从缓存获取
        List<ProductVO> cached = (List<ProductVO>) redisTemplate.opsForValue().get(HOT_PRODUCT_KEY);
        if (cached != null && !cached.isEmpty()) {
            return cached.stream().limit(size).collect(Collectors.toList());
        }
        
        // 查询热销商品
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        wrapper.orderByDesc(Product::getSalesVolume);
        wrapper.last("LIMIT " + size);
        
        List<Product> products = productMapper.selectList(wrapper);
        List<ProductVO> voList = products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        // 缓存10分钟
        redisTemplate.opsForValue().set(HOT_PRODUCT_KEY, voList, 10, TimeUnit.MINUTES);
        
        return voList;
    }
    
    @Override
    public List<ProductVO> getRecommendProducts(Integer size) {
        // 简单推荐：返回最新上架的商品
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getStatus, 1);
        wrapper.orderByDesc(Product::getCreateTime);
        wrapper.last("LIMIT " + size);
        
        List<Product> products = productMapper.selectList(wrapper);
        return products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<String> getSearchHistory(String userId) {
        LambdaQueryWrapper<SearchHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SearchHistory::getUserId, userId);
        wrapper.orderByDesc(SearchHistory::getCreateTime);
        wrapper.last("LIMIT 20"); // 最多返回20条
        
        List<SearchHistory> histories = searchHistoryMapper.selectList(wrapper);
        return histories.stream()
                .map(SearchHistory::getKeyword)
                .distinct()
                .collect(Collectors.toList());
    }
    
    @Override
    public void clearSearchHistory(String userId) {
        LambdaQueryWrapper<SearchHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SearchHistory::getUserId, userId);
        searchHistoryMapper.delete(wrapper);
        
        log.info("清空搜索历史成功: userId={}", userId);
    }
    
    /**
     * 保存搜索历史
     */
    private void saveSearchHistory(String userId, String keyword) {
        try {
            SearchHistory history = new SearchHistory();
            history.setUserId(userId);
            history.setKeyword(keyword);
            searchHistoryMapper.insert(history);
        } catch (Exception e) {
            log.error("保存搜索历史失败", e);
            // 不影响主流程
        }
    }
    
    /**
     * 转换为VO
     */
    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        
        // 查询库存状态
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getProductId, product.getProductId());
        Inventory inventory = inventoryMapper.selectOne(wrapper);
        
        if (inventory != null && inventory.getAvailableStock() > 0) {
            vo.setStockStatus(1);
        } else {
            vo.setStockStatus(0);
        }
        
        return vo;
    }
}

