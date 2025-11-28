package com.pharmacy.online.controller;

import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.WechatLoginRequest;
import com.pharmacy.online.dto.WechatLoginResponse;
import com.pharmacy.online.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * 微信快捷登录
     */
    @PostMapping("/wechat/login")
    public Result<WechatLoginResponse> wechatLogin(@Valid @RequestBody WechatLoginRequest request) {
        log.info("微信登录请求: code={}", request.getCode());
        WechatLoginResponse response = authService.wechatLogin(request);
        return Result.success(response);
    }
    
    /**
     * 确认隐私协议
     */
    @PostMapping("/agreement/accept")
    public Result<Void> acceptAgreement(@RequestAttribute("userId") String userId) {
        log.info("确认隐私协议: userId={}", userId);
        authService.acceptAgreement(userId);
        return Result.success();
    }
    
    /**
     * 获取隐私协议内容
     */
    @GetMapping("/agreement/content")
    public Result<String> getAgreementContent() {
        String content = authService.getAgreementContent();
        return Result.success(content);
    }
}

