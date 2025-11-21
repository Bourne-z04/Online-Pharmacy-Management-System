package com.pharmacy.online.service.impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pharmacy.online.dto.WechatLoginRequest;
import com.pharmacy.online.dto.WechatLoginResponse;
import com.pharmacy.online.entity.User;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.UserMapper;
import com.pharmacy.online.service.AuthService;
import com.pharmacy.online.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired(required = false)
    private WxMaService wxMaService;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WechatLoginResponse wechatLogin(WechatLoginRequest request) {
        try {
            // 调用微信接口获取OpenID
            String openId;
            if (wxMaService != null) {
                WxMaJscode2SessionResult session = wxMaService.getUserService()
                        .getSessionInfo(request.getCode());
                openId = session.getOpenid();
            } else {
                // 开发环境模拟OpenID
                openId = "test_openid_" + System.currentTimeMillis();
                log.warn("未配置微信服务，使用模拟OpenID: {}", openId);
            }
            
            // 查询用户是否存在
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getOpenId, openId);
            User user = userMapper.selectOne(wrapper);
            
            boolean isFirstLogin = false;
            
            // 首次登录，创建用户
            if (user == null) {
                user = new User();
                user.setUserId("U" + IdUtil.getSnowflakeNextIdStr());
                user.setOpenId(openId);
                user.setNickname(request.getNickname());
                user.setAvatar(request.getAvatar());
                user.setGender(request.getGender());
                user.setAgreementAccepted(0);
                user.setStatus(1);
                userMapper.insert(user);
                isFirstLogin = true;
                log.info("新用户注册成功: userId={}, openId={}", user.getUserId(), openId);
            } else {
                // 更新用户信息
                if (request.getNickname() != null) {
                    user.setNickname(request.getNickname());
                }
                if (request.getAvatar() != null) {
                    user.setAvatar(request.getAvatar());
                }
                if (request.getGender() != null) {
                    user.setGender(request.getGender());
                }
                userMapper.updateById(user);
                log.info("用户登录成功: userId={}", user.getUserId());
            }
            
            // 生成Token
            String token = jwtUtil.generateToken(user.getUserId());
            
            // 构造响应
            WechatLoginResponse response = new WechatLoginResponse();
            response.setUserId(user.getUserId());
            response.setToken(token);
            response.setIsFirstLogin(isFirstLogin);
            response.setAgreementAccepted(user.getAgreementAccepted() == 1);
            response.setNickname(user.getNickname());
            response.setAvatar(user.getAvatar());
            
            return response;
            
        } catch (Exception e) {
            log.error("微信登录失败", e);
            throw new BusinessException("登录失败，请稍后重试");
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptAgreement(String userId) {
        User user = getUserByUserId(userId);
        if (user.getAgreementAccepted() == 1) {
            return;
        }
        
        user.setAgreementAccepted(1);
        user.setAgreementTime(LocalDateTime.now());
        userMapper.updateById(user);
        
        log.info("用户确认隐私协议: userId={}", userId);
    }
    
    @Override
    public String getAgreementContent() {
        // 返回隐私协议内容（实际应从配置或数据库读取）
        return "《百惠康大药房用户隐私保护协议》\n\n" +
               "欢迎使用百惠康大药房在线商城服务...\n" +
               "（此处应包含完整的隐私协议内容）";
    }
    
    /**
     * 根据用户ID获取用户
     */
    private User getUserByUserId(String userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, userId);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return user;
    }
}

