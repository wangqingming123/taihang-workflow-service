package com.allintechinc.taihang.workflow.service;

import com.allintechinc.taihang.exception.BusinessException;
import com.allintechinc.taihang.form.SearchForm;
import com.allintechinc.taihang.request.HttpRequestHolder;
import com.allintechinc.taihang.workflow.dto.ProcessDefinitionDto;
import com.allintechinc.taihang.workflow.dto.ProcessTypeDto;
import com.allintechinc.taihang.workflow.entity.ProcessDefinition;
import com.allintechinc.taihang.workflow.exception.ProcessRecordBusinessException;
import com.allintechinc.taihang.workflow.form.ProcessDefinitionForm;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sundp
 */
public class ProcessRecordServiceTest extends WorkflowServiceTest {

    /**
     * 测试创建流程定义
     */
    @Test
    public void testCreateProcessRecord() {
        HttpRequestHolder.addCurrentTenantId(1L);
        Long id = getProcessRecordService().create(getProcessDefinitionForm());
        Optional<ProcessDefinition> optional = getRecordRepository().findById(id);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertNotEquals(0, id);
    }

    /**
     * 测试重复创建流程定义
     */
    @Test
    public void testCreateExistProcessRecord() {
        try {
            HttpRequestHolder.addCurrentTenantId(1L);
            Long id = getProcessRecordService().create(getProcessDefinitionForm());
            Optional<ProcessDefinition> optional = getRecordRepository().findById(id);
            Assertions.assertTrue(optional.isPresent());
            Assertions.assertNotEquals(0, id);
        } catch (BusinessException e) {
            Assertions.assertEquals(ProcessRecordBusinessException.PROCESS_DEFINITION_EXISTS.getCode(), e.getCode());
        }
    }

    @Test
    public void testCreateWithProcessExist() {
        ProcessDefinitionForm processRecordForm = new ProcessDefinitionForm();
        HttpRequestHolder.addCurrentTenantId(123L);
        processRecordForm.setProcessType("test_approve0");
        processRecordForm.setName("采购审批");

        try {
            getProcessRecordService().create(processRecordForm);
        } catch (BusinessException e) {
            Assertions.assertEquals(ProcessRecordBusinessException.PROCESS_DEFINITION_EXISTS.getCode(), e.getCode());
        }
    }

    /**
     * 测试更换审批人
     */
    @Test
    public void testCreateWithProcessNameRepeat() {
        ProcessDefinitionForm processRecordForm = new ProcessDefinitionForm();
        HttpRequestHolder.addCurrentTenantId(123L);
        processRecordForm.setProcessType("newProcess");
        processRecordForm.setName("test_approve1");

        try {
            getProcessRecordService().create(processRecordForm);
        } catch (BusinessException e) {
            Assertions.assertEquals(ProcessRecordBusinessException.PROCESS_NAME_EXISTS.getCode(), e.getCode());
        }
    }

    /**
     * 测试通过Id查询流程定义
     */
    @Test
    public void testDetail() {
        Long id = 1L;
        try {
            Optional<ProcessDefinition> optional = getRecordRepository().findById(id);
            Assertions.assertTrue(optional.isPresent());
            getProcessRecordService().detail(id);
        } catch (BusinessException e) {
            Assertions.assertEquals(ProcessRecordBusinessException.PROCESS_DEFINITION_EXISTS.getCode(), e.getCode());
        }
    }

    /**
     * 测试流程不存在
     */
    @Test
    public void testDetailNotExist() {
        Long id = 100L;
        try {
            Optional<ProcessDefinition> optional = getRecordRepository().findById(id);
            Assertions.assertFalse(optional.isPresent());
            getProcessRecordService().detail(id);
        } catch (BusinessException e) {
            Assertions.assertEquals(ProcessRecordBusinessException.PROCESS_DEFINITION_NOT_EXISTS.getCode(), e.getCode());
        }
    }

    /**
     * 测试通过租户id查询流程定义
     */
    @Test
    public void testFindAllByTenantId() {
        List<String> listCodes = Lists.newArrayList("test_approve1", "test_approve2", "test_approve3", "test_approve4");
        List<ProcessDefinition> list = getRecordRepository().findAllByTenantIdAndProcessTypeInOrderByCreatedDateDesc(123L, listCodes);
        List<ProcessDefinitionDto> dtoList = Lists.newArrayList();
        Map<String, List<ProcessDefinition>> map = list.stream().collect(Collectors.groupingBy(ProcessDefinition::getProcessType));
        map.forEach((code, recordList) -> {
            recordList.sort(Comparator.comparing(ProcessDefinition::getCreatedDate).reversed());
            ProcessDefinitionDto processRecordDto = new ProcessDefinitionDto();
            BeanUtils.copyProperties(recordList.get(0), processRecordDto);
            dtoList.add(processRecordDto);
        });
        Assertions.assertEquals(4, dtoList.size());
    }

    /**
     * 测试查询流程通过流程类型
     */
    @Test
    public void testFindProcess() {
        int pageNum = 2;
        int pageSize = 2;
        SearchForm<ProcessDefinition> searchForm = new SearchForm<>();
        searchForm.setPageNo(pageNum);
        searchForm.setPageSize(pageSize);
        Map<String, Object> map = new HashMap<>(2);
        map.put("processType", "type_1");
        searchForm.setFilter(map);
        long countFinanceNum = getRecordRepository().count();
        Assertions.assertTrue(countFinanceNum > 0);
        HttpRequestHolder.addCurrentTenantId(123L);
        Page<ProcessDefinitionDto> processDefinitionDtoPage = getProcessRecordService().search(searchForm);
        List<ProcessDefinitionDto> processDefinitionDtoList = processDefinitionDtoPage.getContent();

        Assertions.assertEquals(processDefinitionDtoList.size(), 0);
    }

    /**
     * 测试查询流程
     */
    @Test
    public void testFindProcessNoFilter() {
        int pageNum = 2;
        int pageSize = 2;
        SearchForm<ProcessDefinition> searchForm = new SearchForm<>();
        searchForm.setPageNo(pageNum);
        searchForm.setPageSize(pageSize);
        long countFinanceNum = getRecordRepository().count();
        Assertions.assertTrue(countFinanceNum > 0);
        HttpRequestHolder.addCurrentTenantId(123L);
        Page<ProcessDefinitionDto> processDefinitionDtoPage = getProcessRecordService().search(searchForm);
        List<ProcessDefinitionDto> processDefinitionDtoList = processDefinitionDtoPage.getContent();

        Assertions.assertEquals(processDefinitionDtoList.size(), 2);
    }

    /**
     * 测试查询所有流程类型
     */
    @Test
    public void testFindAllProcessType() {
        HttpRequestHolder.addCurrentTenantId(123L);
        ProcessTypeDto processTypeDto = getProcessRecordService().processTypeList();
        Assertions.assertEquals(processTypeDto.getProcessTypes().size(), 7);
    }

    /**
     * 测试流程定义是否存在
     */
    @Test
    public void testExist() {
        HttpRequestHolder.addCurrentTenantId(1L);
        ProcessDefinitionForm processDefinitionForm = getProcessDefinitionForm();
        getProcessRecordService().create(processDefinitionForm);
        Boolean exist = getProcessRecordService().exist(processDefinitionForm.getProcessType());
        Assertions.assertTrue(exist);
    }

}
