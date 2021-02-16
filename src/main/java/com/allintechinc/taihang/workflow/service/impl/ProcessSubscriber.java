package com.allintechinc.taihang.workflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.allintechinc.kunlun.pubsub.subscribe.AbstractSubscriber;
import com.allintechinc.taihang.workflow.exception.ProcessRecordBusinessException;
import com.allintechinc.taihang.workflow.form.ProcessParamForm;
import com.allintechinc.taihang.workflow.service.ProcessHandleRecordService;
import com.allintechinc.taihang.workflow.util.IConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * @author sundp
 */
@Component(IConstants.WORKFLOW_TOPIC)
@Slf4j
public class ProcessSubscriber extends AbstractSubscriber {

    @Autowired
    private ProcessHandleRecordService processHandleRecordService;

    @Override
    public void execute(String message) {
        if (StringUtils.isNotEmpty(message)) {
            log.info("receive startup process message:{}", message);
            ProcessParamForm processParamForm = JSON.parseObject(message, ProcessParamForm.class);
            if (Objects.isNull(processParamForm.getTenantId())) {
                throw ProcessRecordBusinessException.TENANT_ID_NOT_EXISTS;
            }
            //启动流程
            processHandleRecordService.startProcessInstance(processParamForm);
        }
    }

    @Override
    public String getGroupName() {
        return IConstants.CONSUMER_GROUP;
    }
}
