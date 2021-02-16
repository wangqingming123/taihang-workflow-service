package com.allintechinc.taihang.workflow.enums;

/**
 * @author sundp
 * 流程状态：草稿 提交成功 审批中 审批通过 驳回 拒绝 结束(归档) 取消
 */
public enum ProcessStatusEnum {
    /**
     * 草稿
     */
    DRAFT,
    /**
     * 提交成功
     */
    SUBMITTED,
    /**
     * 审批中
     */
    IN_PROCESS,
    /**
     * 审批通过
     */
    APPROVED,
    /**
     * 驳回
     */
    REJECT,
    /**
     * 拒绝
     */
    REFUSE,
    /**
     * 结束(归档)
     */
    END,
    /**
     * 取消
     */
    CANCEL,
    /**
     * 未开始
     */
    NOT_STARTED;
}
