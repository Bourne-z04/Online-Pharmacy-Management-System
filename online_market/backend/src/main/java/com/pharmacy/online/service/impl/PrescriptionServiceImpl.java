package com.pharmacy.online.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pharmacy.online.dto.*;
import com.pharmacy.online.entity.ConsultationSession;
import com.pharmacy.online.entity.Prescription;
import com.pharmacy.online.exception.BusinessException;
import com.pharmacy.online.mapper.ConsultationSessionMapper;
import com.pharmacy.online.mapper.PrescriptionMapper;
import com.pharmacy.online.service.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处方服务实现类
 */
@Slf4j
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
    
    @Autowired
    private PrescriptionMapper prescriptionMapper;
    
    @Autowired
    private ConsultationSessionMapper sessionMapper;
    
    @Value("${business.prescription.max-evidence-images:5}")
    private Integer maxEvidenceImages;
    
    @Value("${business.prescription.max-image-size:5242880}")
    private Long maxImageSize;
    
    @Value("${business.prescription.consultation-timeout:600}")
    private Integer consultationTimeout;
    
    @Value("${file.upload.path:upload/prescription/}")
    private String uploadPath;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submitPatientInfo(String userId, PatientInfoRequest request) {
        // 1. 生成处方ID
        String prescriptionId = "RX" + System.currentTimeMillis();
        
        // 2. 创建处方记录
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(prescriptionId);
        prescription.setUserId(userId);
        prescription.setPatientName(request.getPatientName());
<<<<<<< HEAD
        prescription.setPatientIdCard(encryptIdCard(request.getPatientIdCard())); // 加密身份证号
=======
        prescription.setPatientIdCard(encryptIdCard(request.getIdCard())); // 加密身份证号
>>>>>>> ab7ae8244a59dd8aaa3960962d462c545b191874
        prescription.setGender(request.getGender());
        prescription.setAge(request.getAge());
        prescription.setWeight(request.getWeight());
        prescription.setDiseaseDesc(request.getDiseaseDesc());
        prescription.setStatus("PENDING");
        prescription.setAgreementAccepted(request.getAgreementAccepted());
        
        prescriptionMapper.insert(prescription);
        
        log.info("提交患者信息成功: userId={}, prescriptionId={}", userId, prescriptionId);
        return prescriptionId;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> uploadEvidence(String userId, String prescriptionId, MultipartFile[] files) {
        // 1. 验证处方存在
        Prescription prescription = getPrescriptionByIdAndUserId(prescriptionId, userId);
        
        // 2. 验证文件数量
        if (files == null || files.length == 0) {
            throw new BusinessException("请选择要上传的文件");
        }
        if (files.length > maxEvidenceImages) {
            throw new BusinessException("最多只能上传" + maxEvidenceImages + "张图片");
        }
        
        // 3. 验证文件并上传
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
                String url = "/upload/prescription/" + filename;
                urls.add(url);
            } catch (IOException e) {
                log.error("文件上传失败", e);
                throw new BusinessException("文件上传失败，请重试");
            }
        }
        
        // 4. 更新处方记录
        prescription.setEvidenceImages(JSON.toJSONString(urls));
        prescriptionMapper.updateById(prescription);
        
        log.info("上传复诊凭证成功: prescriptionId={}, count={}", prescriptionId, urls.size());
        return urls;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ConsultationSessionVO createConsultation(String userId, ConsultationCreateRequest request) {
        // 1. 验证处方存在
        Prescription prescription = getPrescriptionByIdAndUserId(request.getPrescriptionId(), userId);
        
        if (prescription.getEvidenceImages() == null || prescription.getEvidenceImages().isEmpty()) {
            throw new BusinessException("请先上传复诊凭证");
        }
        
        // 2. 匹配医生（简化处理，实际应调用医生匹配算法）
        String doctorId = matchDoctor(request.getDepartment());
        String doctorName = "李医生"; // 模拟医生信息
        
        // 3. 创建问诊会话
        String sessionId = "CS" + System.currentTimeMillis();
        
        ConsultationSession session = new ConsultationSession();
        session.setSessionId(sessionId);
        session.setUserId(userId);
        session.setDoctorId(doctorId);
        session.setDepartment(request.getDepartment());
        session.setStatus("WAITING");
        session.setQueueNumber(1); // 简化处理
        session.setEstimatedWaitTime(3); // 预计3分钟
        
        sessionMapper.insert(session);
        
        // 4. 更新处方记录
        prescription.setSessionId(sessionId);
        prescription.setDoctorId(doctorId);
        prescription.setDoctorName(doctorName);
        prescriptionMapper.updateById(prescription);
        
        // 5. 构造返回结果
        ConsultationSessionVO vo = new ConsultationSessionVO();
        vo.setSessionId(sessionId);
        vo.setDoctorId(doctorId);
        vo.setDoctorName(doctorName);
        vo.setDepartment(request.getDepartment());
        vo.setStatus("WAITING");
        vo.setStatusDesc("等待医生接诊");
        vo.setQueueNumber(1);
        vo.setEstimatedWaitTime(3);
        
        log.info("创建问诊会话成功: userId={}, sessionId={}, doctorId={}", 
                userId, sessionId, doctorId);
        return vo;
    }
    
    @Override
    public ConsultationSessionVO getConsultation(String userId, String sessionId) {
        LambdaQueryWrapper<ConsultationSession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ConsultationSession::getSessionId, sessionId);
        wrapper.eq(ConsultationSession::getUserId, userId);
        
        ConsultationSession session = sessionMapper.selectOne(wrapper);
        if (session == null) {
            throw new BusinessException("问诊会话不存在");
        }
        
        ConsultationSessionVO vo = new ConsultationSessionVO();
        BeanUtils.copyProperties(session, vo);
        vo.setStatusDesc(getSessionStatusDesc(session.getStatus()));
        
        // 计算剩余时间
        if ("CONSULTING".equals(session.getStatus()) && session.getStartTime() != null) {
            LocalDateTime endTime = session.getStartTime().plusSeconds(consultationTimeout);
            Duration duration = Duration.between(LocalDateTime.now(), endTime);
            vo.setRemainingTime((int) duration.getSeconds());
        }
        
        return vo;
    }
    
    @Override
    public PrescriptionStatusVO getPrescriptionStatus(String userId, String prescriptionId) {
        Prescription prescription = getPrescriptionByIdAndUserId(prescriptionId, userId);
        
        PrescriptionStatusVO vo = new PrescriptionStatusVO();
        vo.setPrescriptionId(prescriptionId);
        vo.setStatus(prescription.getStatus());
        vo.setStatusDesc(getPrescriptionStatusDesc(prescription.getStatus()));
        vo.setAuditOpinion(prescription.getAuditOpinion());
        vo.setPrescriptionImage(prescription.getPrescriptionImage());
        
        return vo;
    }
    
    @Override
    public PrescriptionVO getPrescription(String userId, String prescriptionId) {
        Prescription prescription = getPrescriptionByIdAndUserId(prescriptionId, userId);
        
        PrescriptionVO vo = new PrescriptionVO();
        BeanUtils.copyProperties(prescription, vo);
        vo.setGenderDesc(prescription.getGender() == 1 ? "男" : "女");
        vo.setStatusDesc(getPrescriptionStatusDesc(prescription.getStatus()));
        
        // 解析复诊凭证图片
        if (prescription.getEvidenceImages() != null && !prescription.getEvidenceImages().isEmpty()) {
            vo.setEvidenceImages(JSON.parseArray(prescription.getEvidenceImages(), String.class));
        }
        
        return vo;
    }
    
    /**
     * 医生匹配算法（简化实现）
     */
    private String matchDoctor(String department) {
        // 实际应该：
        // 1. 查询该科室在线医生列表
        // 2. 排除队列已满（>5人）的医生
        // 3. 按评分、响应速度排序
        // 4. 选择排名第一的医生
        
        // 这里简化处理，返回模拟医生ID
        return "DOC" + IdUtil.getSnowflakeNextIdStr();
    }
    
    /**
     * 根据处方ID和用户ID获取处方
     */
    private Prescription getPrescriptionByIdAndUserId(String prescriptionId, String userId) {
        LambdaQueryWrapper<Prescription> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Prescription::getPrescriptionId, prescriptionId);
        wrapper.eq(Prescription::getUserId, userId);
        
        Prescription prescription = prescriptionMapper.selectOne(wrapper);
        if (prescription == null) {
            throw new BusinessException("处方不存在");
        }
        return prescription;
    }
    
    /**
     * 加密身份证号
     */
    private String encryptIdCard(String idCard) {
        // 实际应使用AES等加密算法
        // 这里简化处理
        return idCard;
    }
    
    /**
     * 获取会话状态描述
     */
    private String getSessionStatusDesc(String status) {
        switch (status) {
            case "WAITING":
                return "等待医生接诊";
            case "CONSULTING":
                return "问诊中";
            case "FINISHED":
                return "问诊已结束";
            case "TIMEOUT":
                return "问诊超时";
            default:
                return status;
        }
    }
    
    /**
     * 获取处方状态描述
     */
    private String getPrescriptionStatusDesc(String status) {
        switch (status) {
            case "PENDING":
                return "待审核";
            case "APPROVED":
                return "审核通过";
            case "REJECTED":
                return "审核拒绝";
            default:
                return status;
        }
    }
}

