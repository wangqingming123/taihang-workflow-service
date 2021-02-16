package com.allintechinc.taihang.workflow.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 流程记录表单
 *
 * @author sundp
 */
@Getter
@Setter
@ToString
public class ProcessDefinitionForm implements Serializable {

    private static final long serialVersionUID = 1388782248570497610L;
    /**
     * 租户编码
     */
    private Long tenantId;
    /**
     * 流程类型
     */
    private String processType;
    /**
     * 流程唯一键
     */
    private String processKey;
    /**
     * 流程名称
     */
    private String name;
    /**
     * 审批人Id
     */
    private String reviewerIds;
    /**
     * 审批人名称
     */
    private String reviewerNames;
    /**
     * 流程定义
     */
    private String contents;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private String createDate;
    /**
     * 最后修改人
     */
    private Long lastModifiedBy;
    /**
     * 最后修改时间
     */
    private String lastModifiedDate;

}

