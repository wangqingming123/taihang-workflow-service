package com.allintechinc.taihang.workflow.util;

import com.allintechinc.taihang.workflow.vo.GatewayVO;
import com.allintechinc.taihang.workflow.vo.SequenceFlowVO;
import com.allintechinc.taihang.workflow.vo.UserTaskVO;
import com.google.common.collect.Lists;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author sundp
 */
@Slf4j
public enum DrawProcessUtil {
    /**
     * 实例
     */
    INSTANCE;
    private static final Integer FLOW_RADIX_0 = (1 << 3) - 1;
    private static final Integer FLOW_RADIX_1 = (1 << 4) + 1;
    private static final Integer FLOW_RADIX_2 = (1 << 5) - 1;
    private static final String FILE_NAME = "process.ftl";
    private static final String START_EVENT = "startEvent_1";
    private static final String END_EVENT = "endEvent_1";
    private static final String USER_TASK = "userTask_";
    private static final String GATEWAY = "gateway_";
    private static final String SEQUENCE_FLOW = "flow_";
    private static final String TERMINATE_EVENT = "terminateEvent_1";
    private static final String TASK_KEY = "taskList";
    private static final String GATEWAY_KEY = "gatewayList";
    private static final String FLOW_KEY = "flowList";
    private static final String REJECT_CONDITION = "<conditionExpression xsi:type=\"tFormalExpression\">${outcome=='reject'}</conditionExpression>";
    private static final String APPROVED_CONDITION = "<conditionExpression xsi:type=\"tFormalExpression\">${outcome=='approved'}</conditionExpression>";

    /**
     * 通过模板生成xml
     *
     * @param mapParam
     * @param templateContents
     * @param itemIds
     * @param itemNames
     * @return
     */
    public String getBpmContentsFromTemplate(Configuration configuration, String templateContents, Map<String, Object> mapParam, String[] itemIds, String[] itemNames) throws IOException, TemplateException {

        int taskSize = itemIds.length;
        List<UserTaskVO> taskList = Lists.newArrayListWithExpectedSize(taskSize);
        List<GatewayVO> gatewayList = Lists.newArrayListWithExpectedSize(taskSize);
        List<SequenceFlowVO> flowList = Lists.newArrayList();

        SequenceFlowVO sequenceFlow = new SequenceFlowVO();
        sequenceFlow.setSourceRef(START_EVENT).setTargetRef(USER_TASK + 1).setId(SEQUENCE_FLOW + 1).setName(SEQUENCE_FLOW + 1);
        flowList.add(sequenceFlow);

        for (int i = 0; i < taskSize; i++) {
            int number = i + 1;

            UserTaskVO userTask = new UserTaskVO();
            userTask.setAssignee(itemIds[i]).setId(USER_TASK + number).setName(itemNames[i]);
            taskList.add(userTask);

            GatewayVO gateway = new GatewayVO();
            gateway.setId(GATEWAY + number).setName(GATEWAY + number);
            gatewayList.add(gateway);

            sequenceFlow = new SequenceFlowVO();
            sequenceFlow.setSourceRef(USER_TASK + number).setTargetRef(GATEWAY + number)
                    .setId(SEQUENCE_FLOW + number * FLOW_RADIX_0)
                    .setName(SEQUENCE_FLOW + number * FLOW_RADIX_0);
            flowList.add(sequenceFlow);
            sequenceFlow = new SequenceFlowVO();
            sequenceFlow.setSourceRef(GATEWAY + number)
                    .setTargetRef(TERMINATE_EVENT)
                    .setConditionExpression(REJECT_CONDITION)
                    .setId(SEQUENCE_FLOW + number * FLOW_RADIX_1)
                    .setName(SEQUENCE_FLOW + number * FLOW_RADIX_1);
            flowList.add(sequenceFlow);
            sequenceFlow = new SequenceFlowVO();
            if (i == taskSize - 1) {
                sequenceFlow.setTargetRef(END_EVENT);
            } else {
                sequenceFlow.setTargetRef(USER_TASK + (number + 1));
            }
            sequenceFlow.setSourceRef(GATEWAY + number)
                    .setConditionExpression(APPROVED_CONDITION)
                    .setId(SEQUENCE_FLOW + number * FLOW_RADIX_2)
                    .setName(SEQUENCE_FLOW + number * FLOW_RADIX_2);
            flowList.add(sequenceFlow);
        }

        mapParam.put(TASK_KEY, taskList);
        mapParam.put(GATEWAY_KEY, gatewayList);
        mapParam.put(FLOW_KEY, flowList);

        Template template = new Template(FILE_NAME, templateContents, configuration);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, mapParam);
    }
}

