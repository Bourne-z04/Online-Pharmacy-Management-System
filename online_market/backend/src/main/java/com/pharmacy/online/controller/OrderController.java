package com.pharmacy.online.controller;

import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.*;
import com.pharmacy.online.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<OrderCreateResultVO> createOrder(@RequestAttribute("userId") String userId,
                                                    @Valid @RequestBody OrderCreateRequest request) {
        log.info("创建订单: userId={}, request={}", userId, request);
        OrderCreateResultVO result = orderService.createOrder(userId, request);
        return Result.success("订单创建成功", result);
    }
    
    /**
     * 订单列表
     */
    @GetMapping("/list")
    public Result<PageResult<OrderVO>> getOrderList(@RequestAttribute("userId") String userId,
                                                     @RequestParam(required = false) String status,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取订单列表: userId={}, status={}, page={}", userId, status, page);
        PageResult<OrderVO> result = orderService.getOrderList(userId, status, page, size);
        return Result.success(result);
    }
    
    /**
     * 订单详情
     */
    @GetMapping("/{orderNo}")
    public Result<OrderDetailVO> getOrderDetail(@RequestAttribute("userId") String userId,
                                                 @PathVariable String orderNo) {
        log.info("获取订单详情: userId={}, orderNo={}", userId, orderNo);
        OrderDetailVO detail = orderService.getOrderDetail(userId, orderNo);
        return Result.success(detail);
    }
    
    /**
     * 取消订单
     */
    @PutMapping("/{orderNo}/cancel")
    public Result<Void> cancelOrder(@RequestAttribute("userId") String userId,
                                     @PathVariable String orderNo,
                                     @RequestBody(required = false) OrderCancelRequest request) {
        log.info("取消订单: userId={}, orderNo={}", userId, orderNo);
        orderService.cancelOrder(userId, orderNo, request != null ? request.getReason() : null);
        return Result.success("订单取消成功");
    }
    
    /**
     * 确认收货
     */
    @PutMapping("/{orderNo}/confirm")
    public Result<Void> confirmOrder(@RequestAttribute("userId") String userId,
                                      @PathVariable String orderNo) {
        log.info("确认收货: userId={}, orderNo={}", userId, orderNo);
        orderService.confirmOrder(userId, orderNo);
        return Result.success("确认收货成功");
    }
    
    /**
     * 发起支付
     */
    @PostMapping("/{orderNo}/pay")
    public Result<PaymentVO> createPayment(@RequestAttribute("userId") String userId,
                                            @PathVariable String orderNo) {
        log.info("发起支付: userId={}, orderNo={}", userId, orderNo);
        PaymentVO payment = orderService.createPayment(userId, orderNo);
        return Result.success(payment);
    }
    
    /**
     * 查询支付状态
     */
    @GetMapping("/{orderNo}/payment/status")
    public Result<String> getPaymentStatus(@RequestAttribute("userId") String userId,
                                            @PathVariable String orderNo) {
        log.info("查询支付状态: userId={}, orderNo={}", userId, orderNo);
        String status = orderService.getPaymentStatus(userId, orderNo);
        return Result.success(status);
    }
}

