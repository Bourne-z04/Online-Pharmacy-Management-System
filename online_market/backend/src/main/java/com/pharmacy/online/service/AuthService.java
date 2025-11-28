package com.pharmacy.online.service;

import com.pharmacy.online.dto.WechatLoginRequest;
import com.pharmacy.online.dto.WechatLoginResponse;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 微信快捷登录
     */
    WechatLoginResponse wechatLogin(WechatLoginRequest request);
    
    /**
     * 确认隐私协议
     */
    void acceptAgreement(String userId);
    
    /**
     * 获取隐私协议内容
     */
    String getAgreementContent();
}

