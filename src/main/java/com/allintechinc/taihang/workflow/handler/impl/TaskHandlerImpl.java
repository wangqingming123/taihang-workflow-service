package com.allintechinc.taihang.workflow.handler.impl;

import com.allintechinc.taihang.form.SearchForm;
import com.allintechinc.taihang.request.HttpRequestHolder;
import com.allintechinc.taihang.utils.DateUtil;
import com.allintechinc.taihang.utils.MapUtil;
import com.allintechinc.taihang.workflow.common.ResponseResult;
import com.allintechinc.taihang.workflow.dto.ProcessInstanceDTO;
import com.allintechinc.taihang.workflow.dto.TaskDTO;
import com.allintechinc.taihang.workflow.enums.ProcessStatusEnum;
import com.allintechinc.taihang.workflow.exception.ProcessHandleRecordException;
import com.allintechinc.taihang.workflow.exception.ProcessRecordBusinessException;
import com.allintechinc.taihang.workflow.form.ReviewForm;
import com.allintechinc.taihang.workflow.handler.TaskHandler;
import com.allintechinc.taihang.workflow.util.IConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfoQuery;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @author sundp
 */
@Component
public class TaskHandlerImpl implements TaskHandler {

    private final TaskService taskService;
    private final HistoryService historyService;
    private final RuntimeService runtimeService;

    private static final String SEARCH_VALUE = "%{0}%";
    private static final String[] TASK_IGNORE_PROPERTIES = {"transientVariables", "variables", "variablesLocal"};

    @Autowired
    public TaskHandlerImpl(RuntimeService runtimeService, TaskService taskService, HistoryService historyService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.historyService = historyService;
    }

    @Override
    public ResponseResult<String> handleTask(ReviewForm reviewForm) {
        Long reviewerId = Objects.nonNull(reviewForm.getReviewerId()) ? reviewForm.getReviewerId() : HttpRequestHolder.getCurrentUserId();
        String assignee = String.valueOf(reviewerId);
        Task task = taskService.createTaskQuery()
                .taskTenantId(String.valueOf(HttpRequestHolder.getCurrentTenantId()))
                .taskId(reviewForm.getTaskId()).taskAssignee(assignee).singleResult();
        if (Objects.isNull(task)) {
            //兼容原来的id+name方式
            assignee = String.valueOf(reviewerId) + '_' + reviewForm.getReviewerName();
            task = taskService.createTaskQuery()
                    .taskTenantId(String.valueOf(HttpRequestHolder.getCurrentTenantId()))
                    .taskId(reviewForm.getTaskId()).taskAssignee(assignee).singleResult();
        }
        if (Objects.isNull(task)) {
            throw ProcessHandleRecordException.PROCESS_TASK_NOT_EXISTS;
        }
        Map<String, Object> map;
        try {
            map = MapUtil.beanToMap(reviewForm);
            map.put(IConstants.HANDLE_OUTCOME, reviewForm.getStatus().toLowerCase());
        } catch (Exception e) {
            throw ProcessHandleRecordException.PROCESS_BEAN_T_MAP_ERROR;
        }
        taskService.complete(reviewForm.getTaskId(), map);
        return ResponseResult.success(reviewForm.getStatus());
    }

    @Override
    public ResponseResult<Map<String, Object>> getTasksByTenantIdAndAssigneeAndStatus(Long tenantId, SearchForm searchForm) {
        Map<String, Object> mapFilter = searchForm.getFilter();
        //处理人
        String assignee = (String) mapFilter.get(IConstants.PROCESS_ASSIGNEE);
        //任务状态 待办、已办
        String status = (String) mapFilter.get(IConstants.PROCESS_STATUS);
        if (StringUtils.isEmpty(assignee) || StringUtils.isEmpty(status)) {
            throw ProcessRecordBusinessException.PROCESS_ASSIGNEE_AND_STATUS_CANNOT_BE_EMPTY;
        }
        Long assigneeId = HttpRequestHolder.getCurrentUserId();
        //流程类型
        String processType = (String) mapFilter.get(IConstants.PROCESS_PROCESS_TYPE);
        //申请人
        String applyUser = (String) mapFilter.get(IConstants.PROCESS_APPLICATION_NAME);
        //流程距离当前间隔时间
        Integer intervalDate = (Integer) mapFilter.get(IConstants.PROCESS_INTERVAL_DATE);
        //流程名称
        String processName = (String) mapFilter.get(IConstants.PROCESS_PROCESS_NAME);
        //业务编码
        String businessCode = (String) mapFilter.get(IConstants.PROCESS_BUSINESS_CODE);
        //任务创建时间范围
        Map<String, Object> mapTime = (Map<String, Object>) mapFilter.get(IConstants.CREATE_TIME);
        String assigneeName = String.valueOf(assigneeId) + '_' + assignee;
        TaskInfoQuery taskInfoQuery;
        if (status.equals(ProcessStatusEnum.IN_PROCESS.toString())) {
            //待办
            taskInfoQuery = taskService.createTaskQuery().taskTenantId(String.valueOf(tenantId)).taskAssignee(assigneeName).active();
            if ((int) taskInfoQuery.count() == 0) {
                taskInfoQuery = taskService.createTaskQuery().taskTenantId(String.valueOf(tenantId)).taskAssignee(String.valueOf(assigneeId)).active();
            }
        } else {
            //已办
            taskInfoQuery = historyService.createHistoricTaskInstanceQuery().taskTenantId(String.valueOf(tenantId)).taskAssignee(assigneeName).finished();
            if ((int) taskInfoQuery.count() == 0) {
                taskInfoQuery = historyService.createHistoricTaskInstanceQuery().taskTenantId(String.valueOf(tenantId)).taskAssignee(String.valueOf(assigneeId)).finished();
            }
            //按照狀態
            if (!status.equals(ProcessStatusEnum.END.toString())) {
                taskInfoQuery.processVariableValueEquals(IConstants.PROCESS_STATUS, status);
            }
        }
        //按照流程类型过滤
        if (!StringUtils.isEmpty(processType)) {
            taskInfoQuery.processDefinitionName(processType);
        }
        //按照申请人过滤
        if (!StringUtils.isEmpty(applyUser)) {
            taskInfoQuery.processVariableValueLike(IConstants.PROCESS_APPLY_USER, MessageFormat.format(SEARCH_VALUE, applyUser));
        }
        //按照流程名称
        if (!StringUtils.isEmpty(processName)) {
            taskInfoQuery.processVariableValueLike(IConstants.PROCESS_PROCESS_NAME, MessageFormat.format(SEARCH_VALUE, processName));
        }
        //按照业务编码过滤
        if (!StringUtils.isEmpty(businessCode)) {
            taskInfoQuery.processInstanceBusinessKeyLike(MessageFormat.format(SEARCH_VALUE, businessCode));
        }
        //按照创建时间过滤
        if (!CollectionUtils.isEmpty(mapTime)) {
            String afterDateValue = (String) mapTime.get(IConstants.CREATE_DATE_MIN);
            String beforeDateValue = (String) mapTime.get(IConstants.CREATE_DATE_MAX);
            if (StringUtils.isNotEmpty(afterDateValue)) {
                taskInfoQuery.taskCreatedAfter(DateUtil.parse(afterDateValue, IConstants.DATE_FORMAT));
            }
            if (StringUtils.isNotEmpty(beforeDateValue)) {
                LocalDate beforeDate = LocalDate.parse(beforeDateValue, IConstants.DATE_TIME_FORMATTER);
                taskInfoQuery.taskCreatedBefore(getNextDate(beforeDate));
            }
        }
        //按照时间间隔过滤
        Date createdOnDate = getCreatedOnDate(intervalDate);
        if (Objects.nonNull(createdOnDate)) {
            taskInfoQuery.taskCreatedOn(createdOnDate);
        }
        taskInfoQuery.orderByTaskCreateTime().desc();
        //保存查询結果
        Map<String, Object> map = Maps.newHashMap();
        //数据总数
        map.put(IConstants.TOTAL, (int) taskInfoQuery.count());
        List<TaskDTO> list = generateTaskList(status, processType, taskInfoQuery, searchForm.pageable());
        map.put(IConstants.TASK_LIST, list);
        return new ResponseResult<>(map);
    }

    private List<TaskDTO> generateTaskList(String status, String processType, TaskInfoQuery taskInfoQuery, Pageable pageable) {
        int pageNum = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        List<TaskDTO> list = Lists.newLinkedList();
        if (status.equals(ProcessStatusEnum.IN_PROCESS.toString())) {
            //待办
            List<Task> taskList = ((TaskQuery) taskInfoQuery).listPage(pageNum * pageSize, pageSize);
            taskList.forEach(task -> {
                String processId = task.getProcessInstanceId();
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
                TaskDTO taskDto = new TaskDTO();
                Long applyUserId = (Long) taskService.getVariable(task.getId(), IConstants.PROCESS_APPLY_USER_ID);
                BeanUtils.copyProperties(task, taskDto, TASK_IGNORE_PROPERTIES);
                taskDto.setApplyUserId(applyUserId);
                taskDto.setApplyUser((String) taskService.getVariable(task.getId(), IConstants.PROCESS_APPLY_USER));
                taskDto.setProcessType(!StringUtils.isEmpty(processType) ? processType : processInstance.getProcessDefinitionName());
                taskDto.setProcessDefinitionName((String) taskService.getVariable(task.getId(), IConstants.PROCESS_PROCESS_NAME));
                taskDto.setStatus(ProcessStatusEnum.IN_PROCESS.toString());
                taskDto.setBusinessCode(processInstance.getBusinessKey());
                taskDto.setBusinessId((Long) taskService.getVariable(task.getId(), IConstants.PROCESS_BUSINESS_ID));
                taskDto.setBusinessData((String) taskService.getVariable(task.getId(), IConstants.PROCESS_BUSINESS_DATA));
                list.add(taskDto);
            });
        } else {
            //已办理
            List<HistoricTaskInstance> hisTaskList = ((HistoricTaskInstanceQuery) taskInfoQuery).listPage(pageNum * pageSize, pageSize);
            hisTaskList.forEach(historicTaskInstance -> {
                TaskDTO taskDto = new TaskDTO();
                BeanUtils.copyProperties(historicTaskInstance, taskDto);
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).singleResult();
                taskDto.setBusinessCode(historicProcessInstance.getBusinessKey());
                List<HistoricVariableInstance> entityList = historyService.createHistoricVariableInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).orderByVariableName().desc().list();
                Optional<HistoricVariableInstance> optional = entityList.stream().filter(historicVariableInstance -> historicVariableInstance.getVariableName().equals(IConstants.PROCESS_APPLY_USER_ID)).findAny();
                optional.ifPresent(historicVariableInstance -> taskDto.setApplyUserId((Long) historicVariableInstance.getValue()));
                optional = entityList.stream().filter(historicVariableInstance -> historicVariableInstance.getVariableName().equals(IConstants.PROCESS_REVIEWER_ID)).findAny();
                optional.ifPresent(historicVariableInstance -> taskDto.setOperatorId((Long) historicVariableInstance.getValue()));
                optional = entityList.stream().filter(historicVariableInstance -> historicVariableInstance.getVariableName().equals(IConstants.PROCESS_BUSINESS_ID)).findAny();
                optional.ifPresent(historicVariableInstance -> taskDto.setBusinessId((Long) historicVariableInstance.getValue()));
                taskDto.setApplyUser(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_APPLY_USER));
                taskDto.setBusinessData(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_BUSINESS_DATA));
                taskDto.setStatus(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_STATUS));
                taskDto.setComment(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_COMMENT));
                taskDto.setProcessType(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_PROCESS_TYPE));
                taskDto.setProcessDefinitionName(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_PROCESS_NAME));
                list.add(taskDto);
            });
        }
        return list;
    }

    @Override
    public ResponseResult<Map<String, Object>> getOwnerApprove(Long tenantId, SearchForm searchForm) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery()
                .processInstanceTenantId(String.valueOf(tenantId)).involvedUser(String.valueOf(HttpRequestHolder.getCurrentUserId()));
        Map<String, Object> mapFilter = searchForm.getFilter();
        //流程类型
        String processType = (String) mapFilter.get(IConstants.PROCESS_PROCESS_TYPE);
        //申请人
        String applyUser = (String) mapFilter.get(IConstants.PROCESS_APPLICATION_NAME);
        //流程名称
        String processName = (String) mapFilter.get(IConstants.PROCESS_PROCESS_NAME);
        //业务编码
        String businessCode = (String) mapFilter.get(IConstants.PROCESS_BUSINESS_CODE);
        //状态
        String status = (String) mapFilter.get(IConstants.PROCESS_STATUS);
        //任务创建时间范围
        Map<String, Object> mapTime = (Map<String, Object>) mapFilter.get(IConstants.CREATE_TIME);
        Map<String, Object> map = Maps.newHashMap();
        //按照流程类型过滤
        if (!StringUtils.isEmpty(processType)) {
            historicProcessInstanceQuery.processDefinitionName(processType);
        }
        //按照流程名称
        if (!StringUtils.isEmpty(processName)) {
            historicProcessInstanceQuery.variableValueLike(IConstants.PROCESS_PROCESS_NAME, MessageFormat.format(SEARCH_VALUE, processName));
        }
        //按照业务编码过滤
        if (!StringUtils.isEmpty(businessCode)) {
            historicProcessInstanceQuery.processInstanceBusinessKeyLike(MessageFormat.format(SEARCH_VALUE, businessCode));
        }
        //按照创建时间过滤
        if (!CollectionUtils.isEmpty(mapTime)) {
            String afterDateValue = (String) mapTime.get(IConstants.CREATE_DATE_MIN);
            String beforeDateValue = (String) mapTime.get(IConstants.CREATE_DATE_MAX);
            if (StringUtils.isNotEmpty(afterDateValue)) {
                historicProcessInstanceQuery.startedAfter(DateUtil.parse(afterDateValue, IConstants.DATE_FORMAT));
            }
            if (StringUtils.isNotEmpty(beforeDateValue)) {
                LocalDate beforeDate = LocalDate.parse(beforeDateValue, IConstants.DATE_TIME_FORMATTER);
                historicProcessInstanceQuery.startedBefore(getNextDate(beforeDate));
            }
        }
        if (StringUtils.isNotEmpty(status)) {
            if (ProcessStatusEnum.IN_PROCESS.toString().equals(status)) {
                historicProcessInstanceQuery.unfinished();
            } else {
                historicProcessInstanceQuery.finished();
            }
        }
        historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc();

        map.put(IConstants.TOTAL, (int) historicProcessInstanceQuery.count());
        int pageNum = searchForm.pageable().getPageNumber();
        int pageSize = searchForm.pageable().getPageSize();
        List<ProcessInstanceDTO> list = Lists.newLinkedList();
        List<HistoricProcessInstance> hisProcessList = historicProcessInstanceQuery.listPage(pageNum * pageSize, pageSize);
        hisProcessList.forEach(historicTaskInstance -> {
            ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();
            BeanUtils.copyProperties(historicTaskInstance, processInstanceDTO);
            processInstanceDTO.setStatus(Objects.isNull(historicTaskInstance.getEndTime()) ? ProcessStatusEnum.IN_PROCESS.toString() : ProcessStatusEnum.END.toString());

            List<HistoricVariableInstance> entityList = historyService.createHistoricVariableInstanceQuery()
                    .processInstanceId(historicTaskInstance.getId()).orderByVariableName().desc().list();
            processInstanceDTO.setApplyUser(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_APPLY_USER));
            processInstanceDTO.setName(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_PROCESS_NAME));
            processInstanceDTO.setProcessDefinitionName(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_PROCESS_TYPE));
            processInstanceDTO.setBusinessData(getProcessVariableValueByVariableName(entityList, IConstants.PROCESS_BUSINESS_DATA));
            Optional<HistoricVariableInstance> optional = entityList.stream().filter(historicVariableInstance -> historicVariableInstance.getVariableName().equals(IConstants.PROCESS_BUSINESS_ID)).findAny();
            optional.ifPresent(historicVariableInstance -> processInstanceDTO.setBusinessId((Long) historicVariableInstance.getValue()));
            list.add(processInstanceDTO);
        });
        map.put(IConstants.PROCESS_LIST, list);
        return new ResponseResult<>(map);
    }

    private String getProcessVariableValueByVariableName(List<HistoricVariableInstance> entityList, String variableName) {
        if (CollectionUtils.isEmpty(entityList)) {
            return null;
        }
        Optional<HistoricVariableInstance> optional = entityList.stream().filter(historicVariableInstance -> historicVariableInstance.getVariableName().equals(variableName)).findAny();
        return optional.map(historicVariableInstance -> (String) historicVariableInstance.getValue()).orElse("");
    }


    private Date getCreatedOnDate(Integer intervalDate) {
        if (Objects.nonNull(intervalDate) && intervalDate.compareTo(0) > 0) {
            LocalDate currentDate = LocalDate.now();
            LocalDate createDate = currentDate.minusDays(intervalDate);
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = createDate.atStartOfDay().atZone(zone).toInstant();
            return Date.from(instant);
        }
        return null;
    }

    private Date getNextDate(LocalDate localDate) {
        LocalDate newLocalDate = localDate.plusDays(1);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = newLocalDate.atStartOfDay().atZone(zone).toInstant();
        return Date.from(instant);
    }
}
