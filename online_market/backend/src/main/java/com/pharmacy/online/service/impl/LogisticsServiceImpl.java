package com.pharmacy.online.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pharmacy.online.dto.LogisticsTrackVO;
import com.pharmacy.online.dto.LogisticsVO;
import com.pharmacy.online.entity.Logistics;
import com.pharmacy.online.entity.OrderMain;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.LogisticsMapper;
import com.pharmacy.online.mapper.OrderMainMapper;
import com.pharmacy.online.service.LogisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 物流服务实现类
 */
@Slf4j
@Service
public class LogisticsServiceImpl implements LogisticsService {
    
    @Autowired
    private LogisticsMapper logisticsMapper;
    
    @Autowired
    private OrderMainMapper orderMainMapper;
    
    @Override
    public LogisticsVO getLogistics(String userId, String orderNo) {
        // 1. 验证订单
        LambdaQueryWrapper<OrderMain> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(OrderMain::getOrderNo, orderNo);
        orderWrapper.eq(OrderMain::getUserId, userId);
        OrderMain order = orderMainMapper.selectOne(orderWrapper);
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 2. 查询物流信息
        LambdaQueryWrapper<Logistics> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Logistics::getOrderNo, orderNo);
        Logistics logistics = logisticsMapper.selectOne(wrapper);
        
        if (logistics == null) {
            throw new BusinessException("物流信息不存在");
        }
        
        // 3. 转换VO
        LogisticsVO vo = new LogisticsVO();
        BeanUtils.copyProperties(logistics, vo);
        vo.setStatusDesc(getStatusDesc(logistics.getStatus()));
        
        // 4. 获取物流轨迹（模拟数据）
        List<LogisticsTrackVO> tracks = new ArrayList<>();
        
        LogisticsTrackVO track1 = new LogisticsTrackVO();
        track1.setStatus("ACCEPTED");
        track1.setDescription("骑手已接单");
        track1.setCreateTime(LocalDateTime.now().minusHours(2));
        tracks.add(track1);
        
        LogisticsTrackVO track2 = new LogisticsTrackVO();
        track2.setStatus("PICKED");
        track2.setDescription("骑手已取货");
        track2.setCreateTime(LocalDateTime.now().minusHours(1));
        tracks.add(track2);
        
        LogisticsTrackVO track3 = new LogisticsTrackVO();
        track3.setStatus("DELIVERING");
        track3.setDescription("骑手配送中");
        track3.setCreateTime(LocalDateTime.now().minusMinutes(30));
        tracks.add(track3);
        
        vo.setTracks(tracks);
        
        return vo;
    }
    
    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        switch (status) {
            case "ACCEPTED":
                return "已接单";
            case "PICKED":
                return "已取货";
            case "DELIVERING":
                return "配送中";
            case "DELIVERED":
                return "已送达";
            default:
                return status;
        }
    }
}

