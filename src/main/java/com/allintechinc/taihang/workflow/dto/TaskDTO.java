package com.allintechinc.taihang.workflow.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

import java.io.Serializable;
import java.util.Date;

/**
 * 流程任务dto
 *
 * @author sundp
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskDTO extends TaskEntityImpl implements Serializable {
    private static final long serialVersionUID = -84996183283215133L;

    /**
     * 流程类型
     */
    private String processType;
    /**
     * 流程名称
     */
    private String processDefinitionName;

    /**
     * 申请人Id
     */
    private Long applyUserId;

    /**
     * 申请人名称
     */
    private String applyUser;

    /**
     * 状态
     */
    private String status;

    /**
     * 业务对象主键
     */
    private Long businessId;

    /**
     * 业务编码
     */
    private String businessCode;

    /**
     * 业务数据
     */
    private String businessData;

    /**
     * 审批说明
     */
    private String comment;

    /**
     * 操作人Id
     */
    private Long operatorId;

    /**
     * 结束时间
     */
    private Date endTime;

}
