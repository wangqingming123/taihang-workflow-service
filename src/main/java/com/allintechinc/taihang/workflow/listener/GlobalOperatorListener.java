package com.allintechinc.taihang.workflow.listener;

import com.allintechinc.kunlun.pubsub.publish.Publisher;
import com.allintechinc.taihang.workflow.dto.ProcessMessageDTO;
import com.allintechinc.taihang.workflow.entity.ProcessTaskHistory;
import com.allintechinc.taihang.workflow.enums.ProcessStatusEnum;
import com.allintechinc.taihang.workflow.repository.ProcessHandleRecordRepository;
import com.allintechinc.taihang.workflow.util.IConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 全局监听器
 *
 * @author sundp
 */
@Component
@Slf4j
public class GlobalOperatorListener implements FlowableEventListener {

    private final ProcessHandleRecordRepository processHandleRecordRepository;
    private final Publisher publisher;
    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private static final String TOPIC_NAME = "process_approve_status";

    @Autowired
    public GlobalOperatorListener(ProcessHandleRecordRepository processHandleRecordRepository, Publisher publisher, TaskService taskService, RuntimeService runtimeService) {
        this.processHandleRecordRepository = processHandleRecordRepository;
        this.publisher = publisher;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    @Override
    public void onEvent(FlowableEvent event) {
        boolean isReject = false, ending = false;
        ProcessTaskHistory processTaskHistory = null;
        if (event instanceof org.flowable.common.engine.impl.event.FlowableEntityEventImpl) {
            org.flowable.common.engine.impl.event.FlowableEntityEventImpl flowableEntityEvent = (org.flowable.common.engine.impl.event.FlowableEntityEventImpl) event;
            TaskEntityImpl taskEntity = (TaskEntityImpl) flowableEntityEvent.getEntity();
            String taskId = taskEntity.getId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(taskEntity.getProcessInstanceId()).singleResult();
            String assignee = taskEntity.getAssignee();
            String[] items = StringUtils.split(assignee, '_');
            processTaskHistory = new ProcessTaskHistory();
            processTaskHistory.setBusinessId((Long) taskService.getVariable(taskId, IConstants.PROCESS_BUSINESS_ID))
                    .setBusinessCode(processInstance.getBusinessKey())
                    .setProcessType((String) taskService.getVariable(taskId, IConstants.PROCESS_PROCESS_TYPE))
                    .setProcessInstanceId(taskEntity.getProcessInstanceId())
                    .setTaskId(taskId).setTaskName(taskEntity.getName())
                    .setStatus(ProcessStatusEnum.IN_PROCESS.toString())
                    .setApplyUserId((Long) taskService.getVariable(taskId, IConstants.PROCESS_APPLY_USER_ID))
                    .setApplyUser((String) taskService.getVariable(taskId, IConstants.PROCESS_APPLY_USER))
                    .setProcessInstanceName((String) taskService.getVariable(taskId, IConstants.PROCESS_PROCESS_NAME))
                    .setTenantId(Long.parseLong(processInstance.getTenantId()));
            if (items.length == 1) {
                processTaskHistory.setOperatorName(taskEntity.getName()).setOperatorId(Long.valueOf(items[0]));
            } else {
                processTaskHistory.setOperatorName(items[1]).setOperatorId(Long.valueOf(items[0]));
            }

        } else if (event instanceof FlowableProcessTerminatedEventImpl) {
            isReject = true;
            ending = true;
            FlowableProcessTerminatedEventImpl flowableProcessTerminatedEvent = (FlowableProcessTerminatedEventImpl) event;
            DelegateExecution delegateExecution = flowableProcessTerminatedEvent.getExecution();
            processTaskHistory = generateProcessTaskHistory(delegateExecution, ProcessStatusEnum.REJECT.toString(), null);
        } else if (event instanceof FlowableEntityEventImpl) {
            FlowableEntityEventImpl flowableEntityEvent = (FlowableEntityEventImpl) event;
            Object object = flowableEntityEvent.getEntity();
            String taskName = null;
            if (object instanceof TaskEntityImpl) {
                taskName = ((TaskEntityImpl) object).getName();
            }
            DelegateExecution delegateExecution = flowableEntityEvent.getExecution();
            processTaskHistory = generateProcessTaskHistory(delegateExecution, (String) delegateExecution.getVariable(IConstants.PROCESS_STATUS), taskName);
            if (event.getType().name().equals(FlowableEngineEventType.PROCESS_COMPLETED.toString())) {
                ending = true;
            }
        }
        assert processTaskHistory != null;
        if (ending) {
            processTaskHistory.setStatus(ProcessStatusEnum.END.toString())
                    .setOperatorId(0L).setOperatorName(IConstants.SYSTEM)
                    .setEndTime(LocalDateTime.now()).setComment(IConstants.PROCESS_END);
            processHandleRecordRepository.save(processTaskHistory);
            //流程结束后需要发送消息
            sendMessage(isReject, processTaskHistory);
        } else {
            if (!event.getType().equals(FlowableEngineEventType.TASK_CREATED)) {
                processTaskHistory.setEndTime(LocalDateTime.now());
            }
            processHandleRecordRepository.save(processTaskHistory);
        }
    }

    private ProcessTaskHistory generateProcessTaskHistory(DelegateExecution delegateExecution, String status, String taskName) {
        ProcessTaskHistory processTaskHistory = new ProcessTaskHistory();
        processTaskHistory.setBusinessId((Long) delegateExecution.getVariable(IConstants.PROCESS_BUSINESS_ID))
                .setBusinessCode(delegateExecution.getProcessInstanceBusinessKey())
                .setProcessType((String) delegateExecution.getVariable(IConstants.PROCESS_PROCESS_TYPE))
                .setProcessInstanceId(delegateExecution.getProcessInstanceId())
                .setTaskId(delegateExecution.getId())
                .setApplyUserId((Long) delegateExecution.getVariable(IConstants.PROCESS_APPLY_USER_ID))
                .setApplyUser((String) delegateExecution.getVariable(IConstants.PROCESS_APPLY_USER))
                .setStatus(status).setTaskName(taskName)
                .setComment((String) delegateExecution.getVariable(IConstants.PROCESS_COMMENT))
                .setProcessInstanceName((String) delegateExecution.getVariable(IConstants.PROCESS_PROCESS_NAME))
                .setTenantId(Long.parseLong(delegateExecution.getTenantId()))
                .setOperatorName((String) delegateExecution.getVariable(IConstants.PROCESS_REVIEWER_NAME))
                .setOperatorId((Long) delegateExecution.getVariable(IConstants.PROCESS_REVIEWER_ID));
        return processTaskHistory;
    }

    private void sendMessage(boolean isReject, ProcessTaskHistory processTaskHistory) {
        ProcessMessageDTO processMessageDTO = new ProcessMessageDTO();
        processMessageDTO.setTenantId(processTaskHistory.getTenantId()).setApplyUserId(processTaskHistory.getApplyUserId())
                .setProcessType(processTaskHistory.getProcessType()).setBusinessCode(processTaskHistory.getBusinessCode())
                .setStatus(isReject ? ProcessStatusEnum.REJECT.toString() : ProcessStatusEnum.END.toString());
        publisher.publish(TOPIC_NAME, processMessageDTO.toString());
        log.info("流程审批发送消息成功:{}", processMessageDTO.toString());
    }


    @Override
    public boolean isFailOnException() {
        return false;
    }

    @Override
    public boolean isFireOnTransactionLifecycleEvent() {
        return false;
    }

    @Override
    public String getOnTransaction() {
        return null;
    }
}