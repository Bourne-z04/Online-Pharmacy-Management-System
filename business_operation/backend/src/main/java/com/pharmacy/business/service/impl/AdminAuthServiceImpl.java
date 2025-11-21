package com.pharmacy.business.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pharmacy.business.dto.AdminLoginRequest;
import com.pharmacy.business.dto.AdminLoginResponse;
import com.pharmacy.business.dto.PasswordChangeRequest;
import com.pharmacy.business.entity.AdminUser;
import com.pharmacy.business.exception.BusinessException;
import com.pharmacy.business.mapper.AdminUserMapper;
import com.pharmacy.business.service.AdminAuthService;
import com.pharmacy.business.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * B端认证服务实现类
 */
@Slf4j
@Service
public class AdminAuthServiceImpl implements AdminAuthService {
    
    @Autowired
    private AdminUserMapper adminUserMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AdminLoginResponse login(AdminLoginRequest request, String ip) {
        // 1. 查询用户
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUser::getUsername, request.getUsername());
        AdminUser admin = adminUserMapper.selectOne(wrapper);
        
        if (admin == null) {
            throw new BusinessException("账号或密码错误");
        }
        
        // 2. 验证密码
        if (!BCrypt.checkpw(request.getPassword(), admin.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        
        // 3. 检查账号状态
        if (admin.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 4. 更新登录信息
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setLastLoginIp(ip);
        adminUserMapper.updateById(admin);
        
        // 5. 生成Token
        String token = jwtUtil.generateToken(admin.getId().toString());
        
        // 6. 构造响应
        AdminLoginResponse response = new AdminLoginResponse();
        response.setAdminId(admin.getId());
        response.setUsername(admin.getUsername());
        response.setRealName(admin.getRealName());
        response.setRole(admin.getRole());
        response.setToken(token);
        
        log.info("管理员登录成功: username={}, ip={}", request.getUsername(), ip);
        return response;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long adminId, PasswordChangeRequest request) {
        // 1. 查询管理员
        AdminUser admin = adminUserMapper.selectById(adminId);
        if (admin == null) {
            throw new BusinessException("管理员不存在");
        }
        
        // 2. 验证原密码
        if (!BCrypt.checkpw(request.getOldPassword(), admin.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        
        // 3. 加密新密码
        String encodedPassword = BCrypt.hashpw(request.getNewPassword());
        
        // 4. 更新密码
        admin.setPassword(encodedPassword);
        adminUserMapper.updateById(admin);
        
        log.info("修改密码成功: adminId={}", adminId);
    }
}

