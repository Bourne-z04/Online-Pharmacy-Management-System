package com.pharmacy.online.service;

import com.pharmacy.online.common.PageResult;
import com.pharmacy.online.dto.AfterSaleCreateRequest;
import com.pharmacy.online.dto.AfterSaleVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 售后服务接口
 */
public interface AfterSaleService {
    
    /**
     * 创建售后申请
     */
    String createAfterSale(String userId, AfterSaleCreateRequest request);
    
    /**
     * 上传售后凭证
     */
    List<String> uploadEvidence(String userId, String afterSaleNo, MultipartFile[] files);
    
    /**
     * 获取售后列表
     */
    PageResult<AfterSaleVO> getAfterSaleList(String userId, Integer page, Integer size);
    
    /**
     * 获取售后详情
     */
    AfterSaleVO getAfterSaleDetail(String userId, String afterSaleNo);
    
    /**
     * 取消售后申请
     */
    void cancelAfterSale(String userId, String afterSaleNo);
}

