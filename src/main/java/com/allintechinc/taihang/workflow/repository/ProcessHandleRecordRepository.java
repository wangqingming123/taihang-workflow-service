package com.allintechinc.taihang.workflow.repository;

import com.allintechinc.taihang.workflow.entity.ProcessTaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author sundp
 */
@Repository
public interface ProcessHandleRecordRepository extends JpaRepository<ProcessTaskHistory, Long>, JpaSpecificationExecutor<ProcessTaskHistory> {

    /**
     * 通过租户Id和流程实例Id查询流程处理定义，结果按照创建时间排序
     *
     * @param tenantId
     * @param processId
     * @return
     */
    List<ProcessTaskHistory> findAllByTenantIdAndProcessInstanceIdOrderByBeginTime(Long tenantId, String processId);

    /**
     * 通过租户Id，审批人Id获取流程处理定义
     *
     * @param tenantId
     * @param operatorId
     * @return
     */
    List<ProcessTaskHistory> findAllByTenantIdAndOperatorId(Long tenantId, Long operatorId);

    /**
     * 通过任务Id获取流程操作记录对象
     *
     * @param taskId
     * @return
     */
    Optional<ProcessTaskHistory> findFirstByTaskIdOrderByBeginTimeDesc(String taskId);

    /**
     * 通过任务Id集合获取流程处理定义
     *
     * @param listTaskIds
     * @return
     */
    List<ProcessTaskHistory> findAllByTaskIdInOrderByBeginTimeDesc(List<String> listTaskIds);
}
