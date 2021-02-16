package com.allintechinc.taihang.workflow.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * 用户form
 *
 * @author code generator tool
 */
@Getter
@Setter
public class UserForm {
    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 用户名称不能为空
     */
    @NotBlank(message = "用户名称不能为空")
    private String userName;

}