package com.pharmacy.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pharmacy.online.entity.SearchHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * 搜索历史Mapper接口
 */
@Mapper
public interface SearchHistoryMapper extends BaseMapper<SearchHistory> {
}

