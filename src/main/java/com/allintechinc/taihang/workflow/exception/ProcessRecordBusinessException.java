/**
 * This is auto-generated by code generator tools, YOU *MUST* Implement all the methods!!!
 */
package com.allintechinc.taihang.workflow.exception;

import com.allintechinc.taihang.exception.BusinessException;

/**
 * @author sundp
 */
public class ProcessRecordBusinessException extends BusinessException {
    private static final long serialVersionUID = 5237817902041664512L;

    public static final ProcessRecordBusinessException PROCESS_DEFINITION_EXISTS = new ProcessRecordBusinessException("process.definition.exists", "流程定义已经存在!");
    public static final ProcessRecordBusinessException PROCESS_DEFINITION_NOT_EXISTS = new ProcessRecordBusinessException("process.definition.not.exists", "流程定义不存在!");
    public static final ProcessRecordBusinessException PROCESS_INSTANCE_NOT_EXISTS = new ProcessRecordBusinessException("process.instance.not.exists", "流程实例不存在!");
    public static final ProcessRecordBusinessException GENERATOR_FLOW_CONTENTS_ERROR = new ProcessRecordBusinessException("generator.flow.contents.error", "生成流程内容错误!");
    public static final ProcessRecordBusinessException PROCESS_PARTICIPANTS_NUMBER_NOT_MATCH = new ProcessRecordBusinessException("the_number_of_participants_does_not_match", "审批人数量不匹配!");
    public static final ProcessHandleRecordException PROCESS_NAME_EXISTS = new ProcessHandleRecordException("process.name.exists", "流程名称已存在!");
    public static final ProcessHandleRecordException PROCESS_ASSIGNEE_AND_STATUS_CANNOT_BE_EMPTY = new ProcessHandleRecordException("process.assignee.and.status.can.not.be.empty", "申请人和流程状态不能为空!");
    public static final ProcessHandleRecordException TENANT_ID_NOT_EXISTS = new ProcessHandleRecordException("tenant.id.not.exists", "租户编码不能为空!");

    public ProcessRecordBusinessException(String code, String message) {
        super(code, message);
    }

}