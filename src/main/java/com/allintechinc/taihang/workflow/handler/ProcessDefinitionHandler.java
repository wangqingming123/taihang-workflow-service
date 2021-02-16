package com.allintechinc.taihang.workflow.handler;

import com.allintechinc.taihang.workflow.common.ResponseResult;
import com.allintechinc.taihang.workflow.form.ProcessDefinitionForm;

import java.util.Map;

/**
 * @author sundp
 */
public interface ProcessDefinitionHandler {


    /**
     * 生成流程xml内容
     *
     * @param processRecordForm 表单数据
     * @return
     */
    ResponseResult<Map<String, String>> generatorProcessContents(ProcessDefinitionForm processRecordForm);


    /**
     * 流程发布
     *
     * @param tenantId    租户Id
     * @param processName 流程名称
     * @param contents    流程定义内容
     * @return
     */
    ResponseResult<Object> publish(String tenantId, String processName, String contents);

}
