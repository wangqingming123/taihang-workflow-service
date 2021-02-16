package com.allintechinc.taihang.workflow.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 流程处理记录DTO
 *
 * @author code generator tool
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProcessTaskHistoryDto {

    /**
     * 主键
     */
    private Long id;

    /**
     * 业务主键
     */
    private Long businessId;
    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 流程类型
     */
    private String processType;

    /**
     * 流程实例Id
     */
    private String processInstanceId;

    /**
     * 流程实例名称
     */
    private String processInstanceName;


    /**
     * 操作人Id
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 当前任务Id
     */
    private String taskId;

    /**
     * 当前任务名称
     */
    private String taskName;

    /**
     * 流程状态,draft:草稿 submitted:提交成功 in_process:审批中 approved:审批通过 reject:驳回 refuse:拒绝 end:结束(归档) cancel:取消
     */
    private String status;

    /**
     * 审批人意见
     */
    private String comment;

    /**
     * 申请人Id
     */
    private Long applyUserId;

    /**
     * 申请人
     */
    private String applyUser;

    /**
     * 业务表单数据
     */
    private String businessData;

}

