package com.allintechinc.taihang.workflow.form;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 查询流程传参
 *
 * @author code generator tool
 */
@Getter
@Setter
public class ParamForm implements Serializable {

    private static final long serialVersionUID = -1828334494634373386L;
    /**
     * 租户
     */
    private Long tenantId;

    /**
     * 流程类型
     */
    private List<String> processTypes;

}

