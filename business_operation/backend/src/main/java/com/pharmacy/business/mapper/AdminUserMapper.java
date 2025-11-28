package com.pharmacy.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pharmacy.business.entity.AdminUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员Mapper接口
 */
@Mapper
public interface AdminUserMapper extends BaseMapper<AdminUser> {
}

