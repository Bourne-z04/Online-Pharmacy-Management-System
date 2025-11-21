package com.pharmacy.business.controller;

import com.pharmacy.business.common.Result;
import com.pharmacy.business.dto.AdminLoginRequest;
import com.pharmacy.business.dto.AdminLoginResponse;
import com.pharmacy.business.dto.PasswordChangeRequest;
import com.pharmacy.business.service.AdminAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * B端认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AdminAuthController {
    
    @Autowired
    private AdminAuthService authService;
    
    /**
     * 账号登录
     */
    @PostMapping("/login")
    public Result<AdminLoginResponse> login(@Valid @RequestBody AdminLoginRequest request,
                                             HttpServletRequest httpRequest) {
        log.info("管理员登录: username={}", request.getUsername());
        String ip = getClientIp(httpRequest);
        AdminLoginResponse response = authService.login(request, ip);
        return Result.success(response);
    }
    
    /**
     * 修改密码
     */
    @PostMapping("/password/change")
    public Result<Void> changePassword(@RequestAttribute("adminId") Long adminId,
                                        @Valid @RequestBody PasswordChangeRequest request) {
        log.info("修改密码: adminId={}", adminId);
        authService.changePassword(adminId, request);
        return Result.success("密码修改成功");
    }
    
    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestAttribute("adminId") Long adminId) {
        log.info("退出登录: adminId={}", adminId);
        return Result.success("退出成功");
    }
    
    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

