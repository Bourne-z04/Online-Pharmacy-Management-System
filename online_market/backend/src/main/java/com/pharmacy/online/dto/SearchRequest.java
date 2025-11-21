package com.pharmacy.online.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * 搜索请求DTO
 */
@Data
public class SearchRequest {
    
    /**
     * 搜索关键词
     */
    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;
}

