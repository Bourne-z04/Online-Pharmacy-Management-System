package com.pharmacy.online.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.dto.*;
import com.pharmacy.online.entity.*;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.*;
import com.pharmacy.online.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderMainMapper orderMainMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private UserAddressMapper addressMapper;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private CartMapper cartMapper;
    
    @Value("${business.order.payment-timeout:7200}")
    private Integer paymentTimeout;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderCreateResultVO createOrder(String userId, OrderCreateRequest request) {
        // 1. 生成订单号: "BD" + 年份 + 8位随机数
        String orderNo = "BD" + DateUtil.thisYear() + IdUtil.randomUUID().substring(0, 8).toUpperCase();
        
        // 2. 验证地址
        UserAddress address = addressMapper.selectById(request.getAddressId());
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException("收货地址不存在");
        }
        
        // 3. 获取订单商品列表
        List<OrderItemRequest> itemRequests = getOrderItems(userId, request);
        if (itemRequests.isEmpty()) {
            throw new BusinessException("订单商品不能为空");
        }
        
        // 4. 验证商品信息和库存，计算金额
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        boolean hasPrescription = false;
        
        for (OrderItemRequest itemReq : itemRequests) {
            // 查询商品
            LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
            productWrapper.eq(Product::getProductId, itemReq.getProductId());
            Product product = productMapper.selectOne(productWrapper);
            
            if (product == null) {
                throw new BusinessException("商品不存在: " + itemReq.getProductId());
            }
            if (product.getStatus() == 0) {
                throw new BusinessException("商品已下架: " + product.getProductName());
            }
            
            // 检查库存
            LambdaQueryWrapper<Inventory> invWrapper = new LambdaQueryWrapper<>();
            invWrapper.eq(Inventory::getProductId, itemReq.getProductId());
            Inventory inventory = inventoryMapper.selectOne(invWrapper);
            
            if (inventory == null || inventory.getAvailableStock() < itemReq.getQuantity()) {
                throw new BusinessException("商品库存不足: " + product.getProductName());
            }
            
            // 创建订单明细
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNo);
            orderItem.setProductId(product.getProductId());
            orderItem.setProductName(product.getProductName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setSpecification(product.getSpecification());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setSubtotal(product.getPrice().multiply(new BigDecimal(itemReq.getQuantity())));
            orderItem.setIsPrescription(product.getIsPrescription());
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
            
            if (product.getIsPrescription() == 1) {
                hasPrescription = true;
            }
        }
        
        // 5. 计算运费和优惠（这里简化处理）
        BigDecimal freight = BigDecimal.ZERO;
        if ("COURIER".equals(request.getDeliveryMethod())) {
            freight = new BigDecimal("5.00"); // 配送费5元
        }
        
        BigDecimal discountAmount = BigDecimal.ZERO;
        BigDecimal payableAmount = totalAmount.add(freight).subtract(discountAmount);
        
        // 6. 锁定库存（乐观锁）
        for (OrderItem item : orderItems) {
            boolean locked = lockInventory(item.getProductId(), item.getQuantity());
            if (!locked) {
                throw new BusinessException("库存锁定失败，请稍后重试");
            }
        }
        
        // 7. 创建订单主记录
        OrderMain order = new OrderMain();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setOrderType(hasPrescription ? "PRESCRIPTION" : "NORMAL");
        order.setStatus("PAYING"); // 待支付
        order.setTotalAmount(totalAmount);
        order.setFreight(freight);
        order.setDiscountAmount(discountAmount);
        order.setPayableAmount(payableAmount);
        order.setDeliveryMethod(request.getDeliveryMethod());
        order.setReceiverName(address.getReceiverName());
        order.setReceiverPhone(address.getReceiverPhone());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverDistrict(address.getDistrict());
        order.setReceiverAddress(address.getDetailAddress());
        order.setDeliveryTimeSlot(request.getDeliveryTimeSlot());
        order.setStoreId(request.getStoreId());
        order.setRemark(request.getRemark());
        
        // 设置支付超时时间（2小时后）
        order.setPaymentTimeout(LocalDateTime.now().plusSeconds(paymentTimeout));
        
        orderMainMapper.insert(order);
        
        // 8. 创建订单明细记录
        for (OrderItem item : orderItems) {
            orderItemMapper.insert(item);
        }
        
        // 9. 清空购物车（如果从购物车下单）
        if (request.getCartIds() != null && !request.getCartIds().isEmpty()) {
            LambdaQueryWrapper<Cart> cartWrapper = new LambdaQueryWrapper<>();
            cartWrapper.eq(Cart::getUserId, userId);
            cartWrapper.in(Cart::getId, request.getCartIds());
            cartMapper.delete(cartWrapper);
        }
        
        // 10. 返回结果
        OrderCreateResultVO result = new OrderCreateResultVO();
        result.setOrderNo(orderNo);
        result.setPayableAmount(payableAmount);
        result.setPaymentTimeout(order.getPaymentTimeout().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        
        log.info("创建订单成功: userId={}, orderNo={}, amount={}", userId, orderNo, payableAmount);
        return result;
    }
    
    @Override
    public PageResult<OrderVO> getOrderList(String userId, String status, Integer page, Integer size) {
        Page<OrderMain> pageParam = new Page<>(page, size);
        
        LambdaQueryWrapper<OrderMain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderMain::getUserId, userId);
        
        // 状态筛选
        if (status != null && !status.isEmpty()) {
            wrapper.eq(OrderMain::getStatus, status);
        }
        
        wrapper.orderByDesc(OrderMain::getCreateTime);
        
        Page<OrderMain> result = orderMainMapper.selectPage(pageParam, wrapper);
        
        List<OrderVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageResult<>(result.getTotal(), voList, result.getCurrent(), result.getSize());
    }
    
    @Override
    public OrderDetailVO getOrderDetail(String userId, String orderNo) {
        OrderMain order = getOrderByNoAndUserId(orderNo, userId);
        return convertToDetailVO(order);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(String userId, String orderNo, String reason) {
        OrderMain order = getOrderByNoAndUserId(orderNo, userId);
        
        // 只有待支付和待审核的订单可以取消
        if (!"PAYING".equals(order.getStatus())) {
            throw new BusinessException("当前订单状态不支持取消，请联系商家处理");
        }
        
        // 释放库存
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderNo, orderNo);
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        
        for (OrderItem item : items) {
            releaseInventory(item.getProductId(), item.getQuantity());
        }
        
        // 更新订单状态
        order.setStatus("CANCELLED");
        order.setCancelReason(reason != null ? reason : "用户取消");
        orderMainMapper.updateById(order);
        
        log.info("取消订单成功: userId={}, orderNo={}", userId, orderNo);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder(String userId, String orderNo) {
        OrderMain order = getOrderByNoAndUserId(orderNo, userId);
        
        if (!"SHIPPING".equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        
        order.setStatus("COMPLETED");
        order.setConfirmTime(LocalDateTime.now());
        order.setCompleteTime(LocalDateTime.now());
        orderMainMapper.updateById(order);
        
        log.info("确认收货成功: userId={}, orderNo={}", userId, orderNo);
    }
    
    @Override
    public PaymentVO createPayment(String userId, String orderNo) {
        OrderMain order = getOrderByNoAndUserId(orderNo, userId);
        
        if (!"PAYING".equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        
        // 检查是否超时
        if (LocalDateTime.now().isAfter(order.getPaymentTimeout())) {
            throw new BusinessException("订单已超时，请重新下单");
        }
        
        // 生成支付流水号
        String paymentNo = "PAY" + System.currentTimeMillis();
        
        // 这里应该调用微信支付接口，这里简化处理
        PaymentVO payment = new PaymentVO();
        payment.setOrderNo(orderNo);
        payment.setAmount(order.getPayableAmount());
        payment.setPaymentNo(paymentNo);
        payment.setPrepayId("mock_prepay_id"); // 实际应从微信获取
        payment.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
        payment.setNonceStr(IdUtil.simpleUUID());
        payment.setPaySign("mock_sign"); // 实际应计算签名
        
        // 更新订单支付流水号
        order.setPaymentNo(paymentNo);
        orderMainMapper.updateById(order);
        
        log.info("创建支付成功: userId={}, orderNo={}, paymentNo={}", userId, orderNo, paymentNo);
        return payment;
    }
    
    @Override
    public String getPaymentStatus(String userId, String orderNo) {
        OrderMain order = getOrderByNoAndUserId(orderNo, userId);
        return order.getStatus();
    }
    
    /**
     * 获取订单商品列表
     */
    private List<OrderItemRequest> getOrderItems(String userId, OrderCreateRequest request) {
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            // 立即购买
            return request.getItems();
        } else if (request.getCartIds() != null && !request.getCartIds().isEmpty()) {
            // 从购物车下单
            LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Cart::getUserId, userId);
            wrapper.in(Cart::getId, request.getCartIds());
            
            List<Cart> carts = cartMapper.selectList(wrapper);
            return carts.stream().map(cart -> {
                OrderItemRequest item = new OrderItemRequest();
                item.setProductId(cart.getProductId());
                item.setQuantity(cart.getQuantity());
                return item;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * 锁定库存
     */
    private boolean lockInventory(String productId, Integer quantity) {
        for (int i = 0; i < 3; i++) {  // 最多重试3次
            LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Inventory::getProductId, productId);
            Inventory inventory = inventoryMapper.selectOne(wrapper);
            
            if (inventory == null || inventory.getAvailableStock() < quantity) {
                return false;
            }
            
            int rows = inventoryMapper.freezeStock(productId, quantity, inventory.getVersion());
            if (rows > 0) {
                log.info("锁定库存成功: productId={}, quantity={}", productId, quantity);
                return true;
            }
        }
        return false;
    }
    
    /**
     * 释放库存
     */
    private void releaseInventory(String productId, Integer quantity) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getProductId, productId);
        Inventory inventory = inventoryMapper.selectOne(wrapper);
        
        if (inventory != null) {
            inventoryMapper.unfreezeStock(productId, quantity, inventory.getVersion());
            log.info("释放库存成功: productId={}, quantity={}", productId, quantity);
        }
    }
    
    /**
     * 获取订单
     */
    private OrderMain getOrderByNoAndUserId(String orderNo, String userId) {
        LambdaQueryWrapper<OrderMain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderMain::getOrderNo, orderNo);
        wrapper.eq(OrderMain::getUserId, userId);
        
        OrderMain order = orderMainMapper.selectOne(wrapper);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }
    
    /**
     * 转换为VO
     */
    private OrderVO convertToVO(OrderMain order) {
        OrderVO vo = new OrderVO();
        vo.setOrderNo(order.getOrderNo());
        vo.setOrderType(order.getOrderType());
        vo.setStatus(order.getStatus());
        vo.setStatusDesc(getStatusDesc(order.getStatus()));
        vo.setPayableAmount(order.getPayableAmount());
        vo.setCreateTime(order.getCreateTime());
        vo.setPaymentTimeout(order.getPaymentTimeout());
        
        // 查询订单明细
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderNo, order.getOrderNo());
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        
        vo.setItems(items.stream().map(this::convertItemToVO).collect(Collectors.toList()));
        vo.setTotalQuantity(items.stream().mapToInt(OrderItem::getQuantity).sum());
        if (!items.isEmpty()) {
            vo.setMainImage(items.get(0).getProductImage());
        }
        
        return vo;
    }
    
    /**
     * 转换为详情VO
     */
    private OrderDetailVO convertToDetailVO(OrderMain order) {
        OrderDetailVO vo = new OrderDetailVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStatusDesc(getStatusDesc(order.getStatus()));
        vo.setDeliveryMethodDesc("COURIER".equals(order.getDeliveryMethod()) ? "骑手配送" : "到店自提");
        
        // 手机号脱敏
        if (order.getReceiverPhone() != null) {
            vo.setReceiverPhone(order.getReceiverPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }
        
        // 完整地址
        vo.setReceiverAddress(order.getReceiverProvince() + order.getReceiverCity() + 
                order.getReceiverDistrict() + order.getReceiverAddress());
        
        // 查询订单明细
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderNo, order.getOrderNo());
        List<OrderItem> items = orderItemMapper.selectList(wrapper);
        
        vo.setItems(items.stream().map(this::convertItemToVO).collect(Collectors.toList()));
        
        return vo;
    }
    
    /**
     * 转换订单明细为VO
     */
    private OrderItemVO convertItemToVO(OrderItem item) {
        OrderItemVO vo = new OrderItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }
    
    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        switch (status) {
            case "PAYING":
                return "待支付";
            case "PAID":
                return "待发货";
            case "SHIPPING":
                return "配送中";
            case "COMPLETED":
                return "已完成";
            case "CANCELLED":
                return "已取消";
            case "AFTER_SALE":
                return "售后中";
            default:
                return status;
        }
    }
}

