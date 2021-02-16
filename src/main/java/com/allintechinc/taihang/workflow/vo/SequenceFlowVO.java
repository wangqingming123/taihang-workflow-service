package com.allintechinc.taihang.workflow.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sundp
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class SequenceFlowVO extends BaseVO implements Serializable {
    private static final long serialVersionUID = 525307171941922965L;
    private String sourceRef;
    private String targetRef;
}
