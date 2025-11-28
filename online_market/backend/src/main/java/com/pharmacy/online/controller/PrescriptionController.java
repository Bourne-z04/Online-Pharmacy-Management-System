package com.pharmacy.online.controller;

import com.pharmacy.online.common.Result;
import com.pharmacy.online.dto.*;
import com.pharmacy.online.service.PrescriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * 处方控制器
 */
@Slf4j
@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    /**
     * 提交患者信息
     */
    @PostMapping("/patient-info")
    public Result<String> submitPatientInfo(@RequestAttribute("userId") String userId,
                                             @Valid @RequestBody PatientInfoRequest request) {
        log.info("提交患者信息: userId={}, request={}", userId, request);
        String prescriptionId = prescriptionService.submitPatientInfo(userId, request);
        return Result.success("提交成功", prescriptionId);
    }
    
    /**
     * 上传复诊凭证
     */
    @PostMapping("/evidence")
    public Result<List<String>> uploadEvidence(@RequestAttribute("userId") String userId,
                                                @RequestParam("files") MultipartFile[] files,
                                                @RequestParam("prescriptionId") String prescriptionId) {
        log.info("上传复诊凭证: userId={}, prescriptionId={}, count={}", 
                userId, prescriptionId, files.length);
        List<String> urls = prescriptionService.uploadEvidence(userId, prescriptionId, files);
        return Result.success("上传成功", urls);
    }
    
    /**
     * 创建问诊会话
     */
    @PostMapping("/consultation/create")
    public Result<ConsultationSessionVO> createConsultation(
            @RequestAttribute("userId") String userId,
            @Valid @RequestBody ConsultationCreateRequest request) {
        log.info("创建问诊会话: userId={}, request={}", userId, request);
        ConsultationSessionVO session = prescriptionService.createConsultation(userId, request);
        return Result.success("问诊创建成功", session);
    }
    
    /**
     * 获取问诊会话信息
     */
    @GetMapping("/consultation/{sessionId}")
    public Result<ConsultationSessionVO> getConsultation(@RequestAttribute("userId") String userId,
                                                          @PathVariable String sessionId) {
        log.info("获取问诊会话: userId={}, sessionId={}", userId, sessionId);
        ConsultationSessionVO session = prescriptionService.getConsultation(userId, sessionId);
        return Result.success(session);
    }
    
    /**
     * 获取处方状态
     */
    @GetMapping("/{prescriptionId}/status")
    public Result<PrescriptionStatusVO> getPrescriptionStatus(@RequestAttribute("userId") String userId,
                                                               @PathVariable String prescriptionId) {
        log.info("获取处方状态: userId={}, prescriptionId={}", userId, prescriptionId);
        PrescriptionStatusVO status = prescriptionService.getPrescriptionStatus(userId, prescriptionId);
        return Result.success(status);
    }
    
    /**
     * 获取处方详情
     */
    @GetMapping("/{prescriptionId}")
    public Result<PrescriptionVO> getPrescription(@RequestAttribute("userId") String userId,
                                                   @PathVariable String prescriptionId) {
        log.info("获取处方详情: userId={}, prescriptionId={}", userId, prescriptionId);
        PrescriptionVO prescription = prescriptionService.getPrescription(userId, prescriptionId);
        return Result.success(prescription);
    }
}

