package com.allintechinc.taihang.workflow.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author sundp
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "process_task_history")
@DynamicInsert
public class ProcessTaskHistory implements Serializable {

    private static final long serialVersionUID = 8568342313989621572L;
    /**
     * 主键ID，根据数据库策越选择生成ID方式
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tenantId;
    private Long businessId;
    private String businessCode;
    private String processType;
    private String processInstanceId;
    private String processInstanceName;
    private Long operatorId;
    private String operatorName;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private String taskId;
    private String taskName;
    private String status;
    private String comment;
    private Long applyUserId;
    private String applyUser;
}

