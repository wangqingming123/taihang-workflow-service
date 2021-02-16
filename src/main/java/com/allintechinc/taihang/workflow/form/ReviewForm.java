package com.allintechinc.taihang.workflow.form;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 审批传参
 *
 * @author code generator tool
 */
@Getter
@Setter
public class ReviewForm {

    /**
     * 任务Id
     */
    @NotBlank(message = "任务编码不能为空")
    private String taskId;
    /**
     * 流程实例Id
     */
    @NotBlank(message = "流程实例编码不能为空")
    private String processInstanceId;
    /**
     * 审批人Id
     */
    private Long reviewerId;
    /**
     * 审批人名称
     */
    @NotBlank(message = "审批人不能为空")
    private String reviewerName;
    /**
     * 意见
     */
    @NotBlank(message = "审批意见不能为空")
    @Size(max = 150, message = "审批意见不能超过150个字符")
    @Length
    private String comment;
    /**
     * 状态
     */
    private String status;

}

