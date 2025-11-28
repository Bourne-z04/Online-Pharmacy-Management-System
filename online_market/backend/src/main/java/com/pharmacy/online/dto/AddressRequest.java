package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 地址请求DTO
 */
@Data
public class AddressRequest {
    
    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50个字符")
    private String receiverName;
    
    /**
     * 收货人手机号
     */
    @NotBlank(message = "收货人手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;
    
    /**
     * 省份
     */
    @NotBlank(message = "省份不能为空")
    private String province;
    
    /**
     * 城市
     */
    @NotBlank(message = "城市不能为空")
    private String city;
    
    /**
     * 区/县
     */
    @NotBlank(message = "区/县不能为空")
    private String district;
    
    /**
     * 详细地址
     */
    @NotBlank(message = "详细地址不能为空")
    @Size(min = 5, max = 200, message = "详细地址长度应为5-200个字符")
    private String detailAddress;
    
    /**
     * 是否默认地址：0-否，1-是
     */
    private Integer isDefault;
}

