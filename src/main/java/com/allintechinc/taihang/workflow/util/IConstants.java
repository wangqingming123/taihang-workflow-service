package com.allintechinc.taihang.workflow.util;

import java.time.format.DateTimeFormatter;

/**
 * 常量接口
 *
 * @author sundp
 */
public interface IConstants {

    String PROCESS_ASSIGNEE = "assignee";
    String PROCESS_APPLY_USER_ID = "applyUserId";
    String PROCESS_APPLY_USER = "applyUser";
    String PROCESS_BUSINESS_CODE = "businessCode";
    String PROCESS_BUSINESS_ID = "businessId";
    String PROCESS_PROCESS_TYPE = "processType";
    String PROCESS_PROCESS_NAME = "processName";
    String PROCESS_STATUS = "status";
    String PROCESS_COMMENT = "comment";
    String PROCESS_CONTENTS = "contents";
    String PROCESS_KEY = "processKey";
    String PROCESS_BUSINESS_DATA = "businessData";
    String PROCESS_INTERVAL_DATE = "intervalDate";
    String PROCESS_APPLICATION_NAME = "applicationName";
    String PROCESS_REVIEWER_ID = "reviewerId";
    String PROCESS_REVIEWER_NAME = "reviewerName";
    String SYSTEM = "System";
    String PROCESS_END = "流程结束";
    String TENANT_ID = "tenantId";
    String HANDLE_OUTCOME = "outcome";

    String CREATE_TIME = "createTime";
    String DATE_FORMAT = "yyyy-MM-dd";
    String TASK_LIST = "taskList";
    String PROCESS_LIST = "processList";
    String TOTAL = "total";

    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    String CREATE_DATE_MIN = "min";
    String CREATE_DATE_MAX = "max";
    /**
     * 材料采购申请
     */
    String MATERIAL_PURCHASE = "material_purchase";

    /**
     * 生产订单
     */
    String PRODUCTION_ORDER = "production_order";

    /**
     * 采购订单
     */
    String PURCHASE_ORDER = "purchase_order";

    /**
     * 销售订单
     */
    String SALES_ORDER = "sales_order";

    String WORKFLOW_TOPIC = "workflow";

    String CONSUMER_GROUP = "allin-pubsub";
}
