package com.pharmacy.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pharmacy.online.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址Mapper接口
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
}

