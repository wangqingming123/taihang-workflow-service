package com.allintechinc.taihang.workflow.service;

import com.allintechinc.taihang.request.HttpRequestHolder;
import com.allintechinc.taihang.workflow.form.ProcessDefinitionForm;
import com.allintechinc.taihang.workflow.form.ProcessParamForm;
import com.allintechinc.taihang.workflow.handler.ProcessDefinitionHandler;
import com.allintechinc.taihang.workflow.handler.ProcessInstanceHandler;
import com.allintechinc.taihang.workflow.handler.TaskHandler;
import com.allintechinc.taihang.workflow.handler.impl.ProcessDefinitionHandlerImpl;
import com.allintechinc.taihang.workflow.handler.impl.ProcessInstanceHandlerImpl;
import com.allintechinc.taihang.workflow.handler.impl.TaskHandlerImpl;
import com.allintechinc.taihang.workflow.repository.ProcessHandleRecordRepository;
import com.allintechinc.taihang.workflow.repository.ProcessRecordRepository;
import com.allintechinc.taihang.workflow.service.impl.ProcessHandleRecordServiceImpl;
import com.allintechinc.taihang.workflow.service.impl.ProcessRecordServiceImpl;
import freemarker.template.Configuration;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.flowable.engine.*;
import org.flowable.engine.impl.IdentityServiceImpl;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.UUID;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

/**
 * @author duke
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = "spring.flyway.locations=classpath:/db/flyway/workflow")
@AutoConfigureEmbeddedDatabase(beanName = "dataSource")
@ActiveProfiles("unit")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
@Getter
public class WorkflowServiceTest {

    private static final String DDL_PATH = "classpath:db/flyway/workflow/V20200722__Workflow_Test.sql";

    @Qualifier("dataSource")
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ProcessHandleRecordRepository processHandleRecordRepository;
    @Autowired
    private ProcessRecordRepository recordRepository;
    @Autowired
    private ResourceLoader resourceLoader;

    private ProcessHandleRecordService processHandleRecordService;
    private ProcessRecordService processRecordService;
    private TaskHandler taskHandler;

    private TaskService taskService;

    @BeforeEach
    public void testBefore() {
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration().setDataSource(dataSource).setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_DROP_CREATE);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HistoryService historyService = processEngine.getHistoryService();
        taskService = processEngine.getTaskService();
        ProcessInstanceHandler processInstanceHandler = new ProcessInstanceHandlerImpl(runtimeService, new IdentityServiceImpl((ProcessEngineConfigurationImpl) processEngineConfiguration), taskService);
        ProcessDefinitionHandler processHandler = new ProcessDefinitionHandlerImpl(processEngine.getRepositoryService(), new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS), resourceLoader);
        taskHandler = new TaskHandlerImpl(runtimeService, taskService, historyService);
        processRecordService = new ProcessRecordServiceImpl(recordRepository, processHandler);
        processHandleRecordService = new ProcessHandleRecordServiceImpl(processInstanceHandler, taskHandler, recordRepository, processHandleRecordRepository, historyService, taskService);
        initTable();
    }

    @SneakyThrows
    private void initTable() {
        String filePath = ResourceUtils.getFile(DDL_PATH).getPath();
        try (FileInputStream fileInputStream = new FileInputStream(filePath); InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8)) {
            ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
            runner.setEscapeProcessing(false);
            runner.setSendFullScript(true);
            runner.runScript(inputStreamReader);
        } catch (FileNotFoundException | SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 流程定义对象
     *
     * @return
     */
    public ProcessDefinitionForm getProcessDefinitionForm() {
        ProcessDefinitionForm processRecordForm = new ProcessDefinitionForm();
        processRecordForm.setProcessType("test_approve9");
        processRecordForm.setName("采购审批");
        processRecordForm.setReviewerIds("1,2,3,4,5");
        processRecordForm.setReviewerNames("abc,def,ghi,jkl,mno");
        return processRecordForm;
    }

    /**
     * 流程启动对象
     *
     * @return
     */
    public ProcessParamForm getProcessParamForm() {
        ProcessParamForm processParamForm = new ProcessParamForm();
        processParamForm.setTenantId(HttpRequestHolder.getCurrentTenantId());
        processParamForm.setApplyUserId(99L);
        processParamForm.setApplyUser("张三");
        processParamForm.setBusinessCode(UUID.randomUUID().toString());
        processParamForm.setBusinessData("");
        processParamForm.setBusinessId(1L);
        processParamForm.setProcessKey("");
        processParamForm.setProcessType("test_approve9");
        return processParamForm;
    }
}
