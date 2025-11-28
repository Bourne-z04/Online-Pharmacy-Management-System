package com.pharmacy.online.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pharmacy.online.dto.UserInfoDTO;
import com.pharmacy.online.dto.UserUpdateRequest;
import com.pharmacy.online.entity.User;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.UserMapper;
import com.pharmacy.online.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Value("${file.upload.path:upload/avatar/}")
    private String uploadPath;
    
    @Override
    public UserInfoDTO getUserInfo(String userId) {
        User user = getUserByUserId(userId);
        
        UserInfoDTO dto = new UserInfoDTO();
        BeanUtils.copyProperties(user, dto);
        
        // 手机号脱敏
        if (user.getPhone() != null) {
            dto.setPhone(user.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        }
        
        return dto;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(String userId, UserUpdateRequest request) {
        User user = getUserByUserId(userId);
        
        // 验证昵称
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        
        // 验证性别
        if (request.getGender() != null) {
            if (request.getGender() < 0 || request.getGender() > 2) {
                throw new BusinessException("性别参数错误");
            }
            user.setGender(request.getGender());
        }
        
        // 验证生日
        if (request.getBirthday() != null) {
            LocalDate minDate = LocalDate.of(1900, 1, 1);
            LocalDate maxDate = LocalDate.now();
            if (request.getBirthday().isBefore(minDate) || request.getBirthday().isAfter(maxDate)) {
                throw new BusinessException("生日日期超出范围");
            }
            user.setBirthday(request.getBirthday());
        }
        
        // 更新手机号
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        
        // 更新真实姓名
        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        
        userMapper.updateById(user);
        log.info("更新用户信息成功: userId={}", userId);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadAvatar(String userId, MultipartFile file) {
        // 1. 验证文件
        if (file.isEmpty()) {
            throw new BusinessException("请选择要上传的文件");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException("文件名为空");
        }
        
        // 验证文件类型
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        if (!".jpg".equalsIgnoreCase(suffix) && !".jpeg".equalsIgnoreCase(suffix) && 
            !".png".equalsIgnoreCase(suffix)) {
            throw new BusinessException("只支持JPG、PNG格式的图片");
        }
        
        // 验证文件大小（2MB）
        if (file.getSize() > 2 * 1024 * 1024) {
            throw new BusinessException("图片过大，请选择2MB以内的图片");
        }
        
        // 2. 生成文件名
        String filename = IdUtil.simpleUUID() + suffix;
        
        // 3. 创建目录
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 4. 保存文件
        File dest = new File(uploadDir, filename);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败，请重试");
        }
        
        // 5. 更新用户头像
        String avatarUrl = "/upload/avatar/" + filename;
        User user = getUserByUserId(userId);
        user.setAvatar(avatarUrl);
        userMapper.updateById(user);
        
        log.info("上传头像成功: userId={}, avatar={}", userId, avatarUrl);
        return avatarUrl;
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

