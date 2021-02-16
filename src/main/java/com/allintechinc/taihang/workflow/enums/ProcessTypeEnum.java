package com.allintechinc.taihang.workflow.enums;


import com.allintechinc.taihang.workflow.util.IConstants;

/**
 * @author sundp
 */
public enum ProcessTypeEnum {
    /**
     * 材料采购申请
     */
    MATERIAL_PURCHASE(IConstants.MATERIAL_PURCHASE, "材料采购申请"),
    /**
     * 生产订单
     */
    PRODUCTION_ORDER(IConstants.PRODUCTION_ORDER, "生产订单"),
    /**
     * 采购订单
     */
    PURCHASE_ORDER(IConstants.PURCHASE_ORDER, "采购订单"),
    /**
     * 销售订单
     */
    SALES_ORDER(IConstants.SALES_ORDER, "销售订单"),
    /**
     * 开立数字账户
     */
    OPEN_DIGITAL_ACCOUNT("open_digital_account", "开立数字账户"),
    /**
     * 变更数字账户
     */
    CHANGE_DIGITAL_ACCOUNT("change_digital_account", "变更数字账户");

    private final String processTypeKey;
    private final String processTypeValue;

    ProcessTypeEnum(String processTypeKey, String processTypeValue) {
        this.processTypeKey = processTypeKey;
        this.processTypeValue = processTypeValue;
    }

    public String getProcessTypeKey() {
        return processTypeKey;
    }

    public String getProcessTypeValue() {
        return processTypeValue;
    }

    public static String getProcessTypeKey(String processTypeValue) {
        for (ProcessTypeEnum processTypeEnum : ProcessTypeEnum.values()) {
            if (processTypeEnum.getProcessTypeValue().equals(processTypeValue)) {
                return processTypeEnum.getProcessTypeKey();
            }
        }
        return null;
    }

}
