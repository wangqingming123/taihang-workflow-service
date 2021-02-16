package com.allintechinc.taihang.workflow.handler;

import com.allintechinc.taihang.form.SearchForm;
import com.allintechinc.taihang.workflow.common.ResponseResult;
import com.allintechinc.taihang.workflow.form.ReviewForm;

import java.util.Map;

/**
 * @author sundp
 * 流程任务处理
 */
public interface TaskHandler {

    /**
     * 处理任务审批
     *
     * @param reviewForm
     * @return
     */
    ResponseResult<String> handleTask(ReviewForm reviewForm);

    /**
     * 从引擎获取任务列表
     *
     * @param tenantId
     * @param searchForm
     * @return
     */
    ResponseResult<Map<String, Object>> getTasksByTenantIdAndAssigneeAndStatus(Long tenantId, SearchForm searchForm);


    /**
     * 我的申请列表
     *
     * @param tenantId
     * @param searchForm
     * @return
     */
    ResponseResult<Map<String, Object>> getOwnerApprove(Long tenantId, SearchForm searchForm);
}
