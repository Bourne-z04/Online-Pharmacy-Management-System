package com.pharmacy.online.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.dto.AfterSaleCreateRequest;
import com.pharmacy.online.dto.AfterSaleVO;
import com.pharmacy.online.entity.AfterSale;
import com.pharmacy.online.entity.OrderMain;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.AfterSaleMapper;
import com.pharmacy.online.mapper.OrderMainMapper;
import com.pharmacy.online.service.AfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 售后服务实现类
 */
@Slf4j
@Service
public class AfterSaleServiceImpl implements AfterSaleService {
    
    @Autowired
    private AfterSaleMapper afterSaleMapper;
    
    @Autowired
    private OrderMainMapper orderMainMapper;
    
    @Value("${business.after-sale.max-images:3}")
    private Integer maxImages;
    
    @Value("${business.after-sale.max-image-size:5242880}")
    private Long maxImageSize;
    
    @Value("${file.upload.path:upload/after-sale/}")
    private String uploadPath;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAfterSale(String userId, AfterSaleCreateRequest request) {
        // 1. 验证订单
        LambdaQueryWrapper<OrderMain> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(OrderMain::getOrderNo, request.getOrderNo());
        orderWrapper.eq(OrderMain::getUserId, userId);
        OrderMain order = orderMainMapper.selectOne(orderWrapper);
        
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        
        // 2. 验证订单状态（待收货或已完成可以申请售后）
        if (!"SHIPPING".equals(order.getStatus()) && !"COMPLETED".equals(order.getStatus())) {
            throw new BusinessException("当前订单状态不支持售后");
        }
        
        // 3. 检查是否已有售后申请
        LambdaQueryWrapper<AfterSale> checkWrapper = new LambdaQueryWrapper<>();
        checkWrapper.eq(AfterSale::getOrderNo, request.getOrderNo());
        checkWrapper.in(AfterSale::getStatus, "PENDING", "APPROVED");
        Long count = afterSaleMapper.selectCount(checkWrapper);
        if (count > 0) {
            throw new BusinessException("该订单已有进行中的售后申请");
        }
        
        // 4. 验证退款金额
        if (request.getRefundAmount().compareTo(order.getActualAmount()) > 0) {
            throw new BusinessException("退款金额不能超过实付金额");
        }
        
        // 5. 生成售后单号
        String afterSaleNo = "AS" + System.currentTimeMillis();
        
        // 6. 创建售后记录
        AfterSale afterSale = new AfterSale();
        afterSale.setAfterSaleNo(afterSaleNo);
        afterSale.setOrderNo(request.getOrderNo());
        afterSale.setUserId(userId);
        afterSale.setType(request.getType());
        afterSale.setReason(request.getReason());
        afterSale.setRefundAmount(request.getRefundAmount());
        afterSale.setStatus("PENDING");
        
        afterSaleMapper.insert(afterSale);
        
        // 7. 更新订单状态
        order.setStatus("AFTER_SALE");
        orderMainMapper.updateById(order);
        
        log.info("创建售后申请成功: userId={}, orderNo={}, afterSaleNo={}", 
                userId, request.getOrderNo(), afterSaleNo);
        return afterSaleNo;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> uploadEvidence(String userId, String afterSaleNo, MultipartFile[] files) {
        // 1. 验证售后单存在
        AfterSale afterSale = getAfterSaleByNoAndUserId(afterSaleNo, userId);
        
        // 2. 验证文件数量
        if (files == null || files.length == 0) {
            throw new BusinessException("请选择要上传的文件");
        }
        if (files.length > maxImages) {
            throw new BusinessException("最多只能上传" + maxImages + "张图片");
        }
        
        // 3. 上传文件
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            // 验证文件大小
            if (file.getSize() > maxImageSize) {
                throw new BusinessException("单张图片大小不能超过5MB");
            }
            
            // 验证文件格式
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                throw new BusinessException("文件名为空");
            }
            
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!".jpg".equalsIgnoreCase(suffix) && !".jpeg".equalsIgnoreCase(suffix) && 
                !".png".equalsIgnoreCase(suffix)) {
                throw new BusinessException("只支持JPG、PNG格式的图片");
            }
            
            // 保存文件
            String filename = IdUtil.simpleUUID() + suffix;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File dest = new File(uploadDir, filename);
            try {
                file.transferTo(dest);
                String url = "/upload/after-sale/" + filename;
                urls.add(url);
            } catch (IOException e) {
                log.error("文件上传失败", e);
                throw new BusinessException("文件上传失败，请重试");
            }
        }
        
        // 4. 更新售后记录
        afterSale.setEvidenceImages(JSON.toJSONString(urls));
        afterSaleMapper.updateById(afterSale);
        
        log.info("上传售后凭证成功: afterSaleNo={}, count={}", afterSaleNo, urls.size());
        return urls;
    }
    
    @Override
    public PageResult<AfterSaleVO> getAfterSaleList(String userId, Integer page, Integer size) {
        Page<AfterSale> pageParam = new Page<>(page, size);
        
        LambdaQueryWrapper<AfterSale> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AfterSale::getUserId, userId);
        wrapper.orderByDesc(AfterSale::getCreateTime);
        
        Page<AfterSale> result = afterSaleMapper.selectPage(pageParam, wrapper);
        
        List<AfterSaleVO> voList = result.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        
        return new PageResult<>(result.getTotal(), voList, result.getCurrent(), result.getSize());
    }
    
    @Override
    public AfterSaleVO getAfterSaleDetail(String userId, String afterSaleNo) {
        AfterSale afterSale = getAfterSaleByNoAndUserId(afterSaleNo, userId);
        return convertToVO(afterSale);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelAfterSale(String userId, String afterSaleNo) {
        AfterSale afterSale = getAfterSaleByNoAndUserId(afterSaleNo, userId);
        
        if (!"PENDING".equals(afterSale.getStatus())) {
            throw new BusinessException("当前状态不支持取消");
        }
        
        // 删除售后记录
        afterSaleMapper.deleteById(afterSale.getId());
        
        // 恢复订单状态
        LambdaQueryWrapper<OrderMain> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderMain::getOrderNo, afterSale.getOrderNo());
        OrderMain order = orderMainMapper.selectOne(wrapper);
        if (order != null) {
            order.setStatus("COMPLETED");
            orderMainMapper.updateById(order);
        }
        
        log.info("取消售后申请成功: userId={}, afterSaleNo={}", userId, afterSaleNo);
    }
    
    /**
     * 根据售后单号和用户ID获取售后记录
     */
    private AfterSale getAfterSaleByNoAndUserId(String afterSaleNo, String userId) {
        LambdaQueryWrapper<AfterSale> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AfterSale::getAfterSaleNo, afterSaleNo);
        wrapper.eq(AfterSale::getUserId, userId);
        
        AfterSale afterSale = afterSaleMapper.selectOne(wrapper);
        if (afterSale == null) {
            throw new BusinessException("售后记录不存在");
        }
        return afterSale;
    }
    
    /**
     * 转换为VO
     */
    private AfterSaleVO convertToVO(AfterSale afterSale) {
        AfterSaleVO vo = new AfterSaleVO();
        BeanUtils.copyProperties(afterSale, vo);
        vo.setTypeDesc("REFUND".equals(afterSale.getType()) ? "退款" : "退货退款");
        vo.setStatusDesc(getStatusDesc(afterSale.getStatus()));
        
        // 解析凭证图片
        if (afterSale.getEvidenceImages() != null && !afterSale.getEvidenceImages().isEmpty()) {
            vo.setEvidenceImages(JSON.parseArray(afterSale.getEvidenceImages(), String.class));
        }
        
        return vo;
    }
    
    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        switch (status) {
            case "PENDING":
                return "待审核";
            case "APPROVED":
                return "已同意";
            case "REJECTED":
                return "已拒绝";
            case "REFUNDED":
                return "已退款";
            default:
                return status;
        }
    }
}

