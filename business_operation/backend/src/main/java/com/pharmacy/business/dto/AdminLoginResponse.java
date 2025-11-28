package com.pharmacy.business.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理员登录响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResponse {
    
    /**
     * 管理员ID
     */
    private Long adminId;
    
    /**
     * 账号
     */
    private String username;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 角色
     */
    private String role;
    
    /**
     * JWT Token
     */
    private String token;
}

