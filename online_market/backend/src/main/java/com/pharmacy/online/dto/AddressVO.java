package com.pharmacy.online.dto;

import lombok.Data;

/**
 * 地址视图对象
 */
@Data
public class AddressVO {
    
    /**
     * 地址ID
     */
    private Long id;
    
    /**
     * 收货人姓名
     */
    private String receiverName;
    
    /**
     * 收货人手机号（脱敏）
     */
    private String receiverPhone;
    
    /**
     * 省份
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 区/县
     */
    private String district;
    
    /**
     * 详细地址
     */
    private String detailAddress;
    
    /**
     * 完整地址
     */
    private String fullAddress;
    
    /**
     * 是否默认地址：0-否，1-是
     */
    private Integer isDefault;
}

