package com.pharmacy.business.service;

import com.pharmacy.business.dto.AdminLoginRequest;
import com.pharmacy.business.dto.AdminLoginResponse;
import com.pharmacy.business.dto.PasswordChangeRequest;

/**
 * B端认证服务接口
 */
public interface AdminAuthService {
    
    /**
     * 登录
     */
    AdminLoginResponse login(AdminLoginRequest request, String ip);
    
    /**
     * 修改密码
     */
    void changePassword(Long adminId, PasswordChangeRequest request);
}

