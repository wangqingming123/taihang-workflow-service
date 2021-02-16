package com.allintechinc.taihang.workflow.handler.impl;

import com.allintechinc.taihang.workflow.common.ResponseResult;
import com.allintechinc.taihang.workflow.exception.ProcessRecordBusinessException;
import com.allintechinc.taihang.workflow.form.ProcessDefinitionForm;
import com.allintechinc.taihang.workflow.handler.ProcessDefinitionHandler;
import com.allintechinc.taihang.workflow.util.DrawProcessUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author sundp
 */
@Slf4j
@Component
public class ProcessDefinitionHandlerImpl implements ProcessDefinitionHandler {

    private final RepositoryService repositoryService;
    private final Configuration configuration;
    private final ResourceLoader resourceLoader;

    private static final String FILE_PATH = "classpath:processes/process.ftl";
    private static final String PROCESS_NAME = "processes/{0}.bpmn20.bpmn";

    @Autowired
    public ProcessDefinitionHandlerImpl(RepositoryService repositoryService, Configuration configuration, ResourceLoader resourceLoader) {
        this.repositoryService = repositoryService;
        this.configuration = configuration;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public ResponseResult<Map<String, String>> generatorProcessContents(ProcessDefinitionForm processRecordForm) {
        String reviewerIds = processRecordForm.getReviewerIds();
        String reviewerNames = processRecordForm.getReviewerNames();

        String[] itemIds = StringUtils.split(reviewerIds, ",");
        String[] itemNames = StringUtils.split(reviewerNames, ",");
        if (itemIds.length != itemNames.length) {
            throw ProcessRecordBusinessException.PROCESS_PARTICIPANTS_NUMBER_NOT_MATCH;
        }
        String processKey = processRecordForm.getProcessType() + '_' + System.currentTimeMillis();
        Map<String, Object> mapParam = Maps.newHashMapWithExpectedSize(16);
        mapParam.put("processKey", processKey);
        mapParam.put("processName", processRecordForm.getProcessType());
        String contents;
        try {
            String templateContents = getTemplateContents();
            contents = DrawProcessUtil.INSTANCE.getBpmContentsFromTemplate(configuration, templateContents, mapParam, itemIds, itemNames);
        } catch (IOException |
                TemplateException e) {
            throw ProcessRecordBusinessException.GENERATOR_FLOW_CONTENTS_ERROR;
        }

        Map<String, String> map = new HashMap<>(2);
        map.put("processKey", processKey);
        map.put("contents", contents);
        return ResponseResult.success(map);
    }

    private String getTemplateContents() throws IOException {
        StringBuilder builder = new StringBuilder();
        if (Objects.isNull(resourceLoader)) {
            String filePath = ResourceUtils.getFile(FILE_PATH).getPath();
            List<String> stringList = Files.readLines(new File(filePath), Charsets.UTF_8);
            for (String s : stringList) {
                builder.append(s).append('\n');
            }
            return builder.toString();
        }
        Resource resource = resourceLoader.getResource(FILE_PATH);
        try (InputStream inputStream = resource.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String data;
            while ((data = bufferedReader.readLine()) != null) {
                builder.append(data).append('\n');
            }
        } catch (IOException e) {
            throw ProcessRecordBusinessException.GENERATOR_FLOW_CONTENTS_ERROR;
        }
        return builder.toString();
    }

    @Override
    public ResponseResult<Object> publish(String tenantId, String processName, String contents) {
        if (StringUtils.isEmpty(contents)) {
            log.error("{} 流程定义为空!", processName);
            return ResponseResult.error(processName + " 流程定义为空");
        }
        Deployment deployment = repositoryService.createDeployment()
                .addBytes(MessageFormat.format(PROCESS_NAME, processName), contents.getBytes())
                .tenantId(tenantId)
                .deploy();
        return ResponseResult.success(deployment.getId());
    }
}
