package com.allintechinc.taihang.workflow.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 申请人列表
 *
 * @author code generator tool
 */
@Getter
@Setter
@EqualsAndHashCode
public class ApplyUserDto {

    /**
     * 申请人编码
     */
    private Long applyUserId;

    /**
     * 申请人姓名
     */
    private String applyUserName;

}

