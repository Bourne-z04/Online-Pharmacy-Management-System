package com.pharmacy.online.service;

import com.pharmacy.online.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 处方服务接口
 */
public interface PrescriptionService {
    
    /**
     * 提交患者信息
     */
    String submitPatientInfo(String userId, PatientInfoRequest request);
    
    /**
     * 上传复诊凭证
     */
    List<String> uploadEvidence(String userId, String prescriptionId, MultipartFile[] files);
    
    /**
     * 创建问诊会话
     */
    ConsultationSessionVO createConsultation(String userId, ConsultationCreateRequest request);
    
    /**
     * 获取问诊会话信息
     */
    ConsultationSessionVO getConsultation(String userId, String sessionId);
    
    /**
     * 获取处方状态
     */
    PrescriptionStatusVO getPrescriptionStatus(String userId, String prescriptionId);
    
    /**
     * 获取处方详情
     */
    PrescriptionVO getPrescription(String userId, String prescriptionId);
}

