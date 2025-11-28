package com.pharmacy.business.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 管理员登录请求DTO
 */
@Data
public class AdminLoginRequest {
    
    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
}

