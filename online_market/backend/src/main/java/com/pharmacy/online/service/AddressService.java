package com.pharmacy.online.service;

import com.pharmacy.online.dto.AddressRequest;
import com.pharmacy.online.dto.AddressVO;

import java.util.List;

/**
 * 地址服务接口
 */
public interface AddressService {
    
    /**
     * 获取地址列表
     */
    List<AddressVO> getAddressList(String userId);
    
    /**
     * 获取地址详情
     */
    AddressVO getAddressById(String userId, Long id);
    
    /**
     * 添加地址
     */
    Long addAddress(String userId, AddressRequest request);
    
    /**
     * 更新地址
     */
    void updateAddress(String userId, Long id, AddressRequest request);
    
    /**
     * 删除地址
     */
    void deleteAddress(String userId, Long id);
    
    /**
     * 设置默认地址
     */
    void setDefaultAddress(String userId, Long id);
    
    /**
     * 获取默认地址
     */
    AddressVO getDefaultAddress(String userId);
}

