package com.allintechinc.taihang.workflow.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.flowable.engine.impl.persistence.entity.HistoricProcessInstanceEntityImpl;

import java.io.Serializable;

/**
 * @author sundp
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ProcessInstanceDTO extends HistoricProcessInstanceEntityImpl implements Serializable {

    private static final long serialVersionUID = 3860405803276162837L;

    /**
     * 业务对象主键
     */
    private Long businessId;
    /**
     * 状态
     */
    private String status;
    /**
     * 申请人姓名
     */
    private String applyUser;
    /**
     * 业务数据
     */
    private String businessData;
}
