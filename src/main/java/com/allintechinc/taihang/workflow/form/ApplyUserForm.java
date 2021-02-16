package com.allintechinc.taihang.workflow.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 查询申请人form
 *
 * @author code generator tool
 */
@Getter
@Setter
public class ApplyUserForm {


    /**
     * 申请人Id
     */
    private Long reviewerId;

    /**
     * 申请人笔名程
     */
    @NotBlank(message = "审批人名称不能为空")
    private String reviewerName;
    /**
     * 状态
     */
    private String status;

}

