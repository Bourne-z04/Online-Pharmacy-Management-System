package com.pharmacy.online.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WechatLoginResponse {
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * JWT Token
     */
    private String token;
    
    /**
     * 是否首次登录
     */
    private Boolean isFirstLogin;
    
    /**
     * 是否已同意隐私协议
     */
    private Boolean agreementAccepted;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户头像
     */
    private String avatar;
}

