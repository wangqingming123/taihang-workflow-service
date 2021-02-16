package com.allintechinc.taihang.workflow.service;

import com.alibaba.fastjson.JSONObject;
import com.allintechinc.taihang.form.SearchForm;
import com.allintechinc.taihang.request.HttpRequestHolder;
import com.allintechinc.taihang.workflow.dto.ApplyUserDto;
import com.allintechinc.taihang.workflow.dto.ProcessTaskDto;
import com.allintechinc.taihang.workflow.dto.ProcessTaskHistoryDto;
import com.allintechinc.taihang.workflow.enums.ProcessStatusEnum;
import com.allintechinc.taihang.workflow.form.ApplyUserForm;
import com.allintechinc.taihang.workflow.form.ReviewForm;
import com.allintechinc.taihang.workflow.form.UserForm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author sundp
 */
public class ProcessTaskHistoryServiceTest extends WorkflowServiceTest {

    /**
     * 测试启动流程
     */
    @Test
    public void testStartWorkflow() {
        HttpRequestHolder.addCurrentTenantId(1L);
        ProcessTaskDto processTaskDto = startProcess();
        Assertions.assertEquals(processTaskDto.getApplyUser(), "张三");
        Assertions.assertEquals(processTaskDto.getAssignee(), "1");
    }

    /**
     * 测试从某用户所有参会的流程中获取申请人
     */
    @Test
    public void testGetAllUsers() {
        HttpRequestHolder.addCurrentTenantId(1L);
        ApplyUserForm applyUserForm = new ApplyUserForm();
        applyUserForm.setReviewerId(226L);
        applyUserForm.setReviewerName("test2");
        List<ApplyUserDto> applyUserDtoList = getProcessHandleRecordService().getAllApplyUsers(applyUserForm);
        Assertions.assertEquals(applyUserDtoList.size(), 1);
    }

    /**
     * 测试查询待办任务
     */
    @Test
    public void testToDoSearch() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(1L);
        startProcess();
        SearchForm searchForm = new SearchForm<>();
        searchForm.setPageNo(1);
        searchForm.setPageSize(10);
        searchForm.setFilter(new JSONObject().fluentPut("status", "IN_PROCESS").fluentPut("processType", "test_approve9").fluentPut("assignee", "abc"));
        Page<ProcessTaskHistoryDto> processTaskHistoryDTOs = getProcessHandleRecordService().search(searchForm);
        List<ProcessTaskHistoryDto> historyDtoList = processTaskHistoryDTOs.getContent();
        Assertions.assertEquals(1, historyDtoList.size());
        Assertions.assertEquals("1", historyDtoList.get(0).getOperatorName());
        Assertions.assertEquals("IN_PROCESS", historyDtoList.get(0).getStatus());
        Assertions.assertEquals("test_approve9", historyDtoList.get(0).getProcessType());
    }

    /**
     * 测试查询已办任务
     */
    @Test
    public void testFinishSearch() {
        testReject();
        SearchForm searchForm = new SearchForm<>();
        searchForm.setPageNo(1);
        searchForm.setPageSize(10);
        searchForm.setFilter(new JSONObject().fluentPut("status", ProcessStatusEnum.REJECT.toString()).fluentPut("processType", "test_approve9").fluentPut("assignee", "abc"));
        Page<ProcessTaskHistoryDto> processTaskHistoryDTOs = getProcessHandleRecordService().search(searchForm);
        List<ProcessTaskHistoryDto> historyDtoList = processTaskHistoryDTOs.getContent();
        Assertions.assertEquals(1, historyDtoList.size());
        Assertions.assertEquals("1", historyDtoList.get(0).getOperatorName());
        Assertions.assertEquals(ProcessStatusEnum.REJECT.toString(), historyDtoList.get(0).getStatus());
        Assertions.assertEquals("test_approve9", historyDtoList.get(0).getProcessType());
    }

    /**
     * 测试驳回
     */
    @Test
    public void testReject() {
        ReviewForm reviewForm = getReviewForm();
        getProcessHandleRecordService().reject(reviewForm);
    }

    /**
     * 测试同意
     */
    @Test
    public void testApproved() {
        ReviewForm reviewForm = getReviewForm();
        getProcessHandleRecordService().approved(reviewForm);
    }

    /**
     * 测试查询流程审批历史
     */
    @Test
    public void testDetail() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(1L);
        ProcessTaskDto processTaskDto = startProcess();
        List<ProcessTaskHistoryDto> processTaskHistoryDtoList = getProcessHandleRecordService().detail(processTaskDto.getProcessInstanceId());
        Assertions.assertEquals(5, processTaskHistoryDtoList.size());
        ProcessTaskHistoryDto processTaskHistoryDto = processTaskHistoryDtoList.get(3);
        Assertions.assertNotNull(processTaskHistoryDto);
        Assertions.assertEquals("jkl", processTaskHistoryDto.getOperatorName());
        Assertions.assertEquals("NOT_STARTED", processTaskHistoryDto.getStatus());

    }

    /**
     * 测试通过流程实例Id获取流程实例信息
     */
    @Test
    public void testFindByProcessId() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(1L);
        ProcessTaskDto processTaskDto = startProcess();
        ProcessTaskHistoryDto processTaskHistoryDto = getProcessHandleRecordService().getProcess(processTaskDto.getProcessInstanceId());
        Assertions.assertNotNull(processTaskHistoryDto);
        Assertions.assertEquals("采购审批", processTaskHistoryDto.getProcessInstanceName());
        Assertions.assertEquals("IN_PROCESS", processTaskHistoryDto.getStatus());
    }

    /**
     * 测试通过任务Id获取审批信息
     */
    @Test
    public void testFindByTaskId() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(1L);
        ProcessTaskHistoryDto processTaskHistoryDto = getProcessHandleRecordService().findByTaskId("a91b8ddd-9ef6-11ea-99d0-080027d76b76");
        Assertions.assertNotNull(processTaskHistoryDto);
        Assertions.assertEquals("9fd0403a-9ef6-11ea-99d0-080027d76b76", processTaskHistoryDto.getProcessInstanceId());
        Assertions.assertEquals("123456", processTaskHistoryDto.getBusinessCode());
        Assertions.assertEquals("REJECT", processTaskHistoryDto.getStatus());
    }

    /**
     * 测试我的申请
     */
    @Test
    public void testOwnerApprove() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(99L);
        startProcess();
        SearchForm searchForm = new SearchForm<>();
        searchForm.setPageNo(1);
        searchForm.setPageSize(10);
        searchForm.setFilter(new JSONObject().fluentPut("applicationName", "张三"));
        Page<ProcessTaskHistoryDto> processTaskHistoryDTOs = getProcessHandleRecordService().getOwnerApprove(searchForm);
        List<ProcessTaskHistoryDto> historyDtoList = processTaskHistoryDTOs.getContent();
        Assertions.assertEquals(1, historyDtoList.size());
        Assertions.assertEquals("IN_PROCESS", historyDtoList.get(0).getStatus());
        Assertions.assertEquals("test_approve9", historyDtoList.get(0).getProcessType());
        Assertions.assertEquals("张三", historyDtoList.get(0).getApplyUser());
    }

    /**
     * 测试是否有申请的流程
     */
    @Test
    public void testFindUserFromWorkflowAsApplyUser() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(99L);
        startProcess();
        UserForm userForm = new UserForm();
        userForm.setUserId(HttpRequestHolder.getCurrentUserId());
        userForm.setUserName("张三");
        boolean flag = getProcessHandleRecordService().findUserFromWorkflow(userForm);
        Assertions.assertTrue(flag);
    }

    /**
     * 测试某用户是否是审批人
     */
    @Test
    public void testFindUserFromWorkflowAsAssignee() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(1L);
        getProcessRecordService().create(getProcessDefinitionForm());
        UserForm userForm = new UserForm();
        userForm.setUserId(HttpRequestHolder.getCurrentUserId());
        userForm.setUserName("abc");
        boolean flag = getProcessHandleRecordService().findUserFromWorkflow(userForm);
        Assertions.assertTrue(flag);
    }


    /**
     * 启动流程
     *
     * @return
     */
    private ProcessTaskDto startProcess() {
        getProcessRecordService().create(getProcessDefinitionForm());
        return getProcessHandleRecordService().startProcessInstance(getProcessParamForm());
    }

    private ReviewForm getReviewForm() {
        HttpRequestHolder.addCurrentTenantId(1L);
        HttpRequestHolder.addCurrentUserId(1L);
        ProcessTaskDto processTaskDto = startProcess();
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setTaskId(processTaskDto.getTaskId());
        reviewForm.setProcessInstanceId(processTaskDto.getProcessInstanceId());
        reviewForm.setReviewerId(HttpRequestHolder.getCurrentUserId());
        reviewForm.setReviewerName("abc");
        reviewForm.setComment("测试审批");
        return reviewForm;
    }

}
