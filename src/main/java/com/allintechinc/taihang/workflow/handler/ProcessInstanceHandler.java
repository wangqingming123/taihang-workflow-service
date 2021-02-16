package com.allintechinc.taihang.workflow.handler;

import com.allintechinc.taihang.workflow.common.ResponseResult;
import com.allintechinc.taihang.workflow.dto.ProcessTaskDto;

import java.util.Map;

/**
 * @author sundp
 * 流程实例
 */
public interface ProcessInstanceHandler {

    /**
     * 启动流程
     *
     * @param tenantId
     * @param businessKey
     * @param processDefinitionKey
     * @param variables
     * @return
     */
    ResponseResult<ProcessTaskDto> startProcessByProcessKeyAndReturnFirstTask(Long tenantId, String businessKey, String processDefinitionKey, Map<String, Object> variables);
}
