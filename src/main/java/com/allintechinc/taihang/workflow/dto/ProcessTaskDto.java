package com.allintechinc.taihang.workflow.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 流程任务对象
 *
 * @author code generator tool
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProcessTaskDto {

    /**
     * 流程实例Id
     */
    private String processInstanceId;

    /**
     * 任务Id
     */
    private String taskId;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 审批人
     */
    private String assignee;

    /**
     * 状态
     */
    private String status;

    /**
     * 意见
     */
    private String comment;

    /**
     * 申请人Id
     */
    private Long applyUserId;

    /**
     * 申请人名称
     */
    private String applyUser;

    /**
     * 流程类型
     */
    private String processType;

    /**
     * 业务编码
     */
    private String businessCode;

}

