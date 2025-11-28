package com.pharmacy.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pharmacy.online.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品Mapper接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}

