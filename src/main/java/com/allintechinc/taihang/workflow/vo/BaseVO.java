package com.allintechinc.taihang.workflow.vo;


import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sundp
 */
@Data
@Accessors(chain = true)
public class BaseVO implements Serializable {
    private static final long serialVersionUID = 820888703199648103L;
    private String id;
    private String name;
    private String conditionExpression;
}

