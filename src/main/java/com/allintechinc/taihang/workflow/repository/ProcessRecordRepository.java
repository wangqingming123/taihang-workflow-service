package com.allintechinc.taihang.workflow.repository;

import com.allintechinc.taihang.workflow.entity.ProcessDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author sundp
 */
@Repository
public interface ProcessRecordRepository extends JpaRepository<ProcessDefinition, Long>, JpaSpecificationExecutor<ProcessDefinition> {

    /**
     * 通过租户Id、流程类型编码、审批人id和审批人名称查询流程定义
     *
     * @param tenantId
     * @param processTypeCode
     * @param reviewer
     * @param reviewerNames
     * @return
     */
    Optional<ProcessDefinition> findByTenantIdAndProcessTypeAndReviewerIdsAndReviewerNames(Long tenantId, String processTypeCode, String reviewer, String reviewerNames);

    /**
     * 通过租户Id和流程类型编码创建时间倒序查询流程定义
     *
     * @param tenantId
     * @param listCodes
     * @return
     */
    List<ProcessDefinition> findAllByTenantIdAndProcessTypeInOrderByCreatedDateDesc(Long tenantId, List<String> listCodes);

    /**
     * 通过租户Id获取所有流程 按照创建时间倒序
     *
     * @param tenantId
     * @return
     */
    List<ProcessDefinition> findAllByTenantIdOrderByCreatedDateDesc(Long tenantId);

    /**
     * 通过租户Id和流程类型按照创建倒序获取流程定义
     *
     * @param tenantId
     * @param processType
     * @return
     */
    Optional<ProcessDefinition> findFirstByTenantIdAndProcessTypeOrderByCreatedDateDesc(Long tenantId, String processType);

    /**
     * 通过租户Id和processKey按照创建倒序获取流程定义
     *
     * @param tenantId
     * @param processKey
     * @return
     */
    Optional<ProcessDefinition> findFirstByTenantIdAndProcessKeyOrderByCreatedDateDesc(Long tenantId, String processKey);


    /**
     * 通过租户Id获取流程类型列表
     *
     * @param tenantId
     * @return
     */
    List<ProcessDefinition> findAllByTenantId(Long tenantId);


    /**
     * 通过processKeys批量查询流程定义
     *
     * @param processKeys
     * @return
     */
    List<ProcessDefinition> findAllByProcessKeyIn(List<String> processKeys);
}
