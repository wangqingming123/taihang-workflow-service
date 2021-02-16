package com.allintechinc.taihang.workflow.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author sundp
 */
@Getter
@Setter
@Accessors(chain = true)
public class ProcessMessageDTO implements Serializable {
    private static final long serialVersionUID = -6632168080836563955L;
    private Long tenantId;
    private Long applyUserId;
    private String processType;
    private String businessCode;
    private String status;

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
