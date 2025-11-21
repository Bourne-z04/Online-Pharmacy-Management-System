package com.pharmacy.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pharmacy.online.dto.AddressRequest;
import com.pharmacy.online.dto.AddressVO;
import com.pharmacy.online.entity.UserAddress;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.UserAddressMapper;
import com.pharmacy.online.service.AddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 地址服务实现类
 */
@Slf4j
@Service
public class AddressServiceImpl implements AddressService {
    
    @Autowired
    private UserAddressMapper addressMapper;
    
    @Value("${business.address.max-count:20}")
    private Integer maxAddressCount;
    
    @Override
    public List<AddressVO> getAddressList(String userId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getUserId, userId);
        wrapper.orderByDesc(UserAddress::getIsDefault);
        wrapper.orderByDesc(UserAddress::getCreateTime);
        
        List<UserAddress> addresses = addressMapper.selectList(wrapper);
        
        return addresses.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public AddressVO getAddressById(String userId, Long id) {
        UserAddress address = getAddressByIdAndUserId(id, userId);
        return convertToVO(address);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addAddress(String userId, AddressRequest request) {
        // 1. 检查地址数量上限
        LambdaQueryWrapper<UserAddress> countWrapper = new LambdaQueryWrapper<>();
        countWrapper.eq(UserAddress::getUserId, userId);
        Long count = addressMapper.selectCount(countWrapper);
        
        if (count >= maxAddressCount) {
            throw new BusinessException("地址数量已达上限，请删除无用地址后再添加");
        }
        
        // 2. 如果设置为默认地址，取消其他默认地址
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }
        
        // 3. 创建地址
        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(request, address);
        address.setUserId(userId);
        
        // 如果是第一个地址，自动设为默认
        if (count == 0) {
            address.setIsDefault(1);
        }
        
        addressMapper.insert(address);
        
        log.info("添加地址成功: userId={}, addressId={}", userId, address.getId());
        return address.getId();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddress(String userId, Long id, AddressRequest request) {
        // 1. 获取地址
        UserAddress address = getAddressByIdAndUserId(id, userId);
        
        // 2. 如果设置为默认地址，取消其他默认地址
        if (request.getIsDefault() != null && request.getIsDefault() == 1) {
            clearDefaultAddress(userId);
        }
        
        // 3. 更新地址
        BeanUtils.copyProperties(request, address);
        addressMapper.updateById(address);
        
        log.info("更新地址成功: userId={}, addressId={}", userId, id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(String userId, Long id) {
        UserAddress address = getAddressByIdAndUserId(id, userId);
        
        addressMapper.deleteById(id);
        
        // 如果删除的是默认地址，自动设置第一个地址为默认
        if (address.getIsDefault() == 1) {
            LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserAddress::getUserId, userId);
            wrapper.orderByDesc(UserAddress::getCreateTime);
            wrapper.last("LIMIT 1");
            
            UserAddress firstAddress = addressMapper.selectOne(wrapper);
            if (firstAddress != null) {
                firstAddress.setIsDefault(1);
                addressMapper.updateById(firstAddress);
            }
        }
        
        log.info("删除地址成功: userId={}, addressId={}", userId, id);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(String userId, Long id) {
        // 1. 验证地址存在
        getAddressByIdAndUserId(id, userId);
        
        // 2. 取消其他默认地址
        clearDefaultAddress(userId);
        
        // 3. 设置为默认地址
        UserAddress address = new UserAddress();
        address.setId(id);
        address.setIsDefault(1);
        addressMapper.updateById(address);
        
        log.info("设置默认地址成功: userId={}, addressId={}", userId, id);
    }
    
    @Override
    public AddressVO getDefaultAddress(String userId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getUserId, userId);
        wrapper.eq(UserAddress::getIsDefault, 1);
        
        UserAddress address = addressMapper.selectOne(wrapper);
        
        // 如果没有默认地址，返回最新的一个
        if (address == null) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserAddress::getUserId, userId);
            wrapper.orderByDesc(UserAddress::getCreateTime);
            wrapper.last("LIMIT 1");
            address = addressMapper.selectOne(wrapper);
        }
        
        return address != null ? convertToVO(address) : null;
    }
    
    /**
     * 取消所有默认地址
     */
    private void clearDefaultAddress(String userId) {
        LambdaUpdateWrapper<UserAddress> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAddress::getUserId, userId);
        wrapper.eq(UserAddress::getIsDefault, 1);
        wrapper.set(UserAddress::getIsDefault, 0);
        
        addressMapper.update(null, wrapper);
    }
    
    /**
     * 根据ID和用户ID获取地址
     */
    private UserAddress getAddressByIdAndUserId(Long id, String userId) {
        LambdaQueryWrapper<UserAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserAddress::getId, id);
        wrapper.eq(UserAddress::getUserId, userId);
        
        UserAddress address = addressMapper.selectOne(wrapper);
        if (address == null) {
            throw new BusinessException("地址不存在");
        }
        return address;
    }
    
    /**
     * 转换为VO
     */
    private AddressVO convertToVO(UserAddress address) {
        AddressVO vo = new AddressVO();
        BeanUtils.copyProperties(address, vo);
        
        // 手机号脱敏（前3后4）
        vo.setReceiverPhone(address.getReceiverPhone()
                .replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        
        // 完整地址
        vo.setFullAddress(address.getProvince() + address.getCity() + 
                address.getDistrict() + address.getDetailAddress());
        
        return vo;
    }
}

