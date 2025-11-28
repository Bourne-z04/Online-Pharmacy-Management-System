package com.pharmacy.online.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pharmacy.online.entity.ConsultationSession;
import org.apache.ibatis.annotations.Mapper;

/**
 * 问诊会话Mapper接口
 */
@Mapper
public interface ConsultationSessionMapper extends BaseMapper<ConsultationSession> {
}

