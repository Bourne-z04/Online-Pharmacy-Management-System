package com.pharmacy.online.controller;

import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.AddressRequest;
import com.pharmacy.online.dto.AddressVO;
import com.pharmacy.online.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 收货地址控制器
 */
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {
    
    @Autowired
    private AddressService addressService;
    
    /**
     * 获取地址列表
     */
    @GetMapping("/list")
    public Result<List<AddressVO>> getAddressList(@RequestAttribute("userId") String userId) {
        log.info("获取地址列表: userId={}", userId);
        List<AddressVO> addressList = addressService.getAddressList(userId);
        return Result.success(addressList);
    }
    
    /**
     * 获取地址详情
     */
    @GetMapping("/{id}")
    public Result<AddressVO> getAddressById(@RequestAttribute("userId") String userId,
                                             @PathVariable Long id) {
        log.info("获取地址详情: userId={}, id={}", userId, id);
        AddressVO address = addressService.getAddressById(userId, id);
        return Result.success(address);
    }
    
    /**
     * 添加地址
     */
    @PostMapping
    public Result<Long> addAddress(@RequestAttribute("userId") String userId,
                                    @Valid @RequestBody AddressRequest request) {
        log.info("添加地址: userId={}, request={}", userId, request);
        Long addressId = addressService.addAddress(userId, request);
        return Result.success("添加成功", addressId);
    }
    
    /**
     * 更新地址
     */
    @PutMapping("/{id}")
    public Result<Void> updateAddress(@RequestAttribute("userId") String userId,
                                       @PathVariable Long id,
                                       @Valid @RequestBody AddressRequest request) {
        log.info("更新地址: userId={}, id={}, request={}", userId, id, request);
        addressService.updateAddress(userId, id, request);
        return Result.success("更新成功");
    }
    
    /**
     * 删除地址
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAddress(@RequestAttribute("userId") String userId,
                                       @PathVariable Long id) {
        log.info("删除地址: userId={}, id={}", userId, id);
        addressService.deleteAddress(userId, id);
        return Result.success("删除成功");
    }
    
    /**
     * 设置默认地址
     */
    @PutMapping("/{id}/default")
    public Result<Void> setDefaultAddress(@RequestAttribute("userId") String userId,
                                           @PathVariable Long id) {
        log.info("设置默认地址: userId={}, id={}", userId, id);
        addressService.setDefaultAddress(userId, id);
        return Result.success("设置成功");
    }
    
    /**
     * 获取默认地址
     */
    @GetMapping("/default")
    public Result<AddressVO> getDefaultAddress(@RequestAttribute("userId") String userId) {
        log.info("获取默认地址: userId={}", userId);
        AddressVO address = addressService.getDefaultAddress(userId);
        return Result.success(address);
    }
}

