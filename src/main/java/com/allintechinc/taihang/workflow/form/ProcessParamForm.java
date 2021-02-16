package com.allintechinc.taihang.workflow.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 流程启动表单
 *
 * @author code generator tool
 */
@Getter
@Setter
public class ProcessParamForm implements Serializable {

    private static final long serialVersionUID = 2374807104111326935L;

    /**
     * 流程类型
     */
    @NotBlank(message = "流程类型不能为空")
    private String processType;

    /**
     * 申请人Id
     */
    private Long applyUserId;

    /**
     * 申请人
     */
    @NotBlank(message = "申请人不能为空")
    private String applyUser;

    /**
     * 业务主键
     */
    private Long businessId;
    /**
     * 业务编码
     */
    @NotBlank(message = "业务编码不能为空")
    private String businessCode;

    /**
     * 租户Id
     */
    private Long tenantId;

    /**
     * 流程Key
     */
    private String processKey;

    /**
     * 业务表单数据
     */
    private String businessData;
}

