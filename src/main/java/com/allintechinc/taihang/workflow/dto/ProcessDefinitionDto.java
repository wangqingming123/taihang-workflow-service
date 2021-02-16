package com.allintechinc.taihang.workflow.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 流程记录DTO
 *
 * @author code generator tool
 */
@Getter
@Setter
@ToString
public class ProcessDefinitionDto implements Serializable {

    private static final long serialVersionUID = 7137566675501571511L;
    /**
     * 主键
     */
    private Long id;

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
     * 审批人编号
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
     * 最后更新人
     */
    private Long lastModifiedBy;

    /**
     * 最后更新时间
     */
    private String lastModifiedDate;

}

