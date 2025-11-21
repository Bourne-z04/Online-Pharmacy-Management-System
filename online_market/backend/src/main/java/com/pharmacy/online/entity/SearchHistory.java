package com.pharmacy.online.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 搜索历史实体类
 */
@Data
@TableName("search_history")
public class SearchHistory implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 历史ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 搜索关键词
     */
    private String keyword;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

