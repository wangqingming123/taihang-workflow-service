package com.allintechinc.taihang.workflow.handler.impl;

import com.allintechinc.taihang.workflow.common.ResponseResult;
import com.allintechinc.taihang.workflow.dto.ProcessTaskDto;
import com.allintechinc.taihang.workflow.handler.ProcessInstanceHandler;
import com.allintechinc.taihang.workflow.util.IConstants;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author sundp
 */
@Component
@Slf4j
public class ProcessInstanceHandlerImpl implements ProcessInstanceHandler {

    private final RuntimeService runtimeService;
    private final IdentityService identityService;
    private final TaskService taskService;

    @Autowired
    public ProcessInstanceHandlerImpl(RuntimeService runtimeService, IdentityService identityService, TaskService taskService) {
        this.runtimeService = runtimeService;
        this.identityService = identityService;
        this.taskService = taskService;
    }

    @Override
    public ResponseResult<ProcessTaskDto> startProcessByProcessKeyAndReturnFirstTask(Long tenantId, String businessKey, String processDefinitionKey, Map<String, Object> variables) {
        Long applyUserId = (Long) variables.get(IConstants.PROCESS_APPLY_USER_ID);
        //设置发起人
        identityService.setAuthenticatedUserId(String.valueOf(applyUserId));
        //启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(processDefinitionKey, businessKey, variables, String.valueOf(tenantId));
        String processInstanceId = processInstance.getProcessInstanceId();
        log.info("流程实例ID:{}---流程定义ID:{}", processInstanceId, processInstance.getProcessDefinitionId());
        //获取当前活动的任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).active().singleResult();
        ProcessTaskDto processTaskDto = new ProcessTaskDto();
        BeanUtils.copyProperties(task, processTaskDto);
        processTaskDto.setProcessInstanceId(processInstanceId)
                .setTaskId(task.getId())
                .setTaskName(task.getName())
                .setApplyUserId(applyUserId)
                .setApplyUser((String) variables.get(IConstants.PROCESS_APPLY_USER));
        return new ResponseResult<>(processTaskDto);
    }
}
